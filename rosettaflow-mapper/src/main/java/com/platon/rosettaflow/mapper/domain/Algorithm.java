package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_algorithm
 * @author admin
 */
@Data
public class Algorithm implements Serializable {
    /**
     * 算法库表ID(自增长)
     */
    @ApiModelProperty(value="算法库表ID(自增长)")
    private Long id;

    /**
     * 算法名称
     */
    @ApiModelProperty(value="算法名称")
    private String name;

    /**
     * 算法描述
     */
    @ApiModelProperty(value="算法描述")
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 算法作者
     */
    @ApiModelProperty(value="算法作者")
    private String auth;

    /**
     * 支持协同方最大数量
     */
    @ApiModelProperty(value="支持协同方最大数量")
    private Integer maxNumbers;

    /**
     * 支持协同方最小数量
     */
    @ApiModelProperty(value="支持协同方最小数量")
    private Integer minNumbers;

    /**
     * 支持语言,多个以","进行分隔
     */
    @ApiModelProperty(value="支持语言,多个以,进行分隔")
    private Integer supportLanguage;

    /**
     * 支持操作系统
     */
    @ApiModelProperty(value="支持操作系统")
    private Integer supportOsSystem;

    /**
     * 算法所属大类:1-统计分析,2-特征工程,3-机器学习
     */
    @ApiModelProperty(value="算法所属大类:1-统计分析,2-特征工程,3-机器学习")
    private Byte type;

    /**
     * 代码路径
     */
    @ApiModelProperty(value="代码路径")
    private String codePath;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
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
        Algorithm other = (Algorithm) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
            && (this.getAuth() == null ? other.getAuth() == null : this.getAuth().equals(other.getAuth()))
            && (this.getMaxNumbers() == null ? other.getMaxNumbers() == null : this.getMaxNumbers().equals(other.getMaxNumbers()))
            && (this.getMinNumbers() == null ? other.getMinNumbers() == null : this.getMinNumbers().equals(other.getMinNumbers()))
            && (this.getSupportLanguage() == null ? other.getSupportLanguage() == null : this.getSupportLanguage().equals(other.getSupportLanguage()))
            && (this.getSupportOsSystem() == null ? other.getSupportOsSystem() == null : this.getSupportOsSystem().equals(other.getSupportOsSystem()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getCodePath() == null ? other.getCodePath() == null : this.getCodePath().equals(other.getCodePath()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getAuth() == null) ? 0 : getAuth().hashCode());
        result = prime * result + ((getMaxNumbers() == null) ? 0 : getMaxNumbers().hashCode());
        result = prime * result + ((getMinNumbers() == null) ? 0 : getMinNumbers().hashCode());
        result = prime * result + ((getSupportLanguage() == null) ? 0 : getSupportLanguage().hashCode());
        result = prime * result + ((getSupportOsSystem() == null) ? 0 : getSupportOsSystem().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCodePath() == null) ? 0 : getCodePath().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", desc=").append(desc);
        sb.append(", auth=").append(auth);
        sb.append(", maxNumbers=").append(maxNumbers);
        sb.append(", minNumbers=").append(minNumbers);
        sb.append(", supportLanguage=").append(supportLanguage);
        sb.append(", supportOsSystem=").append(supportOsSystem);
        sb.append(", type=").append(type);
        sb.append(", codePath=").append(codePath);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}