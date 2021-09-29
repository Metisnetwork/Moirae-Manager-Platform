package com.platon.rosettaflow.interceptor;


import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
@Slf4j
public class MybatisFinalSqlPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();

        if (invocation.getTarget() instanceof StatementHandler) {

            //获取Statement 对象
            Connection conn = (Connection) invocation.getArgs()[0];
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            Statement stmt = handler.prepare(conn, 30);
            handler.parameterize(stmt);

            //获取真实对象
            MetaObject metaObject = SystemMetaObject.forObject(stmt);

            while (metaObject.hasGetter("h")) {
                Object obj = metaObject.getValue("h");
                // 将对象包装成MetaObject对象后就成了真实对象，然后就可以通过反射技术可以操作真实对象的所有属性
                metaObject = SystemMetaObject.forObject(obj);
            }

            //通过反射获取 Statement 对象上的FinalSql
            //todo:后续用 metaObject.getValue() 重构下面的反射代码，提升性能

            Field hField = metaObject.getClass().getDeclaredField("originalObject");
            hField.setAccessible(true);
            Object hObj = hField.get(metaObject);

            Field statementField = hObj.getClass().getDeclaredField("statement");
            statementField.setAccessible(true);
            Object statementObj = statementField.get(hObj);

            Field stmtField = statementObj.getClass().getDeclaredField("stmt");
            stmtField.setAccessible(true);
            Object stmtObj = stmtField.get(statementObj);

            Field statementArrivedField = stmtObj.getClass().getDeclaredField("statement");
            statementArrivedField.setAccessible(true);
            Object statementArrivedFieldObj = statementArrivedField.get(stmtObj);


            String finalSql = statementArrivedFieldObj.toString();

            //去掉不要的字符串
            finalSql = finalSql.substring(finalSql.lastIndexOf(":") + 1, finalSql.length() - 1);

            log.info("最终sql： \n " + finalSql);

        }

        //做了下性能测试 平均耗时为 1，2毫秒，非常低，不错！
        log.debug("抓取最终sql 耗时：" + stopwatch);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }



}
