package com.datum.platform.grpc.dynamic;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class MetadataOptionCsv {
    //原始数据Id
    private String originId;
    //当前数据的完整路径
    private String dataPath;
    //数据的大小 (单位: byte)
    private Integer size;
    //数据的行数
    private Integer rows;
    //数据的列数
    private Integer columns;
    //表示数据是否含有标题行, true: 含有, false: 没有
    private Boolean hasTitle;
    //数据的列信息
    private List<MetadataColumn> metadataColumns;
    //支持的消费选项汇总信息
    private List<String> consumeOptions;
    //支持的消费对象选项汇总信息
    public Attribute getAttributeInfo(){
        Attribute attribute = new Attribute();
        if(CollectionUtil.isEmpty(consumeOptions)){
            return attribute;
        }
        for (String consumeOption: consumeOptions) {
            JSONObject jsonObject = JSONObject.parseObject(consumeOption);
            if(jsonObject.getIntValue("type") == 2){
                NoAttribute noAttribute = new NoAttribute();
                noAttribute.setContract(jsonObject.getJSONArray("information").getJSONObject(0).getString("contract").toLowerCase());
                noAttribute.setCryptoAlgoConsumeUnit(jsonObject.getJSONArray("information").getJSONObject(0).getString("cryptoAlgoConsumeUnit"));
                noAttribute.setPlainAlgoConsumeUnit(jsonObject.getJSONArray("information").getJSONObject(0).getString("plainAlgoConsumeUnit"));
                attribute.setNoAttribute(Optional.of(noAttribute));
            }
            if(jsonObject.getIntValue("type") == 3){
                attribute.setHaveAttribute(Optional.ofNullable(jsonObject.getJSONArray("information").getString(0).toLowerCase()));
            }
        }
        return attribute;
    }


    @Data
    public static class MetadataColumn {
        //列索引 (从 1 下标开始)
        Integer index;
        //列名
        String name;
        //列类型, (string/ bool/ float/ int)
        String type;
        //列大小 (单位: byte)
        Integer size;
        //列描述
        String comment;
    }

    @Data
    public static class Attribute {
        Optional<String> haveAttribute = Optional.empty();
        Optional<NoAttribute> noAttribute = Optional.empty();
    }

    @Data
    public static class NoAttribute {
        //合约地址
        String contract;
        //用于密文算法的定价单位 (token个数)
        String cryptoAlgoConsumeUnit;
        //用于明文算法的定价单位 (token个数)
        String plainAlgoConsumeUnit;
    }
}
