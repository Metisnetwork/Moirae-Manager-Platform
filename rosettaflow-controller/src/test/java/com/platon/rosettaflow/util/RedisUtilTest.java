//package com.platon.rosettaflow.util;
//
//import com.platon.rosettaflow.common.constants.SysConstant;
//import com.platon.rosettaflow.common.utils.RedisUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class RedisUtilTest {
//
//    @Resource
//    private RedisUtil redisUtil;
//
//
//    @Test
//    public void saveList(){
//
//        String content = "{\"beginTime\":1631784269000,\"desc\":\"作业20210916描述\",\"endTime\":1631784269000,\"id\":18,\"jobStatus\":0,\"name\":\"作业20210916\",\"repeatFlag\":1,\"repeatInterval\":1,\"workflowId\":21}";
//        try{
//            for (int i = 0; i < 10; i++) {
//                redisUtil.listLeftPush(SysConstant.JOB_ADD_QUEUE, content, null);
//            }
//            System.out.println("-----redis save list success");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//
//
//
//    }
//
//    @Test
//    public void listSize(){
//       long size = redisUtil.listSize("lKey10");
//       System.out.println("-----listSize:" + size);
//    }
//
//
//}
