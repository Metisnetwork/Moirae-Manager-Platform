USE `datum_platform`;

ALTER TABLE `mo_token`
    ADD COLUMN `type` TINYINT NOT NULL  COMMENT '合约类型: 0-erc20,1-erc721' AFTER `address`,
    ADD COLUMN `meta_data_id` VARCHAR(200) NOT NULL COMMENT '对应元数据ID,hash' AFTER `type`,
    ADD COLUMN `pt_alg_consume` VARCHAR(128)  COMMENT '明文算法消耗量' AFTER `is_add_liquidity`,
    ADD COLUMN `ct_alg_consume` VARCHAR(128)  COMMENT '密文算法消耗量' AFTER `pt_alg_consume`,
    CHANGE `price` `price` VARCHAR(128) NULL COMMENT 'erc20对应LAT的价格',
    CHANGE `is_add_liquidity` `is_add_liquidity` TINYINT DEFAULT 0  NOT NULL COMMENT 'erc20是否添加流动性: 0-否，1-是';

CREATE TABLE `mo_meta_data_marketplace` (
    `meta_data_id` varchar(200)  NOT NULL COMMENT '元数据ID,hash',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`meta_data_id`)
) ENGINE=InnoDB  COMMENT='数据市场可见的元数据';

CREATE TABLE `mo_meta_data_user` (
    `address` varchar(64) NOT NULL COMMENT '用户地址',
    `meta_data_id` varchar(200)  NOT NULL COMMENT '元数据ID,hash',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`meta_data_id`)
) ENGINE=InnoDB  COMMENT='用户可见的元数据';

CREATE TABLE `mo_meta_data_certificate` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID(自增长)',
    `meta_data_id` varchar(200)  NOT NULL COMMENT '元数据ID,hash',
    `type` TINYINT NOT NULL  COMMENT '凭证类型: 0-无属性,1-有属性',
    `token_address` varchar(64) NOT NULL COMMENT '凭证对应合约地址',
    `token_id` varchar(128) NOT NULL COMMENT '有属性凭证对应 token id',
    `is_support_pt_alg` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持明文算法',
    `is_support_ct_alg` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持密文算法',
    `characteristic` varchar(256) DEFAULT NULL COMMENT 'token id 对应特性值，有效期时间戳',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='元数据凭证';

CREATE TABLE `mo_meta_data_certificate_user` (
    `address` varchar(64) NOT NULL COMMENT '用户地址',
    `meta_data_certificate_id` bigint NOT NULL COMMENT '元数据凭证ID',
    `balance` varchar(128) NOT NULL COMMENT '账户余额, ERC20为金额, ERC721时 0-未持有 1-持有',
    `authorize_balance` varchar(128) DEFAULT NULL COMMENT '授权支付助手合约金额, ERC20为金额， ERC721时 0-未授权 1-已授权',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`address`, `meta_data_certificate_id`)
) ENGINE=InnoDB  COMMENT='用户持有的元数据凭证';

ALTER TABLE `dc_meta_data`
    DROP COLUMN `token_address`,
    ADD COLUMN `is_support_pt_alg` TINYINT DEFAULT 0  NOT NULL  COMMENT '是否支持明文算法' AFTER `has_title`,
    ADD COLUMN `is_support_ct_alg` TINYINT DEFAULT 0  NOT NULL  COMMENT '是否支持密文算法' AFTER `is_support_pt_alg`,
    ADD COLUMN `owner_address` VARCHAR(64) NOT NULL   COMMENT '数据拥有者地址' AFTER `is_support_ct_alg`,
    ADD COLUMN `erc20_address` VARCHAR(64) NULL   COMMENT '数据对应erc20合约地址' AFTER `owner_address`,
    ADD COLUMN `erc721_address` VARCHAR(64) NULL   COMMENT '数据对应erc721合约地址' AFTER `erc20_address`,
    ADD COLUMN `erc20_pt_alg_consume` VARCHAR(128)  COMMENT '明文算法消耗量' AFTER `erc721_address`,
    ADD COLUMN `erc20_ct_alg_consume` VARCHAR(128)  COMMENT '密文算法消耗量' AFTER `erc20_pt_alg_consume`;

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

