package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONPath;
import com.datum.platform.chain.platon.contract.ERC721TemplateContract;
import com.datum.platform.common.constants.SysConfig;
import com.datum.platform.common.utils.CustomHttpClient;
import com.datum.platform.mapper.domain.MetaData;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import com.datum.platform.service.DataService;
import com.platon.tuples.generated.Tuple3;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncMetaDataCertificateTask {

    @Resource
    private DataService dataService;
    @Resource
    private ERC721TemplateContract erc721TemplateContract;
    @Resource
    private SysConfig sysConfig;

    @Scheduled(fixedDelay = 1 * 60 * 1000)
    @Lock(keys = "SyncMetaDataCertificateTask", lockWatchdogTimeout = 5 * 60 * 1000,  attemptTimeout = -1)
    public void run() {
        long begin = DateUtil.current();
        try {
            //查询所有待同步的有属性凭证信息
            List<MetaData> metadataList = dataService.listMetaDataOfNeedSyncedMetaDataCertificate();
            for (MetaData metaData: metadataList) {
                try{
                    sync(metaData);
                }catch (Exception e) {
                    log.error("MetaDataCertificate信息同步, 明细失败：{}", metaData.getMetaDataId(), e);
                }
            }
        } catch (Exception e) {
            log.error("MetaDataCertificate信息同步, 失败原因：{}", e.getMessage(), e);
        }
        log.info("MetaDataCertificate信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(MetaData metadata) {
        List<MetaDataCertificate> insertMetaDataCertificateList = new ArrayList<>();
        List<MetaDataCertificate> updateMetaDataCertificateList = new ArrayList<>();
        Set<String> allToken = new HashSet<>();

        Map<String, MetaDataCertificate> tokenId2MetaDataCertificate = dataService.listMetaDataCertificateByMetaDataId(metadata.getMetaDataId())
                .stream().filter(metaDataCertificate -> metaDataCertificate.getType() == MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES).collect(Collectors.toMap(MetaDataCertificate::getTokenId, me -> me));

        String contractAddress = metadata.getErc721Address();
        BigInteger totalSupply = erc721TemplateContract.totalSupply(contractAddress);
        for (BigInteger i = BigInteger.ZERO; i.compareTo(totalSupply) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger tokenId = erc721TemplateContract.tokenByIndex(contractAddress, i);
            allToken.add(tokenId.toString());
            boolean existInDb = tokenId2MetaDataCertificate.containsKey(tokenId.toString());
            // 数据库中已经存在，但是tokenId对应名称未同步
            if(existInDb && StringUtils.isBlank(tokenId2MetaDataCertificate.get(tokenId.toString()).getName())){
                String tokenName = tokenId2TokenName(contractAddress, tokenId);
                if(StringUtils.isNotBlank(tokenName)){
                    MetaDataCertificate metaDataCertificate = tokenId2MetaDataCertificate.get(tokenId.toString());
                    metaDataCertificate.setName(tokenName);
                    updateMetaDataCertificateList.add(metaDataCertificate);
                }
            }
            // 数据库不存在
            if(!existInDb){
                Tuple3<String, String, Boolean> extInfo = erc721TemplateContract.getExtInfo(contractAddress, tokenId);
                MetaDataCertificate certificate = new MetaDataCertificate();
                certificate.setMetaDataId(metadata.getMetaDataId());
                certificate.setType(MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES);
                certificate.setTokenAddress(contractAddress);
                certificate.setTokenId(tokenId.toString());
                certificate.setIsSupportCtAlg(extInfo.getValue3());
                certificate.setIsSupportPtAlg(!extInfo.getValue3());
                certificate.setCharacteristic(extInfo.getValue2());
                certificate.setName(tokenId2TokenName(contractAddress, tokenId));
                insertMetaDataCertificateList.add(certificate);
            }
        }

        Collection<MetaDataCertificate> dbTokenList = tokenId2MetaDataCertificate.values();
        List<Long> deleteIdList = dbTokenList.stream().filter(item -> !allToken.contains(item.getTokenId())).map(MetaDataCertificate::getId).collect(Collectors.toList());

        dataService.saveOrUpdateOrDeleteBatchMetaDataCertificate(metadata.getMetaDataId(), insertMetaDataCertificateList, updateMetaDataCertificateList, deleteIdList);
    }

    private String tokenId2TokenName(String contractAddress , BigInteger tokenId){
        try {
            String tokenURI = erc721TemplateContract.tokenURI(contractAddress, tokenId);
            if(StringUtils.isNotBlank(tokenURI)){
                Request request = new Request.Builder().url(tokenURI.replace("ipfs://", sysConfig.getIpfsGateway())).build();
                Response response = CustomHttpClient.client.newCall(request).execute();
                if(response.isSuccessful()){
                    String respBody = response.body().string();
                    return JSONPath.extract(respBody, "$.name").toString();
                }
            }
        } catch (Exception e) {
            log.warn("get tokenId name error ! {} ", e.getMessage());
        }
        return null;
    }
}
