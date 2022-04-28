package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mo_model
 *
 * @author admin
 */
@Data
@TableName(value = "mo_model")
public class Model implements Serializable {

    /**
     * 元数据id
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
     * 训练模型的算法id
     */
    private Long trainAlgorithmId;
    /**
     * 训练模型的账户
     */
    private String trainUserAddress;
    /**
     * 输入模型的算法id
     */
    private Long supportedAlgorithmId;
    /**
     * 模型评估结果
     */
    private String evaluate;
    /**
     * 原始数据的类型
     */
    private Integer dataType;

    /**
     * 元数据的选项，和 data_type 配套使用
     */
    private String metadataOption;
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

    /**
     * 模型主键id
     */
    @TableField(exist = false)
    private Long modelId;
    /**
     * 模型所属的机构id
     */
    @TableField(exist = false)
    private String originId;
    /**
     * 模型名称
     */
    @TableField(exist = false)
    private String fileName;
    /**
     * 模型由哪个算法生成
     */
    @TableField(exist = false)
    private String algorithmName;
    @TableField(exist = false)
    private String algorithmNameEn;
    @TableField(exist = false)
    private String nodeName;
    @TableField(exist = false)
    private String imageUrl;
    @TableField(exist = false)
    private Org org;
}
