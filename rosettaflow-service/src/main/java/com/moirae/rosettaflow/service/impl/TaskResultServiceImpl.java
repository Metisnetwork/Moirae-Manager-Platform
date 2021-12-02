package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.protobuf.ByteString;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcDataProviderService;
import com.moirae.rosettaflow.grpc.service.TaskStatus;
import com.moirae.rosettaflow.mapper.TaskResultMapper;
import com.moirae.rosettaflow.mapper.domain.TaskResult;
import com.moirae.rosettaflow.service.ITaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hudenian
 * @date 2021/10/14
 */
@Slf4j
@Service
public class TaskResultServiceImpl extends ServiceImpl<TaskResultMapper, TaskResult> implements ITaskResultService {

    @Resource
    private GrpcDataProviderService grpcDataProviderService;

    @Override
    public List<TaskResult> queryTaskResultByTaskId(String taskId) {
        LambdaQueryWrapper<TaskResult> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TaskResult::getTaskId, taskId);
        queryWrapper.eq(TaskResult::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }

    @Override
    public void batchInsert(List<TaskResult> taskResultList) {
        this.baseMapper.batchInsert(taskResultList);
    }

    @Override
    public DownloadReplyResponseDto downloadTaskResultFile(int id, int compressType) {
        //1.查询TaskResult
        LambdaQueryWrapper<TaskResult> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TaskResult::getId, id);
        queryWrapper.eq(TaskResult::getStatus, StatusEnum.VALID.getValue());
        TaskResult taskResult = this.getOne(queryWrapper);
        if (Objects.isNull(taskResult)) {
            log.debug("Download taskResult file failed, id:{} ", id);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.TASK_RESULT_ID_NOT_EXIST.getMsg());
        }
        //2.组装下载request
        if (compressType != TaskDownloadCompressEnum.ZIP.getValue() && compressType != TaskDownloadCompressEnum.TAR_GZ.getValue()) {
            log.debug("Download taskResult file failed, compressType error, compressType:{} ", compressType);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_COMPRESSTYPE_ERROR.getMsg());
        }
        Map<String, String> compressMap = new HashMap<>(2);
        compressMap.put("compress", Objects.requireNonNull(TaskDownloadCompressEnum.getByValue(compressType)).getCompressType());
        DownloadRequestDto downloadRequestDto = new DownloadRequestDto();
        //todo 下载文件路径filePath,rpc返回的是文件夹可能存在问题，待后续处理
        BeanCopierUtils.copy(taskResult, downloadRequestDto);
        //downloadRequestDto.setFilePath("/home/user1/fighter/data30002/银行预测小数据集_20211126-062130.csv");
        downloadRequestDto.setCompress(compressMap);
        //3.调用rpc下载
        AtomicReference<ByteString> byteString = new AtomicReference<>(ByteString.EMPTY);
        CountDownLatch count = new CountDownLatch(1);
        DownloadReplyResponseDto responseDto = new DownloadReplyResponseDto();
        grpcDataProviderService.downloadTask(downloadRequestDto, downloadReply ->{
            boolean hasContent = downloadReply.hasContent();
            if(hasContent){
                ByteString content = downloadReply.getContent();
                ByteString bs = byteString.get().concat(content);
                byteString.set(bs);
            }
            boolean hasStatus = downloadReply.hasStatus();
            if(hasStatus){
                TaskStatus status = downloadReply.getStatus();
                responseDto.setDownloadStatus(status.getNumber());
                //下载状态： Start = 0、 Finished = 1、 Cancelled = 2、 Failed = 3
                switch (status.getNumber()){
                    case 0:
                        log.debug("Download taskResult file start, filePath:{}, status:{}, id:{}.......",downloadRequestDto.getFilePath(), status.getNumber(), id);
                        break;
                    case 1:
                        log.debug("Download taskResult file finished, filePath:{}, status:{}, id:{}.......",downloadRequestDto.getFilePath(), status.getNumber(), id);
                        count.countDown();
                        break;
                    case 2:
                    case 3:
                        log.debug("Download taskResult file failed, filePath:{}, status:{}, id:{}.......",downloadRequestDto.getFilePath(), status.getNumber(), id);
                        count.countDown();
                        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_FAIL.getMsg());
                    default:
                        break;
                }
            }
        });
        try {
            //4.倒计时锁，等待服务端响应返回数据
            boolean await = count.await(60, TimeUnit.SECONDS);
            if (!await) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_TIMEOUT.getMsg());
            }
        } catch (InterruptedException e) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, e.getMessage());
        }
        responseDto.setFileName(taskResult.getFileName());
        responseDto.setContent(byteString.get().toByteArray());
        return responseDto;
    }
}
