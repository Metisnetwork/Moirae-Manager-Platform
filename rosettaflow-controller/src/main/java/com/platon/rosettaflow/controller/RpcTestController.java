package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.MetaDataDetailResponseDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import com.platon.rosettaflow.grpc.service.GrpcMetaDataService;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDetailResponseDto;
import com.platon.rosettaflow.req.user.GrpcReq;
import com.platon.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @PostMapping("auth")
    @ApiOperation(value = "grpc授权接口测试", notes = "grpc授权接口测试")
    public ResponseVo<String> testGrpc(@RequestBody @Valid GrpcReq grpcReq) {
        log.info("grpc授权接口测试");
        NodeIdentityDto nodeIdentityDto = grpcAuthService.getNodeIdentity();
        return ResponseVo.createSuccess(nodeIdentityDto.getNodeName());
    }

    @PostMapping("metadata")
    @ApiOperation(value = "grpc metadata接口测试", notes = "grpc metadata接口测试")
    public ResponseVo<List<MetaDataDetailResponseDto>> metadata(@RequestBody @Valid GrpcReq grpcReq) {
        log.info("metadata接口测试");
        List<MetaDataDetailResponseDto> metaDataDetailList = grpcMetaDataService.getMetaDataDetailList();
        return ResponseVo.createSuccess(metaDataDetailList);
    }

    @PostMapping("task")
    @ApiOperation(value = "grpc task接口测试", notes = "grpc task接口测试")
    public ResponseVo<List<TaskDetailResponseDto>> task(@RequestBody @Valid GrpcReq grpcReq) {
        log.info("metadata接口测试");
        List<TaskDetailResponseDto> taskDetailList = grpcTaskService.getTaskDetailList();
        return ResponseVo.createSuccess(taskDetailList);
    }
}
