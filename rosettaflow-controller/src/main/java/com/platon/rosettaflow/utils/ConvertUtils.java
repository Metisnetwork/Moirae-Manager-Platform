package com.platon.rosettaflow.utils;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public class ConvertUtils {

    /**
     * 列表对象非并行转换工具
     * @param collection
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertSerialToList(Collection<?> collection, Class<T> tClass) {
        List<T> list2 = new ArrayList<>();
        synchronized (list2) {
            collection.stream().forEach(o1 -> {
                T target = ReflectUtil.newInstanceIfPossible(tClass);
                BeanUtils.copyProperties(o1, target);
                list2.add(target);
            });
        }
        return list2;
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
        synchronized (list2) {
            list1.parallelStream().forEach(o1 -> {
                T target = ReflectUtil.newInstanceIfPossible(tClass);
                BeanUtils.copyProperties(o1, target);
                list2.add(target);
            });
        }
        return list2;
    }

//    public static ProjectTemplateVo convert2Vo(ProjectTemplateDto projectTemplateDto) {
//        ProjectTemplateVo projectTemplateVo = new ProjectTemplateVo();
//        projectTemplateVo.setId(projectTemplateDto.getId());
//        projectTemplateVo.setProjectName(projectTemplateDto.getProjectName());
//        projectTemplateVo.setProjectDesc(projectTemplateDto.getProjectDesc());
//        return projectTemplateVo;
//    }
//
//    public static List<ProjectTemplateVo> convert2Vo(List<ProjectTemplateDto> projectTemplateDtoList) {
//        List<ProjectTemplateVo> list = new ArrayList<>();
//        if (null != projectTemplateDtoList) {
//            projectTemplateDtoList.forEach(projectTemplateDto -> list.add(convert2Vo(projectTemplateDto)));
//        }
//        return list;
//    }

//    public static List<ProjectTemplateVo> convert2Vo(List<ProjectTemplateDto> projectTemplateDtoList) {
//        List<ProjectTemplateVo> list = new ArrayList<>();
//        if (null != projectTemplateDtoList) {
//            projectTemplateDtoList.forEach(projectTemplateDto -> list.add(convert2Vo(projectTemplateDto)));
//        }
//        return list;
//    }
}
