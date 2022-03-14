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


DROP TABLE `t_user_org`;
DROP TABLE `t_organization`;
DROP TABLE `t_job`;
DROP TABLE `t_meta_data`;
DROP TABLE `t_meta_data_details`;
DROP TABLE `t_login_log`;
DROP TABLE `t_user_meta_data`;

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

ALTER TABLE `t_project`
    DROP INDEX `UK_NAME`,
    ADD  UNIQUE INDEX `UK_NAME` (`user_id`, `project_name`, `status`, `del_version`);

ALTER TABLE `t_workflow`
    DROP INDEX `UK_FLOW_NAME`;

