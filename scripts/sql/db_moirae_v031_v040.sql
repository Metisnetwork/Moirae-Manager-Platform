USE `db_moirae`;

DROP TABLE `dc_meta_data_auth`;

ALTER TABLE `dc_meta_data`
    ADD COLUMN `token_address` VARCHAR(100) NULL   COMMENT 'token合约的地址' AFTER `update_at`;

ALTER TABLE `dc_org`
    ADD COLUMN `opt_address` VARCHAR(100) NULL   COMMENT '组织操作钱包地址' AFTER `update_at`;

ALTER TABLE `dc_meta_data_column`
    DROP COLUMN `create_time`,
    DROP COLUMN `update_time`;

DROP TABLE `t_project`;

DROP TABLE `t_project_member`;

DROP TABLE `t_project_temp`;

RENAME TABLE `t_user` TO `mo_user`;
ALTER TABLE `mo_user`
    DROP COLUMN `id`,
    CHANGE `address` `address` VARCHAR(64) CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL   COMMENT '用户钱包地址'  FIRST,
    DROP INDEX `UK_ADDRESS`,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`address`);

DROP TABLE IF EXISTS `mo_user_login`;
CREATE TABLE `mo_user_login` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志表id(自增长)',
    `address` varchar(64) NOT NULL COMMENT '登录地址',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '登录状态: 0-登录失败, 1-登录成功',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='用户登录日志表';







