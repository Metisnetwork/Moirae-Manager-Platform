package com.platon.rosettaflow.utils;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.vo.PageVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public class ConvertUtils {

    public static <T> PageVo<T> convertPageVo(IPage<?> page, List<T> items) {
        PageVo<T> pageVo = new PageVo<>();
        pageVo.setCurrent(page.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(page.getSize());
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    /**
     * 列表对象并行转换工具(性能好，会乱序)
     * @param list1
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertParallelToList(List list1, Class<T> tClass) {
        List<T> list2 = new ArrayList();
        list1.parallelStream().forEach(o1 -> {
            T target = ReflectUtil.newInstanceIfPossible(tClass);
            BeanUtils.copyProperties(o1, target);
            list2.add(target);
        });
        return list2;
    }
}
