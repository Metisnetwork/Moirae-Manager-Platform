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
