package com.datum.platform.grpc.dynamic;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liushuyu
 * @date 2022/10/14 11:05
 * @desc <pre>
 *     [
 *   {
 *     "origin": "t2",
 *     "reference": [
 *       {
 *         "target": "t1",
 *         "dependPartyId": [
 *           "result1",
 *           "result2"
 *         ],
 *         "merge": false,
 *         "dependParamsType":[30001,30001]
 *         "dependParams": [
 *           "{\"inputType\": 1, \"keyColumnName\": \"\", \"selectedColumnNames\": []}",
 *           "{\"inputType\": 1, \"keyColumnName\": \"\", \"selectedColumnNames\": []}"
 *         ]
 *       }
 *     ]
 *   },
 *   {
 *     "origin": "t3",
 *     "reference": [
 *       {
 *         "target": "t2",
 *         "dependPartyId": [
 *           "result1",
 *           "result2"
 *         ],
 *         "merge": false,
 *         "dependParamsType":[2]
 *         "dependParams": [
 *           "{\"inputType\": 3}"
 *         ]
 *       }
 *     ]
 *   }
 * ]
 * </pre>
 */

@Data
public class WorkflowPolicyOrdinary {


    private List<OriginTask> originTaskList = new ArrayList<>();

    @Data
    public static class OriginTask {
        /**
         * 任务(值为task_name)
         */
        private String origin;

        /**
         * 依赖任务数组
         */
        private List<ReferenceTask> reference = new ArrayList<>();
    }

    @Data
    public static class ReferenceTask {

        /**
         * 具体依赖的某个任务
         */
        private String target;

        /**
         * 表示要使用依赖任务的那些结果
         */
        private List<String> dependPartyId = new ArrayList<>();

        /**
         * 表示如果dependPartyId依赖的结果大于1个时，是否需要合并
         */
        private boolean merge = false;

        /**
         * 	dependParams参数的类型
         */
        private List<Integer> dependParamsType = new ArrayList<>();

        /**
         * 后继任务依赖前置任务的参数
         */
        private List<String> dependParams = new ArrayList<>();
    }
}
