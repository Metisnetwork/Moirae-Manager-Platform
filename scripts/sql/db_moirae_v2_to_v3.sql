USE `db_moirae`;

-- 用户组织
DROP TABLE `t_user_org`;
DROP TABLE `t_user_org_maintain`;

CREATE TABLE `t_user_org` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系ID(自增长)',
    `user_address` varchar(64) NOT NULL COMMENT '用户的钱包地址',
    `org_identity_id` varchar(128) NOT NULL COMMENT '组织的身份标识Id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_USER_IDENTITY_ID` (`user_address`,`org_identity_id`)
) ENGINE=InnoDB COMMENT='用户组织关系';

ALTER TABLE `t_user` ADD COLUMN `org_identity_id` VARCHAR(128) NULL   COMMENT '默认发起的组织id' AFTER `status`;

-- 算法
DROP TABLE `t_algorithm`;
CREATE TABLE `t_algorithm`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT '算法表ID(自增长)',
    `algorithm_code` varchar(64) NOT NULL COMMENT '算法编号（线性回归、逻辑回归、。。。）',
    `algorithm_step` int(11) DEFAULT '1' COMMENT '算法步骤（如训练-1, 预测-2）',
    `algorithm_name`    varchar(60)         DEFAULT NULL COMMENT '中文算法名称',
    `algorithm_name_en`    varchar(60)         DEFAULT NULL COMMENT '英文算法名称',
    `algorithm_desc`    varchar(200)        DEFAULT NULL COMMENT '中文算法描述',
    `algorithm_desc_en`    varchar(200)        DEFAULT NULL COMMENT '英文算法描述',
    `author`            varchar(30)         DEFAULT NULL COMMENT '算法作者',
    `max_numbers`       bigint(20)          DEFAULT NULL COMMENT '支持协同方最大数量',
    `min_numbers`       bigint(20)          DEFAULT NULL COMMENT '支持协同方最小数量',
    `support_language`  varchar(64)         DEFAULT NULL COMMENT '支持语言,多个以","进行分隔',
    `support_os_system` varchar(64)         DEFAULT NULL COMMENT '支持操作系统,多个以","进行分隔',
    `algorithm_type`    tinyint(4)          DEFAULT NULL COMMENT '算法所属大类:1-统计分析,2-特征工程,3-机器学习',
    `cost_mem`          bigint(20)          DEFAULT NULL COMMENT '所需的内存 (单位: byte)',
    `cost_cpu`          int(11)             DEFAULT NULL COMMENT '所需的核数 (单位: 个)',
    `cost_gpu`          int(11)             DEFAULT NULL COMMENT 'GPU核数(单位：核)',
    `cost_bandwidth`    bigint(20)          DEFAULT '0' COMMENT '所需的带宽 (单位: bps)',
    `run_time`          bigint(20) NOT NULL DEFAULT '3600000' COMMENT '所需的运行时长,默认1小时 (单位: ms)',
    `input_model`       tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否需要输入模型: 0-否，1:是',
    `store_pattern`     tinyint(4) NOT NULL DEFAULT '1' COMMENT '输出存储形式: 1-明文，2:密文',
    `data_rows_flag`         tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否判断数据行数: 0-否，1-是',
    `data_columns_flag`      tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否判断数据列数: 0-否，1-是',
    `public_Flag`       tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否是公有算法: 0-否，1-是',
    `status`            tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1-有效',
    `create_time`       timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ALG_NAME` (`algorithm_name`)
) ENGINE = InnoDB COMMENT ='算法表';

INSERT INTO `t_algorithm` VALUES (1, 'LogisticRegression', 1, '逻辑回归训练',  'Logistic Regression Training', '用于跨组织逻辑回归训练', 'Used for cross-organization logistic regression training', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '180000', '0', '1', '1', '0', '1', '1', '2021-10-25 15:16:02', '2021-11-12 10:03:52');
INSERT INTO `t_algorithm` VALUES (2, 'LogisticRegression', 2, '逻辑回归预测', 'Logistic Regression Prediction', '用于跨组织逻辑回归预测', 'Used for cross-organization logistic regression prediction', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '180000', '1', '1', '1', '0', '1', '1', '2021-10-25 15:16:02', '2021-11-12 10:03:46');
INSERT INTO `t_algorithm` VALUES (3, 'PrivacySetIntersection', 1, '隐私集合求交（PSI）', 'Privacy Set Intersection (PSI)', '用于跨组织的数据交集查询', 'Used for cross-organization data intersection query', 'Rosetta', '3', '2', 'SQL,Python', 'window,linux,mac', '1', '1073741824', '1', '2', '3145728', '180000', '0', '1', '1', '0', '1', '0', '2021-10-25 16:47:14', '2021-11-12 10:02:38');
INSERT INTO `t_algorithm` VALUES (4, 'PrivacySummation', 1, '隐私求和', 'Privacy Summation', '用于多方参与的隐私数据求和', 'Summation of private data for multi-party participation', 'Rosetta', '3', '2', 'SQL,Python', 'window,linux,mac', '1', '1073741824', '1', '2', '3145728', '180000', '0', '1', '1', '0', '1', '0', '2021-10-25 17:01:40', '2021-11-12 10:02:59');
INSERT INTO `t_algorithm` VALUES (5, 'MissingValueProcessing', 1, '缺失值处理', 'Missing value processing', '用于缺失值处理', 'Used for missing value processing', 'Rosetta', '3', '2', 'SQL,Python', 'window,linux,mac', '2', '1073741824', '1', '2', '3145728', '180000', '0', '1', '1', '0', '1', '0', '2021-10-25 17:03:34', '2021-11-12 10:03:05');
INSERT INTO `t_algorithm` VALUES (6, 'LinearRegression', 1, '线性回归训练', 'Linear Regression Training', '用于跨组织线性回归训练', 'Used for cross-organization linear regression training', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '180000', '0', '1', '1', '0', '1', '1', '2021-11-17 15:00:00', '2021-11-17 13:00:00');
INSERT INTO `t_algorithm` VALUES (7, 'LinearRegression', 2,'线性回归预测', 'Linear Regression Prediction', '用于跨组织线性回归的预测', 'Used for cross-organization linear regression prediction', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '180000', '1', '1', '1', '0', '1', '1', '2021-11-17 15:00:00', '2021-11-17 13:00:00');
INSERT INTO `t_algorithm` VALUES (8, 'DNN', 1, 'DNN训练', 'DNN Training', '用于跨组织DNN训练', 'Used for cross-organization DNN training', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '300000', '0', '1', '1', '0', '1', '1', '2021-11-22 11:00:40', '2021-11-22 11:00:44');
INSERT INTO `t_algorithm` VALUES (9, 'DNN', 2, 'DNN预测', 'DNN Prediction', '用于跨组织DNN预测算法', 'Used for cross-organization DNN prediction', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '300000', '1', '1', '1', '0', '1', '1', '2021-11-22 11:05:49', '2021-11-22 11:05:52');
INSERT INTO `t_algorithm` VALUES (10, 'XGBoost', 1, 'XGBoost训练', 'XGBoost Training', '用于跨组织XGBoost训练', 'Used for cross-organization XGBoost training', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '180000', '0', '1', '1', '0', '1', '1', '2021-11-26 14:50:39', '2021-11-26 15:15:36');
INSERT INTO `t_algorithm` VALUES (11, 'XGBoost', 2, 'XGBoost预测', 'XGBoost Prediction', '用于跨组织XGBoost预测', 'Used for cross-organization XGBoost prediction', 'Rosetta', '3', '2', 'Python', 'window,linux,mac', '3', '1073741824', '1', '2', '3145728', '180000', '1', '1', '1', '0', '1', '1', '2021-11-26 14:51:58', '2021-11-26 14:52:59');


-- 模型
CREATE TABLE `t_model`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模型表ID(自增长)',
    `org_identity_id`   varchar(128) NOT NULL COMMENT '所属组织',
    `name`              varchar(64) DEFAULT NULL COMMENT '名称',
    `meta_data_id`      varchar(128)  DEFAULT NULL COMMENT '元数据id',
    `file_id`           varchar(256)  DEFAULT NULL COMMENT '源文件ID',
    `file_path`         varchar(128)  NOT NULL COMMENT '源文件存放路径',
    `train_task_id`     varchar(256)  NOT NULL COMMENT '训练模型的任务id',
    `train_algorithm_id`    bigint(20) DEFAULT NULL COMMENT '训练模型的算法id',
    `train_user_address`    varchar(64) DEFAULT NULL COMMENT '训练模型的账户',
    `status`            tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态: 0-无效，1-有效',
    `create_time`       timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='模型表';


-- 流程定义
ALTER TABLE `t_workflow`
    DROP COLUMN `node_number`,
    DROP COLUMN `run_status`,
    CHANGE `del_version` `edit_version` BIGINT(11) DEFAULT 1  NULL   COMMENT '编辑版本标识，每次编辑后递增，从1开始';

ALTER TABLE `t_workflow_node`
    DROP COLUMN `next_node_step`,
    DROP COLUMN `run_status`,
    DROP COLUMN `task_id`,
    DROP COLUMN `run_msg`,
    DROP COLUMN `status`,
    ADD COLUMN `workflow_edit_version` INT(11) DEFAULT 1  NOT NULL   COMMENT '工作流版本号' AFTER `workflow_id`,
    ADD COLUMN `sender_identity_id` VARCHAR(128) NOT NULL   COMMENT '任务发启放组织id' AFTER `model_id`;

ALTER TABLE `t_workflow_node_code`
    DROP COLUMN `status`;

ALTER TABLE `t_workflow_node_input`
    DROP COLUMN `sender_flag`,
    DROP COLUMN `status`;

ALTER TABLE `t_workflow_node_output`
    DROP COLUMN `party_id`,
    DROP COLUMN `sender_flag`,
    DROP COLUMN `status`;

ALTER TABLE `t_workflow_node_resource`
    DROP COLUMN `status`;

ALTER TABLE `t_workflow_node_variable`
    DROP COLUMN `status`;


-- 流程运行定义
CREATE TABLE `t_workflow_run_status` (
     `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID(自增长)',
     `workflow_id` BIGINT(20) DEFAULT NULL COMMENT '工作流id',
     `workflow_edit_version` INT(11) NOT NULL DEFAULT '1' COMMENT '工作流版本号',
     `sign` VARCHAR(512) DEFAULT NULL COMMENT '发起任务的账户的签名',
     `step` INT(11) DEFAULT NULL COMMENT '总步骤',
     `cur_step` INT(11) DEFAULT NULL COMMENT '当前步骤',
     `begin_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
     `end_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
     `run_status` TINYINT(4) DEFAULT NULL COMMENT '运行状态: 1-运行中,2-运行成功,3-运行失败',
     `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=INNODB COMMENT='工作流运行状态';


CREATE TABLE `t_workflow_run_task_status` (
     `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID(自增长)',
     `workflow_run_id` BIGINT(20) NOT NULL COMMENT '工作流运行状态ID',
     `workflow_node_id` BIGINT(20) NOT NULL COMMENT '工作流节点配置ID',
     `begin_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
     `end_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
     `run_status` TINYINT(4) DEFAULT NULL COMMENT '运行状态: :0-未开始 1-运行中,2-运行成功,3-运行失败',
     `task_id` VARCHAR(256) DEFAULT NULL COMMENT '任务ID,底层处理完成后返回',
     `run_msg` VARCHAR(256) DEFAULT NULL COMMENT '任务处理结果描述',
     `file_name` VARCHAR(128) NOT NULL COMMENT '任务结果文件的名称',
     `metadata_id` VARCHAR(128) NOT NULL COMMENT '任务结果文件的元数据Id <系统默认生成的元数据>',
     `origin_id` VARCHAR(128) NOT NULL COMMENT '任务结果文件的原始文件Id',
     `file_path` VARCHAR(256) NOT NULL COMMENT '任务结果文件的完整相对路径名',
     `ip` VARCHAR(32) NOT NULL COMMENT '任务结果文件所在的 数据服务内网ip',
     `port` VARCHAR(8) NOT NULL COMMENT '任务结果文件所在的 数据服务内网port',
     `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=INNODB COMMENT='工作流任务运行状态';

DROP TABLE `t_sub_job`;
DROP TABLE `t_sub_job_node`;
DROP TABLE `t_task_result`;
DROP TABLE `t_task_event`;

-- 模板
RENAME TABLE `t_workflow_node_temp` TO `t_workflow_temp_node`;

