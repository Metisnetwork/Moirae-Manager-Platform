USE `db_moirae`;

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

ALTER TABLE `t_user` ADD COLUMN `org_identity_id` VARCHAR(128) NULL   COMMENT '默认连接的组织id' AFTER `status`;


