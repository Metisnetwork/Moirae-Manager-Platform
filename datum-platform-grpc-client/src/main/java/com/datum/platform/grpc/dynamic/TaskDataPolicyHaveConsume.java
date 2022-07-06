package com.datum.platform.grpc.dynamic;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class TaskDataPolicyHaveConsume {
    private List<Integer> consumeTypes;
    private List<String> consumeOptions;

    public Optional<Consume> getConsume(){
        if(consumeTypes.size() == 0 || consumeOptions.size() == 0 ){
            return Optional.empty();
        }
        if(consumeTypes.get(0) == 2){
            JSONObject jsonObject = JSONObject.parseObject(consumeOptions.get(0));
            Consume consume = new Consume();
            consume.setConsumeType(consumeTypes.get(0));
            consume.setTokenAddress(jsonObject.getString("contract").toLowerCase());
            consume.setBalance(jsonObject.getString("balance"));
            return Optional.of(consume);
        }
        if(consumeTypes.get(0) == 3){
            JSONObject jsonObject = JSONObject.parseObject(consumeOptions.get(0));
            Consume consume = new Consume();
            consume.setConsumeType(consumeTypes.get(0));
            consume.setTokenAddress(jsonObject.getString("contract").toLowerCase());
            consume.setTokenId(jsonObject.getString("takenId"));
            return Optional.of(consume);
        }
        return Optional.empty();
    }

    public void setConsume(Consume consume){
        if(consume.consumeType == 2){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contract", consume.getTokenAddress());
            jsonObject.put("balance", consume.getBalance());
            consumeTypes = new ArrayList<>();
            consumeOptions = new ArrayList<>();
            consumeTypes.add(2);
            consumeOptions.add(jsonObject.toJSONString());
        }
        if(consume.consumeType == 3){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contract", consume.getTokenAddress());
            jsonObject.put("takenId", consume.getTokenId());
            consumeTypes = new ArrayList<>();
            consumeOptions = new ArrayList<>();
            consumeTypes.add(3);
            consumeOptions.add(jsonObject.toJSONString());
        }
    }

    @Data
    public static class Consume {
        Integer consumeType;
        String tokenAddress;
        String tokenId;
        String balance;
    }
}
