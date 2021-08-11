# noinspection SqlNoDataSourceInspectionForFile

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID(自增长)',
  `name` varchar(32) NOT NULL COMMENT '用户名',
  `address` varchar(64)  NOT NULL COMMENT '用户钱包地址',
  `identity_id` varchar(64)  DEFAULT NULL COMMENT '组织的身份标识Id',
  `bind_status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '绑定状态: 0-未绑定, 1-已绑定, 2-已拒绝',
  `status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '用户状态: 1-正常, 2-删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for `t_user_data`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_data`;
CREATE TABLE `t_user_data` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户数据表ID(自增长)',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `identity_id` bigint(20) DEFAULT NULL COMMENT '组织的身份标识Id',
    `meta_data_id` bigint(20) DEFAULT NULL COMMENT '元数据id',
    `org_name` varchar(64) DEFAULT NULL COMMENT '组织名称',
    `resource_name` varchar(128) NOT NULL COMMENT '资源名称',
    `authorization_type`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权方式: 1-按时间, 2-按次数',
    `authorization_value` bigint(20) DEFAULT NULL COMMENT '授权值: 按时间单位为（天），按次数单位为（次）',
    `authorize_begin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权开始时间',
    `authorize_end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权结束时间',
    `authorization_status`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权状态: 0-申请中, 1-已授权,2-已拒绝',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据表';

-- ----------------------------
-- Table structure for `t_user_algorithm`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_algorithm`;
CREATE TABLE `t_user_algorithm` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户算法表ID(自增长)',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `identity_id` bigint(20) DEFAULT NULL COMMENT '组织的身份标识Id',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法表id',
    `algorithm_name` varchar(64) DEFAULT NULL COMMENT '算法名称',
    `authorization_type`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权方式: 1-按时间, 2-按次数',
    `authorization_value` bigint(20) DEFAULT NULL COMMENT '授权值: 按时间单位为（天），按次数单位为（次）',
    `authorization_status`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权状态: 0-申请中, 1-已授权,2-已拒绝',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户算法表';

-- ----------------------------
-- Table structure for `t_project`
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目ID(自增长)',
  `name` varchar(128) NOT NULL COMMENT '项目名称',
  `desc`  varchar(512) DEFAULT NULL COMMENT '项目描述',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `nonce` varchar(32)  NOT NULL COMMENT '随机数',
  `status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '项目状态: 1-正常, 2-删除',
  `template_flg`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '是否是项目模版: 1-否, 2-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='项目表';


-- ----------------------------
-- Table structure for `t_project_member`
-- ----------------------------
DROP TABLE IF EXISTS `t_project_member`;
CREATE TABLE `t_project_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目成员ID(自增长)',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role` bigint(20) DEFAULT NULL COMMENT '角色：1-管理员，2-普通用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PROJECT_USER_ID` (`project_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目成员表';


-- ----------------------------
-- Table structure for `t_algorithm`
-- ----------------------------
DROP TABLE IF EXISTS `t_algorithm`;
CREATE TABLE `t_algorithm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法表ID(自增长)',
  `name` varchar(64) DEFAULT NULL COMMENT '算法名称',
  `desc` varchar(512) DEFAULT NULL COMMENT '算法描述',
  `auth` varchar(32) DEFAULT NULL COMMENT '算法作者',
  `max_numbers`  bigint(20) DEFAULT NULL COMMENT '支持协同方最大数量',
  `min_numbers`  bigint(20) DEFAULT NULL COMMENT '支持协同方最小数量',
  `support_language`  varchar(64) DEFAULT NULL COMMENT '支持语言,多个以","进行分隔',
  `support_os_system`  varchar(64) DEFAULT NULL COMMENT '支持操作系统,多个以","进行分隔',
  `type` tinyint(4) DEFAULT NULL COMMENT '算法所属大类:1-统计分析,2-特征工程,3-机器学习',
  `calculate_contract_code` TEXT DEFAULT NULL COMMENT '计算合约',
  `data_split_contract_code` TEXT DEFAULT NULL COMMENT '数据分片合约',
  `cost_mem`  bigint(20) DEFAULT NULL COMMENT '所需的内存 (单位: byte)',
  `cost_processor`  bigint(20) DEFAULT NULL COMMENT '所需的核数 (单位: 个)',
  `cost_gpu`  bigint(20) DEFAULT NULL COMMENT '所需gpu数 (单位: 个)',
  `cost_bandwidth`  bigint(20) DEFAULT NULL COMMENT '所需的带宽 (单位: bps)',
  `duration`  bigint(20) DEFAULT NULL COMMENT '所需的运行时长 (单位: ms)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='算法表';


-- ----------------------------
-- Table structure for `t_algorithm_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_algorithm_user`;
CREATE TABLE `t_algorithm_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法用户关联表ID(自增长)',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '项目id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ALGORITHM_USER_ID` (`algorithm_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='算法用户表';


-- ----------------------------
-- Table structure for `t_algorithm_variable`
-- ----------------------------
DROP TABLE IF EXISTS `t_algorithm_variable`;
CREATE TABLE `t_algorithm_variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法变量表ID(自增长)',
  `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法表id',
  `name` varchar(128) NOT NULL COMMENT '变量名称',
  `variable` varchar(128) NOT NULL COMMENT '变量值',
  `type`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '变量类型: 1-自变量, 2-因变量',
  `desc`  varchar(512) DEFAULT NULL COMMENT '变量描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='算法变量表';

-- ----------------------------
-- Table structure for `t_workflow`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow`;
CREATE TABLE `t_workflow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目工作流ID(自增长)',
  `name` varchar(64) DEFAULT NULL COMMENT '工作流名称',
  `desc`  varchar(128) DEFAULT NULL COMMENT '工作流描述',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建方id',
  `node_number` bigint(20) DEFAULT NULL COMMENT '节点数',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态:1-正常，2-删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流表';


-- ----------------------------
-- Table structure for `t_workflow_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node`;
CREATE TABLE `t_workflow_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点ID(自增长)',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `step` tinyint(4) DEFAULT NULL COMMENT '节点在工作流中序号,从1开始',
  `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点表';


-- ----------------------------
-- Table structure for `t_workflow_node_input`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node_input`;
CREATE TABLE `t_workflow_node_input` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点ID(自增长)',
  `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
  `org_name` varchar(64) DEFAULT NULL COMMENT '组织名称',
  `party_id` varchar(64) DEFAULT NULL COMMENT '任务里面定义的 (p0 -> pN 方 ...)',
  `node_id` varchar(64) DEFAULT NULL COMMENT '组织中调度服务的 nodeId',
  `identity_id` varchar(64) DEFAULT NULL COMMENT '组织的身份标识Id',
  `meta_data_id` varchar(64) DEFAULT NULL COMMENT '元数据id',
  `column_index_list` tinyint(4) NOT NULL COMMENT '该任务使用原始数据的第几列',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点数据提供方列表';


-- ----------------------------
-- Table structure for `t_workflow_node_output`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node_output`;
CREATE TABLE `t_workflow_node_output` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点输出表ID(自增长)',
  `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
  `org_name` varchar(64) DEFAULT NULL COMMENT '组织名称',
  `party_id` varchar(64) DEFAULT NULL COMMENT '任务里面定义的 (p0 -> pN 方 ...)',
  `node_id` varchar(64) DEFAULT NULL COMMENT '组织中调度服务的 nodeId',
  `identity_id` varchar(64) DEFAULT NULL COMMENT '组织的身份标识Id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目工作流节点输出表';

-- ----------------------------
-- Table structure for `t_workflow_node_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node_resource`;
CREATE TABLE `t_workflow_node_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '节点资源表ID(自增长)',
  `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
  `cpu` tinyint(4) DEFAULT NULL COMMENT 'cpu核数（单位：核）',
  `cost_mem` int(11) DEFAULT NULL COMMENT '内存大小（G）',
  `gpu` int(11) DEFAULT NULL COMMENT 'GPU大小（G）',
  `bandwidth` int(11) DEFAULT NULL COMMENT '带宽（M）',
  `duration` bigint(20) DEFAULT NULL COMMENT '所需的运行时长 (单位: ms)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点资源表';

-- ----------------------------
-- Table structure for `t_workflow_instance`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_instance`;
CREATE TABLE `t_workflow_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务表ID(自增长)',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `name` varchar(64) DEFAULT NULL COMMENT '任务名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '作业状态:0-未开始,1-运行中,2-运行成功,3-运行失败',
  `current_step` tinyint(4) DEFAULT NULL COMMENT '当前运行节点序号:0-未启动',
  `dist_step` tinyint(4) DEFAULT NULL COMMENT '即将到运行节点序号',
  `last_step` tinyint(4) DEFAULT NULL COMMENT '最后一个运行节点序号:默认工作流最后一个节点',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流实例表';

-- ----------------------------
-- Table structure for `t_task`
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务表ID(自增长)',
  `task_name` varchar(32) DEFAULT NULL COMMENT '任务名称',
  `task_no` varchar(32) DEFAULT NULL COMMENT 'RosettaNet任务处理Id',
  `job_id` bigint(20) DEFAULT NULL COMMENT '工作流实例表id',
  `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态:0-未开始,1-运行中,2-运行成功,3-运行失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表,对应工作流中的一个节点';


-- ----------------------------
-- Table structure for `t_scheduled`
-- ----------------------------
DROP TABLE IF EXISTS `t_scheduled`;
CREATE TABLE `t_scheduled` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务计划表ID(自增长)',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `name` varchar(64) DEFAULT NULL COMMENT '作业名称',
  `desc` varchar(64) DEFAULT NULL COMMENT '作业描述',
  `switch` tinyint(4) NOT NULL DEFAULT 0 COMMENT '调度开关：0-关,1-开',
  `repeat_flag` tinyint(4) DEFAULT NULL DEFAULT 0 COMMENT '是否重复：0-否,1-是',
  `repeat_interval` bigint(20) DEFAULT NULL COMMENT '重复间隔，单位分钟',
  `begin_time` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务计划表';
