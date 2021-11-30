package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_meta_data
 *
 * @author admin
 */
@Data
@TableName(value = "t_meta_data")
public class MetaData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 元数据表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 资源所属组织的身份标识Id
     */
    private String identityId;
    /**
     * 资源所属组织名称
     */
    private String identityName;
    /**
     * 资源所属组织中调度服务的 nodeId
     */
    private String nodeId;
    /**
     * 元数据id
     */
    private String metaDataId;
    /**
     * 源文件ID
     */
    private String fileId;
    /**
     * 元数据名称|数据名称 (表名)
     */
    private String dataName;
    /**
     * 元数据的描述 (摘要)
     */
    private String dataDesc;
    /**
     * 源文件存放路径
     */
    private String filePath;
    /**
     * 源文件的行数
     */
    private Integer rows;
    /**
     * 源文件的列数
     */
    private Integer columns;
    /**
     * 源文件的大小 (单位: byte)
     */
    private Long size;
    /**
     * 源文件类型: 0-未知，1- CSV类型
     */
    private Byte fileType;
    /**
     * 是否带标题,0表示不带，1表示带标题
     */
    private Byte hasTitle;
    /**
     * 元数据所属行业
     */
    private String industry;
    /**
     * 元数据的状态 (1- 还未发布的新表; 2- 已发布的表; 3- 已撤销的表)
     */
    private Byte dataStatus;
    /**
     * 状态: 0-无效，1- 有效
     */
    @TableField(value = "`status`")
    private Byte status;
    /**
     * 元数据发布时间
     */
    private Date publishAt;
    /**
     * 元数据更新时间
     */
    private Date updateAt;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

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
        MetaData other = (MetaData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getIdentityName() == null ? other.getIdentityName() == null : this.getIdentityName().equals(other.getIdentityName()))
                && (this.getNodeId() == null ? other.getNodeId() == null : this.getNodeId().equals(other.getNodeId()))
                && (this.getMetaDataId() == null ? other.getMetaDataId() == null : this.getMetaDataId().equals(other.getMetaDataId()))
                && (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
                && (this.getDataName() == null ? other.getDataName() == null : this.getDataName().equals(other.getDataName()))
                && (this.getDataDesc() == null ? other.getDataDesc() == null : this.getDataDesc().equals(other.getDataDesc()))
                && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
                && (this.getRows() == null ? other.getRows() == null : this.getRows().equals(other.getRows()))
                && (this.getColumns() == null ? other.getColumns() == null : this.getColumns().equals(other.getColumns()))
                && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
                && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
                && (this.getHasTitle() == null ? other.getHasTitle() == null : this.getHasTitle().equals(other.getHasTitle()))
                && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
                && (this.getDataStatus() == null ? other.getDataStatus() == null : this.getDataStatus().equals(other.getDataStatus()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        result = prime * result + ((getIdentityName() == null) ? 0 : getIdentityName().hashCode());
        result = prime * result + ((getNodeId() == null) ? 0 : getNodeId().hashCode());
        result = prime * result + ((getMetaDataId() == null) ? 0 : getMetaDataId().hashCode());
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getDataName() == null) ? 0 : getDataName().hashCode());
        result = prime * result + ((getDataDesc() == null) ? 0 : getDataDesc().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getRows() == null) ? 0 : getRows().hashCode());
        result = prime * result + ((getColumns() == null) ? 0 : getColumns().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getHasTitle() == null) ? 0 : getHasTitle().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getDataStatus() == null) ? 0 : getDataStatus().hashCode());
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
                ", identityId=" + identityId +
                ", identityName=" + identityName +
                ", nodeId=" + nodeId +
                ", metaDataId=" + metaDataId +
                ", fileId=" + fileId +
                ", dataName=" + dataName +
                ", dataDesc=" + dataDesc +
                ", filePath=" + filePath +
                ", rows=" + rows +
                ", columns=" + columns +
                ", size=" + size +
                ", fileType=" + fileType +
                ", hasTitle=" + hasTitle +
                ", industry=" + industry +
                ", dataStatus=" + dataStatus +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}