package com.platon.rosettaflow.utils;

import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.vo.algorithm.AlgorithmListVo;
import com.platon.rosettaflow.vo.projectTemplate.ProjectTemplateVo;
import com.platon.rosettaflow.vo.user.UserVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public class ConvertUtils {

    public static UserVo convert2Vo(UserDto userDto) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDto, userVo);
        return userVo;
    }

    /**
     * 对象转换工具
     * @param o1
     * @param o2
     * @return
     */
    public static Object convertToVo(Object o1, Object o2) {
        BeanUtils.copyProperties(o1, o2);
        return o2;
    }

    /**
     * 列表对象转换工具
     * @param list1 数组
     * @param o2 对象
     * @param <T> 泛型
     * @return
     */
    public static <T> List<T> convertToVoList(List<T> list1, T o2) {
        List<T> list2 = new ArrayList();
        list1.stream().forEach(o1 -> {
            BeanUtils.copyProperties(o1, o2);
            list2.add(o2);
            }
        );
        return list2;
    }

    public static ProjectTemplateVo convert2Vo(ProjectTemplateDto projectTemplateDto) {
        ProjectTemplateVo projectTemplateVo = new ProjectTemplateVo();
        projectTemplateVo.setId(projectTemplateDto.getId());
        projectTemplateVo.setProjectName(projectTemplateDto.getProjectName());
        projectTemplateVo.setProjectDesc(projectTemplateDto.getProjectDesc());
        return projectTemplateVo;
    }

    public static List<ProjectTemplateVo> convert2Vo(List<ProjectTemplateDto> projectTemplateDtoList) {
        List<ProjectTemplateVo> list = new ArrayList<>();
        if (null != projectTemplateDtoList) {
            projectTemplateDtoList.forEach(projectTemplateDto -> list.add(convert2Vo(projectTemplateDto)));
        }
        return list;
    }

//    public static List<ProjectTemplateVo> convert2Vo(List<ProjectTemplateDto> projectTemplateDtoList) {
//        List<ProjectTemplateVo> list = new ArrayList<>();
//        if (null != projectTemplateDtoList) {
//            projectTemplateDtoList.forEach(projectTemplateDto -> list.add(convert2Vo(projectTemplateDto)));
//        }
//        return list;
//    }
}
