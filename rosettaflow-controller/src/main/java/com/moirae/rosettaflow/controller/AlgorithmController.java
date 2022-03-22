package com.moirae.rosettaflow.controller;

import com.moirae.rosettaflow.req.workflow.GetWorkflowVersionListReq;
import com.moirae.rosettaflow.service.IAlgorithmService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.algorithm.AlgTreeVo;
import com.moirae.rosettaflow.vo.workflow.WorkflowVersionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 算法相关接口
 *
 * @author admin
 * @date 2021/8/17
 */
@Slf4j
@RestController
@Api(tags = "算法相关接口")
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmController {

    @Resource
    private IAlgorithmService algorithmService;

    @GetMapping("queryAlgorithmTreeList")
    @ApiOperation(value = "查询算法树列表", notes = "查询算法树列表")
    public ResponseVo<AlgTreeVo> queryAlgorithmTreeList(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        AlgTreeVo algTreeListVo = new AlgTreeVo();
        return ResponseVo.createSuccess(algTreeListVo);
    }







//    @GetMapping("list")
//    @ApiOperation(value = "查询算法列表", notes = "查询算法列表")
//    public ResponseVo<PageVo<AlgorithmListVo>> list(@Valid AlgListReq algListReq) {
//        IPage<AlgorithmDto> algorithmDtoIpage = algorithmService.queryAlgorithmList(algListReq.getCurrent(), algListReq.getSize(), algListReq.getAlgorithmName());
//        return convertAlgorithmDtoToResponseVo(algorithmDtoIpage);
//    }
//
//    @GetMapping("details/{id}")
//    @ApiOperation(value = "查询算法详情", notes = "查询算法详情")
//    public ResponseVo<AlgDetailsVo> detail(@ApiParam(value = "算法表ID", required = true) @PathVariable Long id) {
//        AlgorithmDto algorithmDto = algorithmService.queryAlgorithmDetails(id);
//        AlgDetailsVo algDetailsVo = BeanUtil.copyProperties(algorithmDto, AlgDetailsVo.class);
//        algDetailsVo.setAlgorithmTypeDesc(AlgorithmTypeEnum.getName(algorithmDto.getAlgorithmType()));
//        return ResponseVo.createSuccess(algDetailsVo);
//    }
//
//
//    /**
//     * 查询算法树响应参数转换
//     */
//    @SuppressWarnings("unchecked")
//    private AlgTreeVo convertAlgTreeList(List<Map<String, Object>> listVo) {
//        AlgTreeVo algTreeListVo = new AlgTreeVo();
//        List<AlgTreeItemVo> algTreeVoList = new ArrayList<>();
//        for (Map<String, Object> map : listVo) {
//            AlgTreeItemVo algTreeVo = BeanUtil.toBean(map, AlgTreeItemVo.class);
//            List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("child");
//            if (null == childList || childList.size() == 0) {
//                algTreeVoList.add(algTreeVo);
//                continue;
//            }
//            List<AlgChildTreeVo> algDetailsVoList = new ArrayList<>();
//            childList.forEach(param -> {
//                AlgChildTreeVo algChildTreeVo = BeanUtil.toBean(param, AlgChildTreeVo.class);
//                AlgDetailsVo algDetailsVo = BeanUtil.toBean(param.get("algorithmDto"), AlgDetailsVo.class);
//                algDetailsVo.setAlgorithmId(algChildTreeVo.getAlgorithmId());
//                algChildTreeVo.setAlgDetailsVo(algDetailsVo);
//                algDetailsVoList.add(algChildTreeVo);
//            });
////            algTreeVo.setChild(algDetailsVoList);
//            algTreeVoList.add(algTreeVo);
//        }
////        algTreeListVo.setAlgTreeVoList(algTreeVoList);
//        return algTreeListVo;
//    }
//
//    /**
//     * 转换算法分页列表
//     * @param algorithmDtoIpage 算法分页page
//     * @return 算法列表
//     */
//    private ResponseVo<PageVo<AlgorithmListVo>> convertAlgorithmDtoToResponseVo(IPage<AlgorithmDto> algorithmDtoIpage) {
//        List<AlgorithmListVo> items = new ArrayList<>();
//        algorithmDtoIpage.getRecords().forEach(algorithmDto -> {
//            AlgorithmListVo algorithmListVo = new AlgorithmListVo();
//            BeanCopierUtils.copy(algorithmDto, algorithmListVo);
//            items.add(algorithmListVo);
//        });
//
//        PageVo<AlgorithmListVo> pageVo = new PageVo<>();
//        BeanUtil.copyProperties(algorithmDtoIpage, pageVo);
//        pageVo.setItems(items);
//
//        return ResponseVo.createSuccess(pageVo);
//    }

}
