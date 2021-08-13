SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID(自增长)',
  `name` varchar(32) NOT NULL COMMENT '用户名',
  `address` varchar(64)  NOT NULL COMMENT '用户钱包地址',
  `status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '状态: 0-无效，1- 有效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for `t_user_org`
-- 是否需要同步机构待底层联调结果调整
-- ----------------------------
DROP TABLE IF EXISTS `t_user_org`;
CREATE TABLE `t_user_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '机构绑定表ID(自增长)',
  `user_id` bigint(20)  NOT NULL COMMENT '用户ID',
  `identity_id` varchar(64)  DEFAULT NULL COMMENT '组织的身份标识Id',
  `identity_name` varchar(100)  DEFAULT NULL COMMENT '组织的身份名称',
  `bind_status`  tinyint(4)   NOT NULL DEFAULT 0 COMMENT '绑定状态: 0-未绑定, 1-已绑定, 2-已拒绝',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户绑定机构表';

-- ----------------------------
-- Table structure for `t_login_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志表id(自增长)',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `login_status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '登录状态: 0-登录失败, 1-登录成功',
  `login_ip` varchar(32) NOT NULL COMMENT '登录ip',
  `login_browser` varchar(16) NOT NULL COMMENT '浏览器',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录日志表';

-- ----------------------------
-- Table structure for `t_user_data`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_data`;
CREATE TABLE `t_user_data` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户数据表ID(自增长)',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `org_name` varchar(64) DEFAULT NULL COMMENT '资源所属组织名称',
    `node_id` bigint(20) DEFAULT NULL COMMENT '资源所属组织中调度服务的 nodeId',
    `identity_id` bigint(20) DEFAULT NULL COMMENT '资源所属组织的身份标识Id',
    `meta_data_id` bigint(20) DEFAULT NULL COMMENT '元数据id',
    `file_id` varchar(256) DEFAULT NULL COMMENT '源文件ID',
    `table_name` varchar(128) NOT NULL COMMENT '文件名称',
    `desc` varchar(128) NOT NULL COMMENT '元数据的描述 (摘要)',
    `file_path` varchar(128) NOT NULL COMMENT '文件存储路径',
    `rows` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据行数(不算title)',
    `columns` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据列数',
    `size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小(字节)',
    `file_type` varchar(20) NOT NULL COMMENT '文件后缀/类型, csv',
    `has_title` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否带标题,0表示不带，1表示带标题',
    `status` varchar(20) NOT NULL DEFAULT 'created' COMMENT '数据的状态 (created: 还未发布的新表; released: 已发布的表; revoked: 已撤销的表)',
    `authorization_type`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权方式: 1-按时间, 2-按次数, 3-永久',
    `authorization_value` bigint(20) DEFAULT NULL COMMENT '授权值: 按时间单位为（天），按次数单位为（次）',
    `authorize_begin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权开始时间',
    `authorize_end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权结束时间',
    `authorization_status`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权状态: 0-待申请,1-申请中, 2-已授权,3-已拒绝',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据表';

-- ----------------------------
-- Table structure for `t_user_data_details`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_data_details`;
CREATE TABLE `t_user_data_details` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据详情表ID(自增长)',
	`user_data_id` bigint(20) DEFAULT NULL COMMENT '用户数据表id',
    `column_index` bigint(11) DEFAULT NULL COMMENT '列索引',
    `column_name` varchar(32) DEFAULT NULL COMMENT '列名',
    `column_type` varchar(32) DEFAULT NULL COMMENT '列类型',
    `size` bigint(20) DEFAULT '0' COMMENT '列大小（byte）',
    `desc` varchar(32)  DEFAULT NULL COMMENT '列描述',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据详情表';

-- ----------------------------
-- Table structure for `t_project`
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目ID(自增长)',
  `name` varchar(128) NOT NULL COMMENT '项目名称',
  `desc`  varchar(512) DEFAULT NULL COMMENT '项目描述',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '状态: 1-正常, 2-删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PROJECT_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='项目表';

-- ----------------------------
-- Table structure for `t_project_template`
-- ----------------------------
DROP TABLE IF EXISTS `t_project_template`;
CREATE TABLE `t_project_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目模板表ID(自增长)',
  `name` varchar(128) NOT NULL COMMENT '项目名称',
  `desc`  varchar(512) DEFAULT NULL COMMENT '项目描述',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `status`  tinyint(4)   NOT NULL DEFAULT 1 COMMENT '项目状态: 1-正常, 2-删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PROJECT_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='项目模板表';


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
-- Table structure for `t_user_algorithm`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_algorithm`;
CREATE TABLE `t_user_algorithm` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户算法表ID(自增长)',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法表id',
    `authorization_type`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权方式: 1-按时间, 2-按次数, 3-永久',
    `authorization_value` bigint(20) DEFAULT NULL COMMENT '授权值: 按时间单位为（天），按次数单位为（次）',
    `authorize_begin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权开始时间',
    `authorize_end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权结束时间',
    `authorization_status`  tinyint(4)  NOT NULL DEFAULT 1 COMMENT '授权状态: 0-待申请,1-申请中, 2-已授权,3-已拒绝',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户算法表';

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
-- Table structure for `t_algorithm_code`
-- ----------------------------
DROP TABLE IF EXISTS `t_algorithm_code`;
CREATE TABLE `t_algorithm_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法代码表ID(自增长)',
  `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
  `calculate_contract_code` TEXT DEFAULT NULL COMMENT '计算合约',
  `data_split_contract_code` TEXT DEFAULT NULL COMMENT '数据分片合约',
  `edit_type` tinyint(4) DEFAULT NULL COMMENT '编辑类型:1-sql,2-noteBook',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='算法代码表';


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
  `run_status` tinyint(4) DEFAULT NULL COMMENT '运行状态:0-未完成,1-已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流表';

-- ----------------------------
-- Table structure for `t_workflow_template`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_template`;
CREATE TABLE `t_workflow_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目工作流模板ID(自增长)',
  `name` varchar(64) DEFAULT NULL COMMENT '工作流名称',
  `desc`  varchar(128) DEFAULT NULL COMMENT '工作流描述',
  `project_template_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建方id',
  `node_number` bigint(20) DEFAULT NULL COMMENT '节点数',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态:1-正常，2-删除',
  `run_status` tinyint(4) DEFAULT NULL COMMENT '运行状态:0-未完成,1-已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流模板表';


-- ----------------------------
-- Table structure for `t_workflow_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node`;
CREATE TABLE `t_workflow_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点ID(自增长)',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `step` tinyint(4) DEFAULT NULL COMMENT '节点在工作流中序号,从1开始',
  `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
  `run_status` tinyint(4) DEFAULT NULL COMMENT '运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点表';

-- ----------------------------
-- Table structure for `t_workflow_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node_template`;
CREATE TABLE `t_workflow_node_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点模板表ID(自增长)',
  `workflow_template_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `step` tinyint(4) DEFAULT NULL COMMENT '节点在工作流中序号,从1开始',
  `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
  `run_status` tinyint(4) DEFAULT NULL COMMENT '运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点模板表';


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
-- Table structure for `t_workflow_node_variable`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node_variable`;
CREATE TABLE `t_workflow_node_variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点ID(自增长)',

  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点变量表';



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
-- Table structure for `t_workflow_node_resource_template`
-- ----------------------------
DROP TABLE IF EXISTS `t_workflow_node_resource_template`;
CREATE TABLE `t_workflow_node_resource_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '节点资源表ID(自增长)',
  `workflow_node_template_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
  `cpu` tinyint(4) DEFAULT NULL COMMENT 'cpu核数（单位：核）',
  `cost_mem` int(11) DEFAULT NULL COMMENT '内存大小（G）',
  `gpu` int(11) DEFAULT NULL COMMENT 'GPU大小（G）',
  `bandwidth` int(11) DEFAULT NULL COMMENT '带宽（M）',
  `duration` bigint(20) DEFAULT NULL COMMENT '所需的运行时长 (单位: ms)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作流节点资源模板表';


-- ----------------------------
-- Table structure for `t_sub_job`
-- ----------------------------
DROP TABLE IF EXISTS `t_sub_job`;
CREATE TABLE `t_sub_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务表ID(自增长)',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `name` varchar(64) DEFAULT NULL COMMENT '工作流名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '作业状态:0-未开始,1-运行中,2-运行成功,3-运行失败',
  `current_step` tinyint(4) DEFAULT NULL COMMENT '当前运行节点序号:0-未启动',
  `dist_step` tinyint(4) DEFAULT NULL COMMENT '即将到运行节点序号',
  `last_step` tinyint(4) DEFAULT NULL COMMENT '最后一个运行节点序号:默认工作流最后一个节点',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_workflow_Id_NAME` (`workflow_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子作业表';

-- ----------------------------
-- Table structure for `t_sub_job_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_sub_job_node`;
CREATE TABLE `t_sub_job_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务表ID(自增长)',
  `task_name` varchar(32) DEFAULT NULL COMMENT '任务名称',
  `task_no` varchar(32) DEFAULT NULL COMMENT 'RosettaNet任务处理Id',
  `job_id` bigint(20) DEFAULT NULL COMMENT '工作流实例表id',
  `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态:0-未开始,1-运行中,2-运行成功,3-运行失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子作业节点表';



-- ----------------------------
-- Table structure for `t_job`
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务计划表ID(自增长)',
  `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
  `name` varchar(64) DEFAULT NULL COMMENT '作业名称',
  `desc` varchar(64) DEFAULT NULL COMMENT '作业描述',
  `repeat_flag` tinyint(4) DEFAULT NULL DEFAULT 1 COMMENT '是否重复：0-否,1-是',
  `repeat_interval` int(11) DEFAULT NULL COMMENT '重复间隔，单位分钟',
  `begin_time` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `status`  tinyint(4)   NOT NULL DEFAULT 0 COMMENT '状态: 0-未结束，1-已结束',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='作业表';

-- ----------------------------
-- Table structure for `t_task_event`
-- 是否需要从rosettanet同步待确认
-- ----------------------------
DROP TABLE IF EXISTS `t_task_event`;
CREATE TABLE `t_task_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务表ID(自增长)',
  `task_id` varchar(256) NOT NULL COMMENT '任务ID,hash',
  `type` varchar(20) NOT NULL COMMENT '事件类型',
  `identity_id` varchar(256) NOT NULL COMMENT '产生事件的组织身份ID',
  `event_time` datetime NOT NULL COMMENT '产生事件的时间',
  `content` varchar(512) NOT NULL COMMENT '事件内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务事件表';