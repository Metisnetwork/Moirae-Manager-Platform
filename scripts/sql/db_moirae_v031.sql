CREATE DATABASE IF NOT EXISTS `db_moirae` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `db_moirae`;

/*Table structure for table `t_algorithm` */

DROP TABLE IF EXISTS `t_algorithm`;

CREATE TABLE `t_algorithm` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法表ID(自增长)',
    `algorithm_code` varchar(64)  NOT NULL COMMENT '算法编号（线性回归、逻辑回归、。。。）',
    `algorithm_step` int(11) DEFAULT '1' COMMENT '算法步骤（如训练-1, 预测-2）',
    `algorithm_name` varchar(60)  DEFAULT NULL COMMENT '中文算法名称',
    `algorithm_name_en` varchar(60)  DEFAULT NULL COMMENT '英文算法名称',
    `algorithm_desc` varchar(200)  DEFAULT NULL COMMENT '中文算法描述',
    `algorithm_desc_en` varchar(200)  DEFAULT NULL COMMENT '英文算法描述',
    `author` varchar(30)  DEFAULT NULL COMMENT '算法作者',
    `max_numbers` bigint(20) DEFAULT NULL COMMENT '支持协同方最大数量',
    `min_numbers` bigint(20) DEFAULT NULL COMMENT '支持协同方最小数量',
    `support_language` varchar(64)  DEFAULT NULL COMMENT '支持语言,多个以","进行分隔',
    `support_os_system` varchar(64)  DEFAULT NULL COMMENT '支持操作系统,多个以","进行分隔',
    `algorithm_type` tinyint(4) DEFAULT NULL COMMENT '算法所属大类:1-统计分析,2-特征工程,3-机器学习',
    `cost_mem` bigint(20) DEFAULT NULL COMMENT '所需的内存 (单位: byte)',
    `cost_cpu` int(11) DEFAULT NULL COMMENT '所需的核数 (单位: 个)',
    `cost_gpu` int(11) DEFAULT NULL COMMENT 'GPU核数(单位：核)',
    `cost_bandwidth` bigint(20) DEFAULT '0' COMMENT '所需的带宽 (单位: bps)',
    `run_time` bigint(20) NOT NULL DEFAULT '3600000' COMMENT '所需的运行时长,默认1小时 (单位: ms)',
    `input_model` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否需要输入模型: 0-否，1:是',
    `output_model` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否产生模型: 0-否，1:是',
    `store_pattern` tinyint(4) NOT NULL DEFAULT '1' COMMENT '输出存储形式: 1-明文，2:密文',
    `data_rows_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否判断数据行数: 0-否，1-是',
    `data_columns_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否判断数据列数: 0-否，1-是',
    `public_Flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否是公有算法: 0-否，1-是',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1-有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ALG_NAME` (`algorithm_name`)
) ENGINE=InnoDB COMMENT='算法表';

/*Table structure for table `t_algorithm_auth` */

DROP TABLE IF EXISTS `t_algorithm_auth`;

CREATE TABLE `t_algorithm_auth` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法授权表ID(自增长)',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `algorithm_id` bigint(20) NOT NULL COMMENT '算法表id',
    `auth_type` tinyint(4) DEFAULT NULL COMMENT '授权方式: 1-按时间, 2-按次数, 3-永久',
    `auth_value` bigint(20) DEFAULT NULL COMMENT '授权值: 按次数单位为（次）',
    `auth_begin_time` datetime DEFAULT NULL COMMENT '授权开始时间',
    `auth_end_time` datetime DEFAULT NULL COMMENT '授权结束时间',
    `auth_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '授权状态: 0-待申请,1-申请中, 2-已授权,3-已拒绝',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `user_algorithm_id` (`user_id`,`algorithm_id`)
) ENGINE=InnoDB COMMENT='算法授权表';

/*Table structure for table `t_algorithm_code` */

DROP TABLE IF EXISTS `t_algorithm_code`;

CREATE TABLE `t_algorithm_code` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法代码表ID(自增长)',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
    `edit_type` tinyint(4) DEFAULT NULL COMMENT '编辑类型:1-sql,2-noteBook',
    `calculate_contract_code` text  COMMENT '计算合约',
    `data_split_contract_code` text  COMMENT '数据分片合约',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `algorithm_id` (`algorithm_id`)
) ENGINE=InnoDB COMMENT='算法代码表';

/*Table structure for table `t_algorithm_type` */

DROP TABLE IF EXISTS `t_algorithm_type`;

CREATE TABLE `t_algorithm_type` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法表ID(自增长)',
    `algorithm_type_name` varchar(30)  DEFAULT NULL COMMENT '中文算法名称',
    `algorithm_type_name_en` varchar(60)  DEFAULT NULL COMMENT '英文算法名称',
    `algorithm_type_desc` varchar(200)  DEFAULT NULL COMMENT '中文算法描述',
    `algorithm_type_desc_en` varchar(200)  DEFAULT NULL COMMENT '英文算法描述',
    `algorithm_type` tinyint(4) DEFAULT NULL COMMENT '算法所属大类:1-统计分析,2-特征工程,3-机器学习',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1-有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='算法大类表';

/*Table structure for table `t_algorithm_variable` */

DROP TABLE IF EXISTS `t_algorithm_variable`;

CREATE TABLE `t_algorithm_variable` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法变量表ID(自增长)',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法表id',
    `var_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '变量类型: 1-自变量, 2-因变量',
    `var_key` varchar(128)  NOT NULL COMMENT '变量key',
    `var_value` varchar(128)  NOT NULL COMMENT '变量值',
    `var_desc` varchar(512)  DEFAULT NULL COMMENT '变量描述',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `algorithm_id` (`algorithm_id`)
) ENGINE=InnoDB COMMENT='算法变量表';

/*Table structure for table `t_algorithm_variable_struct` */

DROP TABLE IF EXISTS `t_algorithm_variable_struct`;

CREATE TABLE `t_algorithm_variable_struct` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法变量模板结构表ID(自增长)',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法表id',
    `struct` varchar(1024)  NOT NULL COMMENT '模板json格式结构',
    `desc` varchar(128)  DEFAULT NULL COMMENT '模板描述',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `algorithm_id` (`algorithm_id`)
) ENGINE=InnoDB COMMENT='算法变量模板结构表';

/*Table structure for table `t_data_sync` */

DROP TABLE IF EXISTS `t_data_sync`;

CREATE TABLE `t_data_sync` (
    `data_type` varchar(256) CHARACTER SET utf8mb4 NOT NULL COMMENT '数据类型',
    `latest_synced` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据最新同步时间戳，精确到毫秒',
    `info` varchar(50) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '描述',
    PRIMARY KEY (`data_type`)
) ENGINE=InnoDB COMMENT='数据同步时间记录';

/*Table structure for table `t_model` */

DROP TABLE IF EXISTS `t_model`;

CREATE TABLE `t_model` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模型表ID(自增长)',
    `org_identity_id` varchar(128)  NOT NULL COMMENT '所属组织',
    `name` varchar(128)  DEFAULT NULL COMMENT '名称',
    `meta_data_id` varchar(128)  DEFAULT NULL COMMENT '元数据id',
    `file_id` varchar(256)  DEFAULT NULL COMMENT '源文件ID',
    `file_path` varchar(128)  NOT NULL COMMENT '源文件存放路径',
    `train_task_id` varchar(256)  NOT NULL COMMENT '训练模型的任务id',
    `train_algorithm_id` bigint(20) DEFAULT NULL COMMENT '训练模型的算法id',
    `train_user_address` varchar(64)  DEFAULT NULL COMMENT '训练模型的账户',
    `supported_algorithm_id` bigint(20) DEFAULT NULL COMMENT '模型支持输入的算法id',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1-有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='模型表';

/*Table structure for table `t_project` */

DROP TABLE IF EXISTS `t_project`;

CREATE TABLE `t_project` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目ID(自增长)',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id(创建者id)',
    `project_name` varchar(30)  NOT NULL COMMENT '项目名称',
    `project_desc` varchar(200)  DEFAULT NULL COMMENT '项目描述',
    `del_version` bigint(11) DEFAULT '0' COMMENT '版本标识，用于逻辑删除',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_NAME` (`user_id`, `project_name`,`status`,`del_version`),
    KEY `user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='项目表';

/*Table structure for table `t_project_member` */

DROP TABLE IF EXISTS `t_project_member`;

CREATE TABLE `t_project_member` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目成员ID(自增长)',
    `project_id` bigint(20) NOT NULL COMMENT '项目id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `role` tinyint(4) DEFAULT NULL COMMENT '角色：1-管理员，2-编辑着, 3-查看着',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_PROJ_USER_ID` (`project_id`,`user_id`),
    KEY `user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='项目成员管理表';

/*Table structure for table `t_project_temp` */

DROP TABLE IF EXISTS `t_project_temp`;

CREATE TABLE `t_project_temp` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目模板表ID(自增长)',
    `project_name` varchar(30)  NOT NULL COMMENT '中文项目名称',
    `project_name_en` varchar(30)  NOT NULL COMMENT '英文项目名称',
    `project_desc` varchar(200)  DEFAULT NULL COMMENT '中文项目描述',
    `project_desc_en` varchar(200)  DEFAULT NULL COMMENT '英文项目描述',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_NAME` (`project_name`) USING BTREE
) ENGINE=InnoDB COMMENT='项目模板表';

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID(自增长)',
    `user_name` varchar(64)  NOT NULL COMMENT '用户名',
    `address` varchar(64)  NOT NULL COMMENT '用户钱包地址',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `org_identity_id` varchar(128)  DEFAULT NULL COMMENT '默认连接的组织id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ADDRESS` (`address`) USING BTREE COMMENT '用户地址唯一',
    UNIQUE KEY `UK_USERNAME` (`user_name`) USING BTREE COMMENT '用户名称唯一'
) ENGINE=InnoDB COMMENT='用户表';

/*Table structure for table `t_workflow` */

DROP TABLE IF EXISTS `t_workflow`;

CREATE TABLE `t_workflow` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流ID(自增长)',
    `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id(创建方id)',
    `workflow_name` varchar(60)  DEFAULT NULL COMMENT '工作流名称',
    `workflow_desc` varchar(200)  DEFAULT NULL COMMENT '工作流描述',
    `address` varchar(64)  DEFAULT NULL COMMENT '发起任务的账户',
    `sign` varchar(512)  DEFAULT NULL COMMENT '发起任务的账户的签名',
    `edit_version` bigint(11) DEFAULT '1' COMMENT '编辑版本标识，每次编辑后递增，从1开始',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1- 有效',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='工作流表';

/*Table structure for table `t_workflow_node` */

DROP TABLE IF EXISTS `t_workflow_node`;

CREATE TABLE `t_workflow_node` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点表ID(自增长)',
    `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
    `workflow_edit_version` int(11) NOT NULL DEFAULT '1' COMMENT '工作流版本号',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
    `node_name` varchar(30)  DEFAULT NULL COMMENT '节点名称',
    `node_step` int(11) DEFAULT NULL COMMENT '节点在工作流中序号,从1开始',
    `input_model` int(11) NOT NULL DEFAULT '0' COMMENT '是否需要输入模型: 0-否，1:是',
    `model_id` bigint(20) DEFAULT NULL COMMENT '工作流节点需要的模型id,对应t_task_result表id',
    `sender_identity_id` varchar(128)  DEFAULT NULL COMMENT '任务发启放组织id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_NODE_STEP` (`workflow_id`,`workflow_edit_version`,`node_step`)
) ENGINE=InnoDB COMMENT='工作流节点表';

/*Table structure for table `t_workflow_node_code` */

DROP TABLE IF EXISTS `t_workflow_node_code`;

CREATE TABLE `t_workflow_node_code` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点算法代码表ID(自增长)',
    `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
    `edit_type` tinyint(4) DEFAULT NULL COMMENT '编辑类型:1-sql, 2-noteBook',
    `calculate_contract_code` text  COMMENT '计算合约',
    `data_split_contract_code` text  COMMENT '数据分片合约',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `workflow_node_id` (`workflow_node_id`)
) ENGINE=InnoDB COMMENT='工作流节点算法代码表';

/*Table structure for table `t_workflow_node_input` */

DROP TABLE IF EXISTS `t_workflow_node_input`;

CREATE TABLE `t_workflow_node_input` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点输入表ID(自增长)',
    `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
    `identity_id` varchar(128)  DEFAULT NULL COMMENT '组织的身份标识Id',
    `data_table_id` varchar(128)  DEFAULT NULL COMMENT '数据表ID',
    `key_column` bigint(20) DEFAULT NULL COMMENT 'ID列(列索引)(存id值)',
    `dependent_variable` bigint(20) DEFAULT NULL COMMENT '因变量(标签)(存id值)',
    `data_column_ids` varchar(1024)  DEFAULT NULL COMMENT '数据字段ID索引(存id值)',
    `party_id` varchar(64)  DEFAULT NULL COMMENT '任务里面定义的 (p0 -> pN 方 ...)',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `workflow_node_id` (`workflow_node_id`)
) ENGINE=InnoDB COMMENT='工作流节点输入表';

/*Table structure for table `t_workflow_node_output` */

DROP TABLE IF EXISTS `t_workflow_node_output`;

CREATE TABLE `t_workflow_node_output` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点输出表ID(自增长)',
    `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
    `identity_id` varchar(128)  DEFAULT NULL COMMENT '协同方组织的身份标识Id',
    `store_pattern` tinyint(4) DEFAULT '1' COMMENT '存储形式: 1-明文，2:密文',
    `output_content` text  COMMENT '输出内容',
    `party_id` varchar(64)  DEFAULT NULL COMMENT '任务里面定义的 (q0 -> qN 方 ...)',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `workflow_node_id` (`workflow_node_id`)
) ENGINE=InnoDB COMMENT='项目工作流节点输出表';

/*Table structure for table `t_workflow_node_resource` */

DROP TABLE IF EXISTS `t_workflow_node_resource`;

CREATE TABLE `t_workflow_node_resource` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点资源表ID(自增长)',
    `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
    `cost_mem` bigint(20) DEFAULT NULL COMMENT '所需的内存 (单位: byte)',
    `cost_cpu` int(20) DEFAULT NULL COMMENT '所需的核数 (单位: 个)',
    `cost_gpu` int(11) DEFAULT NULL COMMENT 'GPU核数(单位：核)',
    `cost_bandwidth` bigint(20) DEFAULT '0' COMMENT '所需的带宽 (单位: bps)',
    `run_time` bigint(20) DEFAULT NULL COMMENT '所需的运行时长 (单位: ms)',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `workflow_node_id` (`workflow_node_id`)
) ENGINE=InnoDB COMMENT='工作流节点资源表';

/*Table structure for table `t_workflow_node_variable` */

DROP TABLE IF EXISTS `t_workflow_node_variable`;

CREATE TABLE `t_workflow_node_variable` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点变量表ID(自增长)',
    `workflow_node_id` bigint(20) DEFAULT NULL COMMENT '工作流节点id',
    `var_node_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '变量类型: 1-自变量, 2-因变量',
    `var_node_key` varchar(128)  NOT NULL COMMENT '变量key',
    `var_node_value` varchar(128)  NOT NULL COMMENT '变量值',
    `var_node_desc` varchar(200)  DEFAULT NULL COMMENT '变量描述',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `workflow_node_id` (`workflow_node_id`)
) ENGINE=InnoDB COMMENT='工作流节点变量表';

/*Table structure for table `t_workflow_run_status` */

DROP TABLE IF EXISTS `t_workflow_run_status`;

CREATE TABLE `t_workflow_run_status` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点表ID(自增长)',
    `workflow_id` bigint(20) DEFAULT NULL COMMENT '工作流id',
    `workflow_edit_version` int(11) NOT NULL DEFAULT '1' COMMENT '工作流版本号',
    `sign` varchar(512)  NOT NULL COMMENT '发起任务的账户的签名',
    `address` varchar(64)  NOT NULL COMMENT '发起任务的账户的地址',
    `step` int(11) DEFAULT NULL COMMENT '总步骤',
    `cur_step` int(11) DEFAULT NULL COMMENT '当前步骤',
    `begin_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
    `run_status` tinyint(4) DEFAULT NULL COMMENT '运行状态: 1-运行中,2-运行成功,3-运行失败',
    `cancel_status` tinyint(4) DEFAULT NULL COMMENT '取消状态: 1-取消中,2-取消成功,3-取消失败',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='工作流运行状态';

/*Table structure for table `t_workflow_run_task_result` */

DROP TABLE IF EXISTS `t_workflow_run_task_result`;

CREATE TABLE `t_workflow_run_task_result` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID(自增长)',
    `identity_id` varchar(128)  NOT NULL COMMENT '所属组织',
    `task_id` varchar(256)  DEFAULT NULL COMMENT '任务ID,底层处理完成后返回',
    `file_name` varchar(128)  NOT NULL COMMENT '任务结果文件的名称',
    `metadata_id` varchar(128)  NOT NULL COMMENT '任务结果文件的元数据Id <系统默认生成的元数据>',
    `origin_id` varchar(128)  NOT NULL COMMENT '任务结果文件的原始文件Id',
    `file_path` varchar(256)  NOT NULL COMMENT '任务结果文件的完整相对路径名',
    `ip` varchar(32)  NOT NULL COMMENT '任务结果文件所在的 数据服务内网ip',
    `port` varchar(8)  NOT NULL COMMENT '任务结果文件所在的 数据服务内网port',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='工作流任务运行状态';

/*Table structure for table `t_workflow_run_task_status` */

DROP TABLE IF EXISTS `t_workflow_run_task_status`;

CREATE TABLE `t_workflow_run_task_status` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID(自增长)',
    `workflow_run_id` bigint(20) NOT NULL COMMENT '工作流运行状态ID',
    `workflow_node_id` bigint(20) NOT NULL COMMENT '工作流节点配置ID',
    `node_step` int(11) DEFAULT NULL COMMENT '节点在工作流中序号,从1开始',
    `begin_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
    `run_status` tinyint(4) DEFAULT NULL COMMENT '运行状态: :0-未开始 1-运行中,2-运行成功,3-运行失败',
    `task_id` varchar(256)  DEFAULT NULL COMMENT '任务ID,底层处理完成后返回',
    `run_msg` varchar(256)  DEFAULT NULL COMMENT '任务处理结果描述',
    `model_id` bigint(20) DEFAULT '0' COMMENT '工作流节点需要的模型id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='工作流任务运行状态';

/*Table structure for table `t_workflow_temp` */

DROP TABLE IF EXISTS `t_workflow_temp`;

CREATE TABLE `t_workflow_temp` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流模板ID(自增长)',
    `project_temp_id` bigint(20) DEFAULT NULL COMMENT '项目模板表id',
    `workflow_name` varchar(64)  DEFAULT NULL COMMENT '中文工作流名称',
    `workflow_name_en` varchar(128)  DEFAULT NULL COMMENT '英文工作流名称',
    `workflow_desc` varchar(128)  DEFAULT NULL COMMENT '中文工作流描述',
    `workflow_desc_en` varchar(128)  DEFAULT NULL COMMENT '英文工作流描述',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='工作流模板表';

/*Table structure for table `t_workflow_temp_node` */

DROP TABLE IF EXISTS `t_workflow_temp_node`;

CREATE TABLE `t_workflow_temp_node` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工作流节点模板表ID(自增长)',
    `workflow_temp_id` bigint(20) DEFAULT NULL COMMENT '工作流模板表id',
    `algorithm_id` bigint(20) DEFAULT NULL COMMENT '算法id',
    `node_name` varchar(30)  DEFAULT NULL COMMENT '中文节点名称',
    `node_name_en` varchar(60)  DEFAULT NULL COMMENT '英文节点名称',
    `node_step` int(11) DEFAULT NULL COMMENT '节点在工作流中序号,从1开始',
    `input_model` int(11) NOT NULL DEFAULT '0' COMMENT '是否需要输入模型: 0-否，1:是',
    `model_id` bigint(20) DEFAULT NULL COMMENT '工作流节点需要的模型id,对应t_task_result表id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='工作流节点模板表';

DROP TABLE IF EXISTS `dc_org`;
CREATE TABLE `dc_org` (
    `identity_id` VARCHAR(200) NOT NULL COMMENT '身份认证标识的id',
    `node_name` VARCHAR(100) DEFAULT NULL COMMENT '组织身份名称',
    `node_id` VARCHAR(200) NOT NULL COMMENT '组织节点ID',
    `image_url` VARCHAR(256) DEFAULT NULL COMMENT '组织机构图像url',
    `details` VARCHAR(256)  DEFAULT NULL COMMENT '组织机构简介',
    `status` INT NOT NULL DEFAULT '1' COMMENT '状态,1-Normal; 2-NonNormal',
    `update_at` TIMESTAMP(3) NOT NULL COMMENT '(状态)修改时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`identity_id`),
    KEY `update_at` (`update_at`)
) ENGINE=INNODB COMMENT='数据中心组织信息';

DROP TABLE IF EXISTS `mo_org_expand`;
CREATE TABLE `mo_org_expand` (
    `identity_id` VARCHAR(200) NOT NULL COMMENT '身份认证标识的id',
    `identity_ip` VARCHAR(20) NOT NULL COMMENT '组织的ip',
    `identity_port` INT NOT NULL COMMENT '组织的端口',
    `is_public` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否公共组织: 0-否，1-是',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`identity_id`)
) ENGINE=INNODB COMMENT='组织扩展表';

DROP TABLE IF EXISTS `mo_org_user`;
CREATE TABLE `mo_org_user` (
    `identity_id` varchar(128)  NOT NULL COMMENT '组织的身份标识Id',
    `address` varchar(64)  NOT NULL COMMENT '用户的钱包地址',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`identity_id`, `address`)
) ENGINE=InnoDB COMMENT='组织管理用户';

DROP TABLE IF EXISTS `dc_meta_data`;
CREATE TABLE `dc_meta_data` (
    `meta_data_id` varchar(200)  NOT NULL COMMENT '元数据ID,hash',
    `origin_id` varchar(200)  NOT NULL COMMENT '数据文件ID,hash',
    `identity_id` varchar(200)  NOT NULL COMMENT '组织身份ID',
    `file_name` varchar(100)  NOT NULL COMMENT '文件名称',
    `file_path` varchar(100)  NOT NULL COMMENT '文件存储路径',
    `file_type` int NOT NULL COMMENT '文件后缀/类型, 0:未知; 1:csv',
    `industry` varchar(100)  DEFAULT NULL COMMENT '行业名称',
    `size` bigint NOT NULL DEFAULT '0' COMMENT '文件大小(字节)',
    `rows` int NOT NULL DEFAULT '0' COMMENT '数据行数(不算title)',
    `columns` int NOT NULL DEFAULT '0' COMMENT '数据列数',
    `published_at` datetime(3) NOT NULL COMMENT '发布时间，精确到毫秒',
    `has_title` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否带标题',
    `remarks` varchar(100)  DEFAULT NULL COMMENT '数据描述',
    `status` int DEFAULT NULL COMMENT '元数据的状态 (0: 未知; 1: 还未发布的新表; 2: 已发布的表; 3: 已撤销的表)',
    `update_at` timestamp(3) NOT NULL COMMENT '(状态)修改时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`meta_data_id`),
    KEY `update_at` (`update_at`)
) ENGINE=InnoDB  COMMENT='数据文件信息';

DROP TABLE IF EXISTS `dc_meta_data_column`;
CREATE TABLE `dc_meta_data_column` (
    `meta_data_id` varchar(200) NOT NULL COMMENT '元数据ID,hash',
    `column_idx` int NOT NULL COMMENT '字段索引序号',
    `column_name` varchar(100) NOT NULL COMMENT '字段名称',
    `column_type` varchar(100) NOT NULL COMMENT '字段类型',
    `column_size` int NOT NULL DEFAULT '0' COMMENT '字段大小',
    `remarks` varchar(100) DEFAULT NULL COMMENT '字段描述',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`meta_data_id`,`column_idx`)
) ENGINE=InnoDB COMMENT='数据文件元数据信息';

DROP TABLE IF EXISTS `dc_task`;
CREATE TABLE `dc_task` (
    `id` varchar(200)  NOT NULL COMMENT '任务ID,hash',
    `task_name` varchar(100)  NOT NULL COMMENT '任务名称',
    `user_id` varchar(200)  NOT NULL COMMENT '发起任务的用户的信息 (task是属于用户的)',
    `user_type` int NOT NULL COMMENT '用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址',
    `required_memory` bigint NOT NULL DEFAULT '0' COMMENT '需要的内存, 字节',
    `required_core` int NOT NULL DEFAULT '0' COMMENT '需要的core',
    `required_bandwidth` bigint NOT NULL DEFAULT '0' COMMENT '需要的带宽, bps',
    `required_duration` bigint NOT NULL DEFAULT '0' COMMENT '需要的时间, milli seconds',
    `owner_identity_id` varchar(200)  NOT NULL COMMENT '任务创建者组织身份ID',
    `owner_party_id` varchar(200)  NOT NULL COMMENT '任务参与方在本次任务中的唯一识别ID',
    `create_at` datetime(3) NOT NULL COMMENT '任务创建时间，精确到毫秒',
    `start_at` datetime(3) DEFAULT NULL COMMENT '任务开始执行时间，精确到毫秒',
    `end_at` datetime(3) DEFAULT NULL COMMENT '任务结束时间，精确到毫秒',
    `used_memory` bigint NOT NULL DEFAULT '0' COMMENT '使用的内存, 字节',
    `used_core` int NOT NULL DEFAULT '0' COMMENT '使用的core',
    `used_bandwidth` bigint NOT NULL DEFAULT '0' COMMENT '使用的带宽, bps',
    `used_file_size` bigint DEFAULT '0' COMMENT '使用的所有数据大小，字节',
    `status` int DEFAULT NULL COMMENT '任务状态, 0:未知;1:等待中;2:计算中,3:失败;4:成功',
    `status_desc` varchar(255)  DEFAULT NULL COMMENT '任务状态说明',
    `remarks` varchar(255)  DEFAULT NULL COMMENT '任务描述',
    `task_sign` varchar(1024)  DEFAULT NULL COMMENT '任务签名',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `end_at` (`end_at`)
) ENGINE=InnoDB COMMENT='任务';

DROP TABLE IF EXISTS `dc_task_algo_provider`;
CREATE TABLE `dc_task_algo_provider` (
    `task_id` varchar(200)  NOT NULL COMMENT '任务ID,hash',
    `identity_id` varchar(200)  NOT NULL COMMENT '算法提供者组织身份ID',
    `party_id` varchar(200)  NOT NULL COMMENT '任务参与方在本次任务中的唯一识别ID',
    PRIMARY KEY (`task_id`)
) ENGINE=InnoDB COMMENT='任务算法提供者';

DROP TABLE IF EXISTS `dc_task_data_provider`;
CREATE TABLE `dc_task_data_provider` (
    `task_id` varchar(200)  NOT NULL COMMENT '任务ID,hash',
    `meta_data_id` varchar(200)  NOT NULL COMMENT '参与任务的元数据ID',
    `identity_id` varchar(200)  NOT NULL COMMENT '(冗余)参与任务的元数据的所属组织的identity_id',
    `party_id` varchar(200)  NOT NULL COMMENT '任务参与方在本次任务中的唯一识别ID',
    `key_column_idx` int DEFAULT NULL COMMENT '元数据在此次任务中的主键列下标索引序号',
    PRIMARY KEY (`task_id`,`meta_data_id`)
) ENGINE=InnoDB COMMENT='任务数据提供者（数据和模型）';

DROP TABLE IF EXISTS `dc_task_meta_data_column`;
CREATE TABLE `dc_task_meta_data_column` (
    `task_id` varchar(200)  NOT NULL COMMENT '任务ID,hash',
    `meta_data_id` varchar(200)  NOT NULL COMMENT '参与任务的元数据ID',
    `selected_column_idx` int NOT NULL COMMENT '元数据在此次任务中的参与计算的字段索引序号',
    PRIMARY KEY (`task_id`,`meta_data_id`,`selected_column_idx`)
) ENGINE=InnoDB COMMENT='任务数据metadata明细';

DROP TABLE IF EXISTS `dc_task_power_provider`;
CREATE TABLE `dc_task_power_provider` (
    `task_id` varchar(200)  NOT NULL COMMENT '任务ID,hash',
    `identity_id` varchar(200)  NOT NULL COMMENT '算力提供者组织身份ID',
    `party_id` varchar(200)  NOT NULL COMMENT '任务参与方在本次任务中的唯一识别ID',
    `used_memory` bigint DEFAULT '0' COMMENT '任务使用的内存, 字节',
    `used_core` int DEFAULT '0' COMMENT '任务使用的core',
    `used_bandwidth` bigint DEFAULT '0' COMMENT '任务使用的带宽, bps',
    PRIMARY KEY (`task_id`,`identity_id`)
) ENGINE=InnoDB COMMENT='任务算力提供者';

DROP TABLE IF EXISTS `dc_task_result_consumer`;
CREATE TABLE `dc_task_result_consumer` (
    `task_id` varchar(200)  NOT NULL COMMENT '任务ID,hash',
    `consumer_identity_id` varchar(200)  NOT NULL COMMENT '结果消费者组织身份ID',
    `consumer_party_id` varchar(200)  NOT NULL COMMENT '任务参与方在本次任务中的唯一识别ID',
    `producer_identity_id` varchar(200)  DEFAULT NULL COMMENT '结果产生者的组织身份ID',
    `producer_party_id` varchar(200)  DEFAULT NULL COMMENT '任务参与方在本次任务中的唯一识别ID',
    PRIMARY KEY (`task_id`,`consumer_identity_id`)
) ENGINE=InnoDB COMMENT='任务结果接收者';

DROP TABLE IF EXISTS `dc_power_server`;
CREATE TABLE `dc_power_server` (
    `id` varchar(200) NOT NULL COMMENT '计算服务主机ID,hash',
    `identity_id` varchar(200) NOT NULL COMMENT '组织身份ID',
    `memory` bigint NOT NULL DEFAULT '0' COMMENT '计算服务内存, 字节',
    `core` int NOT NULL DEFAULT '0' COMMENT '计算服务core',
    `bandwidth` bigint NOT NULL DEFAULT '0' COMMENT '计算服务带宽, bps',
    `used_memory` bigint DEFAULT '0' COMMENT '使用的内存, 字节',
    `used_core` int DEFAULT '0' COMMENT '使用的core',
    `used_bandwidth` bigint DEFAULT '0' COMMENT '使用的带宽, bps',
    `published` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否发布，true/false',
    `published_at` datetime(3) NOT NULL COMMENT '发布时间，精确到毫秒',
    `status` int DEFAULT NULL COMMENT '算力的状态 (0: 未知; 1: 还未发布的算力; 2: 已发布的算力(算力未被占用); 3: 已发布的算力(算力正在被占用); 4: 已撤销的算力)',
    `update_at` timestamp(3) NOT NULL COMMENT '(状态)修改时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `update_at` (`update_at`)
) ENGINE=InnoDB COMMENT='计算服务信息';

DROP TABLE IF EXISTS `dc_task_event`;
CREATE TABLE `dc_task_event` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `task_id` varchar(200) NOT NULL COMMENT '任务ID,hash',
    `event_type` varchar(20) NOT NULL COMMENT '事件类型',
    `identity_id` varchar(200) NOT NULL COMMENT '产生事件的组织身份ID',
    `party_id` varchar(200) NOT NULL COMMENT '产生事件的partyId (单个组织可以担任任务的多个party, 区分是哪一方产生的event)',
    `event_at` datetime(3) NOT NULL COMMENT '产生事件的时间，精确到毫秒',
    `event_content` varchar(1024) NOT NULL COMMENT '事件内容',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='任务事件';

DROP TABLE IF EXISTS `mo_task_expand`;
CREATE TABLE `mo_task_expand` (
    `id` varchar(200) NOT NULL COMMENT '任务ID,hash',
    `event_synced` tinyint(1) NOT NULL DEFAULT '0' COMMENT '事件是否同步完成 0-否 1-是',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='任务扩展表';

DROP TABLE IF EXISTS `dc_meta_data_auth`;
CREATE TABLE `dc_meta_data_auth` (
    `meta_data_auth_id` varchar(200) NOT NULL COMMENT '申请数据授权的ID',
    `user_identity_id` varchar(200) NOT NULL COMMENT '申请用户所属组织身份ID',
    `user_id` varchar(200) NOT NULL COMMENT '申请数据授权的用户ID',
    `user_type` int NOT NULL COMMENT '用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址',
    `meta_data_id` varchar(200) NOT NULL COMMENT '元数据ID,hash',
    `dfs_data_status` int DEFAULT NULL COMMENT '元数据在分布式存储环境中的状态 (0: DataStatus_Unknown ; DataStatus_Normal = 1; DataStatus_Deleted = 2)',
    `dfs_data_id` varchar(200) DEFAULT NULL COMMENT '元数据在分布式存储环境中的ID',
    `auth_type` int NOT NULL DEFAULT '0' COMMENT '申请收集授权类型：(0: 未定义; 1: 按照时间段来使用; 2: 按照次数来使用)',
    `start_at` datetime DEFAULT NULL COMMENT '授权开始时间(auth_type=1时)',
    `end_at` datetime DEFAULT NULL COMMENT '授权结束时间(auth_type=1时)',
    `times` int DEFAULT '0' COMMENT '授权次数(auth_type=2时)',
    `expired` tinyint(1) DEFAULT '0' COMMENT '是否已过期 (当 usage_type 为 1 时才需要的字段)',
    `used_times` int DEFAULT '0' COMMENT '已经使用的次数 (当 usage_type 为 2 时才需要的字段)',
    `apply_at` datetime(3) NOT NULL COMMENT '授权申请时间，精确到毫秒',
    `audit_option` int DEFAULT '0' COMMENT '审核结果，0：等待审核中；1：审核通过；2：审核拒绝',
    `audit_desc` varchar(256) DEFAULT '' COMMENT '审核意见 (允许""字符)',
    `audit_at` datetime(3) DEFAULT NULL COMMENT '授权审核时间，精确到毫秒',
    `auth_sign` varchar(1024) DEFAULT NULL COMMENT '授权签名hex',
    `auth_status` int DEFAULT '0' COMMENT '数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的>)',
    `update_at` timestamp(3) NOT NULL COMMENT '修改时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`meta_data_auth_id`),
    KEY `update_at` (`update_at`)
) ENGINE=InnoDB COMMENT='元数据文件授权信息';

-- 创建首页统计 view
create or replace view v_global_stats as
select allOrg.total_org_count, powerOrg.power_org_count, srcFile.data_file_size, usedFile.used_data_file_size,
       task.task_count, (partner.partner_count + task.task_count) as partner_count, power.total_core, power.total_memory, power.total_bandwidth
from
--  总组织数
(
    select count(*) as total_org_count
    from dc_org where status= 1
) allOrg,

-- 算力参与方数
(
    select count(oi.identity_id) as power_org_count
    from dc_org oi
    where EXISTS (select 1 from dc_power_server ps where oi.identity_id = ps.identity_id and ps.status in (2, 3))
      and status = 1

) powerOrg,

-- 上传数据量
(
    select IFNULL(sum(size),0) as data_file_size
    from dc_meta_data where status=2
) srcFile,

-- 交易数据量
(
    select ifnull(sum(df.size),0) as used_data_file_size
    from dc_task_data_provider tmd
             left join dc_meta_data df on tmd.meta_data_id = df.meta_data_id
) usedFile,

-- 完成任务数
(
    select count(*) as task_count
    from dc_task
) task,

-- 参与任务总人次：发起人（一个任务一个发起人，所以发起人次数就是taskCount），算力提供者，数据提供者，或者结果消费者，算法提供者
(
    select ifnull(dataPartner.dataPartnerCount,0) + ifnull(powerPartner.powerPartnerCount,0) + ifnull(algoPartner.algoPartnerCount,0)  + ifnull(resultConsumerPartner.resultConsumerPartnerCount,0) as partner_count
    from
        (
            select count(*) as dataPartnerCount
            from dc_task_data_provider
        ) dataPartner,
        (
            select count(*) as powerPartnerCount
            from dc_task_power_provider
        ) powerPartner,
        (
            select count(*) as algoPartnerCount
            from dc_task_algo_provider
        ) algoPartner,
        (
            select count(*) as resultConsumerPartnerCount
            from dc_task_result_consumer
        ) resultConsumerPartner

) as partner,

-- 总算力
(
    select ifnull(sum(p.core),0) as total_core, ifnull(sum(p.memory),0) as total_memory, ifnull(sum(p.bandwidth),0) as total_bandwidth
    from dc_power_server p inner join dc_org o on p.identity_id = o.identity_id
    where o.`status` = 1 and p.`status` in (2, 3)
) as power;

-- 组织参与任务数统计 view （统计组织在任务中的角色是：发起人， 算法提供方，算力提供者，数据提供者，结果消费者）
create or replace view v_org_daily_task_stats as
select tmp.identity_id, count(tmp.task_id) as task_count, date(t.create_at) as task_date
from
    (

        select oi.identity_id, t.id as task_id
        from dc_org oi, dc_task t
        WHERE oi.identity_id = t.owner_identity_id

        union

        select oi.identity_id, tap.task_id as task_id
        from dc_org oi, dc_task_algo_provider tap
        WHERE oi.identity_id = tap.identity_id

        union

        select oi.identity_id, tpp.task_id
        from  dc_org oi, dc_task_power_provider tpp
        WHERE oi.identity_id = tpp.identity_id

        union

        select oi.identity_id, tmd.task_id
        from dc_org oi, dc_task_data_provider tmd
        WHERE oi.identity_id = tmd.identity_id

        union

        select oi.identity_id, trc.task_id
        from dc_org oi, dc_task_result_consumer trc
        WHERE oi.identity_id = trc.consumer_identity_id

    ) tmp, dc_task t
where tmp.task_id = t.id
group by tmp.identity_id, task_date;


-- 创建元数据月统计视图
CREATE OR REPLACE VIEW v_data_file_stats_monthly as
SELECT a.stats_time, a.month_size, SUM(b.month_size) AS accu_size
FROM (
         SELECT DATE_FORMAT(df.published_at, '%Y-%m')  as stats_time, sum(df.size) as month_size
         FROM dc_meta_data df
         WHERE df.status=2
         GROUP BY DATE_FORMAT(df.published_at, '%Y-%m')
         ORDER BY DATE_FORMAT(df.published_at, '%Y-%m')
     ) a
         JOIN (
    SELECT DATE_FORMAT(df.published_at, '%Y-%m')  as stats_time, sum(df.size) as month_size
    FROM dc_meta_data df
    WHERE df.status=2
    GROUP BY DATE_FORMAT(df.published_at, '%Y-%m')
    ORDER BY DATE_FORMAT(df.published_at, '%Y-%m')
) b
              ON a.stats_time >= b.stats_time
GROUP BY a.stats_time
ORDER BY a.stats_time;

-- 创建元数据日统计视图
CREATE OR REPLACE VIEW v_data_file_stats_daily as
SELECT a.stats_time, a.day_size, SUM(b.day_size) AS accu_size
FROM (
         SELECT DATE(df.published_at) as stats_time, sum(df.size) as day_size
         FROM dc_meta_data df
         WHERE df.status=2
         GROUP BY DATE(df.published_at)
         ORDER BY DATE(df.published_at)
     ) a
         JOIN (
    SELECT DATE(df.published_at) as stats_time, sum(df.size) as day_size
    FROM dc_meta_data df
    WHERE df.status=2
    GROUP BY DATE(df.published_at)
    ORDER BY DATE(df.published_at)
) b
              ON a.stats_time >= b.stats_time
GROUP BY a.stats_time
ORDER BY a.stats_time;

-- 创建算力月统计视图
CREATE OR REPLACE VIEW v_power_stats_monthly as
SELECT a.stats_time, a.month_core, a.month_memory, a.month_bandwidth, SUM(b.month_core) AS accu_core, SUM(b.month_memory) AS accu_memory, SUM(b.month_bandwidth) AS accu_bandwidth
FROM (
         SELECT DATE_FORMAT(ps.published_at, '%Y-%m')  as stats_time, sum(ps.core) as month_core, sum(ps.memory) as month_memory, sum(ps.bandwidth) as month_bandwidth
         FROM dc_power_server ps
         WHERE ps.status=2 or ps.status=3
         GROUP BY DATE_FORMAT(ps.published_at, '%Y-%m')
         ORDER BY DATE_FORMAT(ps.published_at, '%Y-%m')
     ) a
         JOIN (
    SELECT DATE_FORMAT(ps.published_at, '%Y-%m')  as stats_time, sum(ps.core) as month_core, sum(ps.memory) as month_memory, sum(ps.bandwidth) as month_bandwidth
    FROM dc_power_server ps
    WHERE ps.status=2 or ps.status=3
    GROUP BY DATE_FORMAT(ps.published_at, '%Y-%m')
    ORDER BY DATE_FORMAT(ps.published_at, '%Y-%m')
) b
              ON a.stats_time >= b.stats_time
GROUP BY a.stats_time
ORDER BY a.stats_time;


-- 创建算力日统计视图
CREATE OR REPLACE VIEW v_power_stats_daily as
SELECT a.stats_time, a.day_core, a.day_memory, a.day_bandwidth, SUM(b.day_core) AS accu_core, SUM(b.day_memory) AS accu_memory, SUM(b.day_bandwidth) AS accu_bandwidth
FROM (
         SELECT DATE(ps.published_at)  as stats_time, sum(ps.core) as day_core, sum(ps.memory) as day_memory, sum(ps.bandwidth) as day_bandwidth
         FROM dc_power_server ps
         WHERE ps.status=2 or ps.status=3
         GROUP BY DATE(ps.published_at)
         ORDER BY DATE(ps.published_at)
     ) a
         JOIN (
    SELECT DATE(ps.published_at)  as stats_time, sum(ps.core) as day_core, sum(ps.memory) as day_memory, sum(ps.bandwidth) as day_bandwidth
    FROM dc_power_server ps
    WHERE ps.status=2 or ps.status=3
    GROUP BY DATE(ps.published_at)
    ORDER BY DATE(ps.published_at)
) b
              ON a.stats_time >= b.stats_time
GROUP BY a.stats_time
ORDER BY a.stats_time;

