package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import com.datum.platform.mapper.enums.MetaDataStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "dc_meta_data")
public class MetaData implements Serializable {
    /**
     * 元数据ID,hash
     */
    @TableId
    private String metaDataId;

    /**
     * 数据文件ID,hash
     */
    private String originId;

    /**
     * 组织身份ID
     */
    private String identityId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 文件后缀/类型, 0:未知; 1:csv
     */
    private MetaDataFileTypeEnum fileType;

    /**
     * 行业名称
     */
    private String industry;

    /**
     * 文件大小(字节)
     */
    private Long size;

    /**
     * 数据行数(不算title)
     */
    @TableField(value = "`rows`")
    private Integer rows;

    /**
     * 数据列数
     */
    @TableField(value = "`columns`")
    private Integer columns;

    /**
     * 发布时间，精确到毫秒
     */
    private Date publishedAt;

    /**
     * 是否带标题
     */
    private Boolean hasTitle;

    /**
     * 数据描述
     */
    private String remarks;

    /**
     * 元数据的状态 (0: 未知; 1: 还未发布的新表; 2: 已发布的表; 3: 已撤销的表)
     */
    private MetaDataStatusEnum status;

    /**
     * (状态)修改时间
     */
    private Date updateAt;

    /**
     * 表示该元数据是 `普通数据` 还是 `模型数据` 的元数据 (0: 未定义; 1: 普通数据元数据; 2: 模型数据元数据)
     */
    private Integer metaDataType;

    /**
     * 源数据的存储位置类型 (组织本地服务器、远端服务器、云等)：0-未知，1-存储在组织本地服务器上，2-存储在远端服务器上
     */
    private Integer locationType;

    /**
     * 元数据的 nonce (用来标识该元数据在所属组织中的元数据的序号, 从 0 开始递增)
     */
    private Long nonce;

    /**
     * 是否可以被曝光 (1: 可以; 0: 不可以; 如 数据原始内容可以被下载或者支持外域查看时则为1, 默认为0)
     */
    private Boolean allowExpose;

    /**
     * 是否支持明文算法
     */
    private Boolean isSupportPtAlg;

    /**
     * 是否支持密文算法
     */
    private Boolean isSupportCtAlg;

    /**
     * 数据拥有者地址
     */
    private String ownerAddress;

    /**
     * 数据对应erc20合约地址
     */
    private String erc20Address;

    /**
     * 数据对应erc721合约地址
     */
    private String erc721Address;

    /**
     * 明文算法消耗量
     */
    private String erc20PtAlgConsume;

    /**
     * 密文算法消耗量
     */
    private String erc20CtAlg_consume;

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
    private String metaDataName;
    @TableField(exist = false)
    private String nodeName;
    @TableField(exist = false)
    private String tokenName;
    @TableField(exist = false)
    private String tokenPrice;
    @TableField(exist = false)
    private String tokenSymbol;
    @TableField(exist = false)
    private String tokenBalance;
    @TableField(exist = false)
    private Long tokenDecimal;
    @TableField(exist = false)
    private List<MetaDataColumn> columnsList;
    @TableField(exist = false)
    private String authorizeBalance;
    @TableField(exist = false)
    private Long totalSize;
    @TableField(exist = false)
    private Integer totalCount;
}
