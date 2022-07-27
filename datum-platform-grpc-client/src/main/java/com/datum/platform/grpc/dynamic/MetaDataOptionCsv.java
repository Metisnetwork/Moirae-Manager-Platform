package com.datum.platform.grpc.dynamic;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.grpc.enums.MetaDataCunsumeTypesEnum;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class MetaDataOptionCsv {
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
    //表示数据的一些使用条件,以32bit位设置值, 各个bit上的值可以并存查看
    private Integer condition;
    //数据的列信息
    private List<MetadataColumn> metadataColumns;
    //支持的消费类型
    private List<Integer> consumeTypes;
    //支持的消费选项汇总信息
    private List<String> consumeOptions;
    //支持的消费对象选项汇总信息
    public Attribute getAttributeInfo(){
        Attribute attribute = new Attribute();
        if(CollectionUtil.isEmpty(consumeTypes) || CollectionUtil.isEmpty(consumeOptions)){
            return attribute;
        }
        for (int i = 0; i < consumeTypes.size(); i++) {
            Integer consumeType = consumeTypes.get(i);
            if(consumeType == MetaDataCunsumeTypesEnum.TYPES_2.getValue()){
                JSONObject jsonObject = JSONObject.parseArray(consumeOptions.get(i)).getJSONObject(0);
                NoAttribute noAttribute = new NoAttribute();
                noAttribute.setContract(jsonObject.getString("contract").toLowerCase());
                noAttribute.setCryptoAlgoConsumeUnit(jsonObject.getString("cryptoAlgoConsumeUnit"));
                noAttribute.setPlainAlgoConsumeUnit(jsonObject.getString("plainAlgoConsumeUnit"));
                attribute.setNoAttribute(Optional.of(noAttribute));
            }

            if(consumeType == MetaDataCunsumeTypesEnum.TYPES_3.getValue()){
                String jsonObject = JSONObject.parseArray(consumeOptions.get(i)).getString(0);
                attribute.setHaveAttribute(Optional.ofNullable(jsonObject.toLowerCase()));
            }
        }
        return attribute;
    }

    public boolean isSupportPtAlg(){
        return (condition & 0x00000001) == 1;
    }

    public boolean isSupportCtAlg(){
        return (condition >> 1 & 0x00000001) == 1;
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
