package com.moirae.rosettaflow.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.vo.PageVo;

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

}
