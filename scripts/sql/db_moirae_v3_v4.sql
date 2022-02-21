USE `db_moirae`;

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

