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
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


//    @Scheduled(fixedDelay = 1 * 60 * 1000)
    @Lock(keys = "SyncMetaDataCertificateTask")
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
        List<MetaDataCertificate> metaDataCertificateList = new ArrayList<>();
        String contractAddress = metadata.getErc721Address();
        BigInteger totalSupply = erc721TemplateContract.totalSupply(contractAddress);
        for (BigInteger i = BigInteger.ZERO; i.compareTo(totalSupply) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger tokenId = erc721TemplateContract.tokenByIndex(contractAddress, i);
            Tuple3<String, String, Boolean> extInfo = erc721TemplateContract.getExtInfo(contractAddress, tokenId);
            MetaDataCertificate certificate = new MetaDataCertificate();
            certificate.setMetaDataId(metadata.getMetaDataId());
            certificate.setType(MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES);
            certificate.setTokenAddress(contractAddress);
            certificate.setTokenId(tokenId.toString());
            certificate.setIsSupportCtAlg(extInfo.getValue3());
            certificate.setIsSupportPtAlg(!extInfo.getValue3());
            certificate.setCharacteristic(extInfo.getValue2());
            try {
                String tokenURI = erc721TemplateContract.tokenURI(contractAddress, tokenId);
                if(StringUtils.isNotBlank(tokenURI)){
                    Request request = new Request.Builder().url(tokenURI.replace("ipfs://", sysConfig.getIpfsGateway())).build();
                    Response response = CustomHttpClient.client.newCall(request).execute();
                    if(response.isSuccessful()){
                        Optional.ofNullable(JSONPath.extract(response.body().string(), "$.name")).ifPresent(item -> certificate.setName(item.toString()));
                    }
                }
            } catch (IOException e) {
                log.warn("get certificate name error ! ", e);
            }
            metaDataCertificateList.add(certificate);
        }

        if(metaDataCertificateList.size() > 0){
            dataService.saveOrUpdateOrDeleteBatchMetaDataCertificate(metadata.getMetaDataId(), metaDataCertificateList);
        }
    }
}
