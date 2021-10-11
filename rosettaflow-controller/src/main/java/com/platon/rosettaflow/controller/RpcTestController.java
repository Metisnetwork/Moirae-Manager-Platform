package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataUsageRuleDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import com.platon.rosettaflow.grpc.service.GrpcMetaDataService;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.platon.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.service.IWorkflowService;
import com.platon.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@RestController
@Api(tags = "gRPC测试接口")
@RequestMapping(value = "testGrpc", produces = MediaType.APPLICATION_JSON_VALUE)
public class RpcTestController {

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private GrpcMetaDataService grpcMetaDataService;

    @Resource
    private GrpcTaskService grpcTaskService;

    @Resource
    private IWorkflowService workflowService;

    @GetMapping("getNodeIdentity")
    @ApiOperation(value = "getNodeIdentity接口测试", notes = "查询自己组织的identity信息")
    public ResponseVo<NodeIdentityDto> getNodeIdentity() {
        log.info("getNodeIdentity接口测试");
        NodeIdentityDto nodeIdentityDto = grpcAuthService.getNodeIdentity();
        return ResponseVo.createSuccess(nodeIdentityDto);
    }

    @GetMapping("getIdentityList")
    @ApiOperation(value = "GetIdentityList接口测试", notes = "查询全网组织的身份信息列表(已入网的)")
    public ResponseVo<List<NodeIdentityDto>> getIdentityList() {
        log.info("GetIdentityList接口测试");
        List<NodeIdentityDto> nodeIdentityDtoList = grpcAuthService.getIdentityList();
        return ResponseVo.createSuccess(nodeIdentityDtoList.size() > 10 ? nodeIdentityDtoList.subList(0, 10) : nodeIdentityDtoList);
    }

    @GetMapping("getGlobalMetadataDetailList")
    @ApiOperation(value = "grpc getTotalMetadataDetailList查看全网元数据列表", notes = "grpc getTotalMetadataDetailList查看全网元数据列表")
    public ResponseVo<List<MetaDataDetailResponseDto>> getGlobalMetadataDetailList() {
        log.info("grpc getTotalMetadataDetailList查看全网元数据列表");
        List<MetaDataDetailResponseDto> metaDataDetailList = grpcMetaDataService.getGlobalMetadataDetailList();
        return ResponseVo.createSuccess(metaDataDetailList.size() > 10 ? metaDataDetailList.subList(0, 8) : metaDataDetailList);
    }

    @GetMapping("getMetadataAuthorityList")
    @ApiOperation(value = "grpc GetMetadataAuthorityList当前(组织)的所有元数据的授权申请及审核结果详情列表", notes = "grpc GetMetadataAuthorityList当前(组织)的所有元数据的授权申请及审核结果详情列表")
    public ResponseVo<List<GetMetaDataAuthorityDto>> getMetadataAuthorityList() {
        log.info("grpc GetMetadataAuthorityList当前(组织)的所有元数据的授权申请及审核结果详情列表");
        List<GetMetaDataAuthorityDto> metaDataAuthorityDtoList = grpcAuthService.getGlobalMetadataAuthorityList();
        return ResponseVo.createSuccess(metaDataAuthorityDtoList.size() > 10 ? metaDataAuthorityDtoList.subList(0, 8) : metaDataAuthorityDtoList);
    }

    @PostMapping("applyMetadataAuthority")
    @ApiOperation(value = "grpc applyMetadataAuthority发起数据授权申请接口测试", notes = "grpc applyMetadataAuthority发起数据授权申请接口测试")
    public ResponseVo<ApplyMetaDataAuthorityResponseDto> applyMetadataAuthority() {
        log.info("grpc applyMetadataAuthority发起数据授权申请接口测试");
        ApplyMetaDataAuthorityRequestDto requestDto = new ApplyMetaDataAuthorityRequestDto();
        requestDto.setUser("lat1krh2rm747g2j0z6zp5sup06u6ez34fx8y8vd25");
        requestDto.setUserType(2);

        MetaDataAuthorityDto auth = new MetaDataAuthorityDto();

        NodeIdentityDto owner = new NodeIdentityDto();
        owner.setNodeName("nodeName");
        owner.setNodeId("s");
        owner.setIdentityId("identity_23ff17389acbfd020043268fb49e7048");
        owner.setStatus(0);

        auth.setOwner(owner);
        auth.setMetaDataId("metaDataId");
        MetaDataUsageRuleDto metaDataUsageDto = new MetaDataUsageRuleDto();
        metaDataUsageDto.setUseType(2);
        metaDataUsageDto.setStartAt(1355555555555L);
        metaDataUsageDto.setEndAt(1955555555555L);
        metaDataUsageDto.setTimes(100);
        auth.setMetaDataUsageDto(metaDataUsageDto);

        requestDto.setAuth(auth);
        requestDto.setSign("sign now unknown");

        ApplyMetaDataAuthorityResponseDto applyMetaDataAuthorityResponseDto = grpcAuthService.applyMetaDataAuthority(requestDto);
        return ResponseVo.createSuccess(applyMetaDataAuthorityResponseDto);
    }


    @GetMapping("task/getTaskDetailList")
    @ApiOperation(value = "grpc GetTaskDetailList查看本组织参与过的全部任务详情列表", notes = "grpc GetTaskDetailList查看本组织参与过的全部任务详情列表")
    public ResponseVo<List<TaskDetailResponseDto>> getTaskDetailList() {
        log.info("grpc GetTaskDetailList查看本组织参与过的全部任务详情列表");
        List<TaskDetailResponseDto> metaDataAuthorityDtoList = grpcTaskService.getTaskDetailList();
        return ResponseVo.createSuccess(metaDataAuthorityDtoList);
    }


    @GetMapping("task/getTaskEventListById{taskId}")
    @ApiOperation(value = "grpc getTaskEventListById查询任务事件根据任务id", notes = "grpc getTaskEventListById查询任务事件根据任务id")
    public ResponseVo<List<TaskEventDto>> getTaskEventListById(@ApiParam(value = "taskId", required = true) @PathVariable String taskId) {
        log.info("grpc getTaskEventListById查询任务事件根据任务id");
        List<TaskEventDto> taskEventDtoList = grpcTaskService.getTaskEventList(taskId);
        return ResponseVo.createSuccess(taskEventDtoList);
    }


    @GetMapping("task/{workflowId}")
    @ApiOperation(value = "grpc task接口测试", notes = "grpc task接口测试")
    public ResponseVo<String> task(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long workflowId) {
        log.info("grpc task接口测试");
        Workflow orgWorkflow = workflowService.getById(workflowId);
        TaskDto taskDto = workflowService.assemblyTaskDto(orgWorkflow.getId(), 1, "0x501eb3eeb2a40e6f2ff6f481302435e6e8af3666", "sign");
        PublishTaskDeclareResponseDto publishTaskDeclareResponseDto = grpcTaskService.syncPublishTask(taskDto);
        return ResponseVo.createSuccess(publishTaskDeclareResponseDto.getMsg());
    }
}
