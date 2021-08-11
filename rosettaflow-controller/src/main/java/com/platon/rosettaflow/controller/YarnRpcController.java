package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.req.user.GrpcReq;
import com.platon.rosettaflow.service.IAuthServiceRpc;
import com.platon.rosettaflow.service.IYarnServiceRpc;
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

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@RestController
@Api(tags = "gRPC测试接口")
@RequestMapping(value = "grpc", produces = MediaType.APPLICATION_JSON_VALUE)
public class YarnRpcController {

    @Resource
    private IYarnServiceRpc yarnServiceRpc;

    @Resource
    private IAuthServiceRpc authServiceRpc;

    @PostMapping("test")
    @ApiOperation(value = "grpc测试", notes = "grpc测试")
    public ResponseVo<String> testGrpc(@RequestBody @Valid GrpcReq grpcReq) {
        log.info("grpc测试");
        String result = yarnServiceRpc.getNodeInfo();

//        String ower = authServiceRpc.getOwner();
        return ResponseVo.createSuccess(result);
    }
}
