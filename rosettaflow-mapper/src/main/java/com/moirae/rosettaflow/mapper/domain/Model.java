package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_model
 *
 * @author admin
 */
@Data
@TableName(value = "t_model")
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 元数据表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 所属组织
     */
    private String orgIdentityId;
    /**
     * 名称
     */
    private String name;
    /**
     * 元数据id
     */
    private String metaDataId;
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
    private String trainAlgorithmId;
    /**
     * 训练模型的账户
     */
    private String trainUserAddress;
    /**
     * 状态: 0-无效，1- 有效
     */
    @TableField(value = "`status`")
    private Byte status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

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
     * 模型的元数据id
     */
    @TableField(exist = false)
    private String metadataId;
    /**
     * 模型由哪个算法生成
     */
    @TableField(exist = false)
    private String algorithmName;
}
