USE `datum_platform`;

ALTER TABLE `mo_token`
    ADD COLUMN `type` TINYINT NOT NULL  COMMENT '合约类型: 0-erc20,1-erc721' AFTER `address`,
    ADD COLUMN `meta_data_id` VARCHAR(200) NOT NULL COMMENT '对应元数据ID,hash' AFTER `type`,
    CHANGE `price` `price` VARCHAR(128) NULL COMMENT 'erc20对应LAT的价格',
    CHANGE `is_add_liquidity` `is_add_liquidity` TINYINT DEFAULT 0  NOT NULL COMMENT 'erc20是否添加流动性: 0-否，1-是';

CREATE TABLE `mo_token_inventory` (
    `token_address` varchar(64) NOT NULL COMMENT '合约地址',
    `token_id` varchar(128) NOT NULL COMMENT 'token id',
    `owner` varchar(64) DEFAULT NULL COMMENT 'token id 对应持有者地址',
    `meta_data_id` VARCHAR(200) NOT NULL COMMENT '对应元数据ID,hash',
    `characteristic` varchar(256) DEFAULT NULL COMMENT 'token id 对应特性值，如有效期',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`token_address`, `token_id`)
)  ENGINE=InnoDB  COMMENT='tokenId持有者信息';

ALTER TABLE `dc_meta_data`
    DROP COLUMN `token_address`,
    ADD COLUMN `is_support_pt_alg` TINYINT DEFAULT 0  NOT NULL  COMMENT '是否支持明文算法' AFTER `has_title`,
    ADD COLUMN `is_support_ct_alg` TINYINT DEFAULT 0  NOT NULL  COMMENT '是否支持密文算法' AFTER `is_support_pt_alg`,
    ADD COLUMN `owner_address` VARCHAR(64) NOT NULL   COMMENT '数据拥有者地址' AFTER `is_support_ct_alg`;

ALTER TABLE `mo_algorithm`
    ADD COLUMN `type` TINYINT DEFAULT 0  NOT NULL  COMMENT '算法类别：0-密文算法，1-明文算法' AFTER `data_columns_flag`;

CREATE TABLE `mo_workflow_run_status_payment_method` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID(自增长)',
    `workflow_run_id` bigint NOT NULL COMMENT '工作流运行状态ID',
    `meta_data_id` VARCHAR(200) NOT NULL COMMENT '对应元数据ID,hash',
    `type` TINYINT NOT NULL  COMMENT '合约类型: 0-erc20,1-erc721',
    `token_address` varchar(64) NOT NULL COMMENT '合约地址',
    `token_id` varchar(128) NOT NULL COMMENT '721下合约token id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='工作流运行状态对应支付方式';

RENAME TABLE `mo_workflow_run_task_status` TO `mo_workflow_run_status_task`;

ALTER TABLE `mo_workflow_task`
    ADD COLUMN `power_type` INT DEFAULT 0  NOT NULL COMMENT '算力提供方式 0-随机 1-指定' AFTER `input_psi_step`,
    ADD COLUMN `power_identity_id` VARCHAR(128) NULL COMMENT '算力提供组织' AFTER `power_type`;

