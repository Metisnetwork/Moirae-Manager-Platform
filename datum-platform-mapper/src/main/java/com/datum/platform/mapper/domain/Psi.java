package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * PSI结果表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_psi")
public class Psi implements Serializable {


    /**
     * 模型元数据id
     */
    @TableId
    private String metaDataId;

    /**
     * 所属组织
     */
    private String identityId;

    /**
     * 名称
     */
    private String name;

    /**
     * 源文件ID
     */
    private String fileId;

    /**
     * 源文件存放路径
     */
    private String filePath;

    /**
     * 训练模型的任务id
     */
    private String trainTaskId;

    /**
     * 训练模型的算法id, 母算法
     */
    private Long trainAlgorithmId;

    /**
     * 训练模型的账户
     */
    private String trainUserAddress;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 原始数据的类型
     */
    private Integer dataType;

    /**
     * 元数据的选项，和 data_type 配套使用
     */
    private String metadataOption;
    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private Org org;
}
