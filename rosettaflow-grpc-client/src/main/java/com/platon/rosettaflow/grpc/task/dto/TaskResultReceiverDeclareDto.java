package com.platon.rosettaflow.grpc.task.dto;

import com.platon.rosettaflow.grpc.service.CommonMessage;
import lombok.Data;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/4
 * @description 任务结果接受者
 */
@Data
public class TaskResultReceiverDeclareDto {
    /**
     * 结果接收方身份信息
     */
    private TaskOrganizationIdentityInfoDto memberInfo;
    /**
     * 被接收结果的生成方身份信息
     */
    private List<TaskOrganizationIdentityInfoDto> providerList;
}
