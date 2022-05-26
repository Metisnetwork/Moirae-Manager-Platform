package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 算法分类表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_algorithm_classify")
public class AlgorithmClassify implements Serializable {

    /**
     * 分类id
     */
    @TableId
    private Long id;

    /**
     * 父分类id，如果为顶级分类，则为0
     */
    private Long parentId;

    /**
     * 分类中文名称
     */
    private String name;

    /**
     * 英文算法名称
     */
    private String nameEn;

    /**
     * 算法图片url
     */
    private String imageUrl;

    /**
     * 是否可用: 0-否，1-是
     */
    private Boolean isAvailable;

    /**
     * 是否算法: 0-否，1-是
     */
    private Boolean isAlgorithm;

    /**
     * 是否存在对应算法: 0-否，1-是
     */
    private Boolean isExistAlgorithm;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;
    private static final long serialVersionUID = 1L;



    @TableField(exist = false)
    private Algorithm alg;

    @TableField(exist = false)
    private List<AlgorithmClassify> childrenList;

}
