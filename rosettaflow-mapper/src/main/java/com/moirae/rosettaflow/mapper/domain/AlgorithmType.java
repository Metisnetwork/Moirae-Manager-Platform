package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_algorithm_type
 *
 * @author admin
 */
@Data
@TableName(value = "t_algorithm_type")
public class AlgorithmType implements Serializable {
    /**
     * 算法表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 算法名称
     */
    private String algorithmTypeName;

    /**
     * 算法描述
     */
    private String algorithmTypeDesc;

    /**
     * 算法所属大类:1-统计分析,2-特征工程,3-机器学习
     */
    private Byte algorithmType;

    /**
     * 状态: 0-无效，1-有效
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
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AlgorithmType other = (AlgorithmType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAlgorithmTypeName() == null ? other.getAlgorithmTypeName() == null : this.getAlgorithmTypeName().equals(other.getAlgorithmTypeName()))
                && (this.getAlgorithmTypeDesc() == null ? other.getAlgorithmTypeDesc() == null : this.getAlgorithmTypeDesc().equals(other.getAlgorithmTypeDesc()))
                && (this.getAlgorithmType() == null ? other.getAlgorithmType() == null : this.getAlgorithmType().equals(other.getAlgorithmType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAlgorithmTypeName() == null) ? 0 : getAlgorithmTypeName().hashCode());
        result = prime * result + ((getAlgorithmTypeDesc() == null) ? 0 : getAlgorithmTypeDesc().hashCode());
        result = prime * result + ((getAlgorithmType() == null) ? 0 : getAlgorithmType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", algorithmTypeName=" + algorithmTypeName +
                ", algorithmTypeDesc=" + algorithmTypeDesc +
                ", algorithmType=" + algorithmType +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}