---------------------- 已执行 ----------------------
ALTER TABLE erp_customer_consign_info add `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态：0未提交；1初审通过；2终审通过';
ALTER TABLE erp_customer_consign_info ADD COLUMN `is_business_address` int(11) NOT NULL DEFAULT '0' COMMENT '是否为经营地址，0否1是';
ALTER TABLE erp_workflow_link ADD COLUMN `verify_user_group_id` int(20) COMMENT '审核人组ID，审核人为空时，该字段有值';
ALTER TABLE erp_customer_company ADD COLUMN `simple_company_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '简单公司名称';
ALTER TABLE erp_workflow_link_detail ADD COLUMN `verify_user_group_id` int(20) DEFAULT NULL COMMENT '审核人组ID，审核人为空时，该字段有值';
INSERT INTO `erp_workflow_template` (`workflow_name`, `workflow_desc`, `workflow_type`, `data_status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES ('客户审批流程', '客户审批流程', '16', '1', NULL, '2018-02-05 15:11:43', '500001', '2018-02-05 15:11:43', '500001');
INSERT INTO `erp_workflow_node` (`workflow_node_name`, `workflow_template_id`, `workflow_step`, `workflow_previous_node_id`, `workflow_next_node_id`, `workflow_department_type`, `workflow_department`, `workflow_role`, `workflow_user`, `data_status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `workflow_role_type`) VALUES ('分公司总经理审批客户', '16', '1', NULL, '20', NULL, NULL, NULL, NULL, '1', NULL, '2018-01-01 09:50:07', '500001', '2018-01-01 09:50:07', '500001', '100004');
INSERT INTO `erp_workflow_node` (`workflow_node_name`, `workflow_template_id`, `workflow_step`, `workflow_previous_node_id`, `workflow_next_node_id`, `workflow_department_type`, `workflow_department`, `workflow_role`, `workflow_user`, `data_status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `workflow_role_type`) VALUES ('总公司风控审批客户', '16', '2', '19', NULL, '300013', '400017', NULL, NULL, '1', NULL, '2018-01-01 09:50:07', '500001', '2018-01-01 09:50:07', '500001', NULL);
UPDATE `erp_role` SET role_type= 100004 WHERE role_name LIKE ('%分公司总经理%');
DROP TABLE if exists `erp_sub_company_city_cover`;
CREATE TABLE `erp_sub_company_city_cover` ;
DROP TABLE if exists `erp_workflow_verify_user_group`;
CREATE TABLE `erp_workflow_verify_user_group` ;
DROP TABLE if exists `erp_customer_risk_management_history`;
CREATE TABLE `erp_customer_risk_management_history` ;

ALTER TABLE erp_customer_company add `address_verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '公司经营地址审核状态：0未提交；1.已提交 2.初审通过；3.终审通过';
ALTER TABLE erp_order add `is_peer` int(11) NOT NULL DEFAULT '0' COMMENT '是否同行调拨，0-否，1是';
ALTER TABLE erp_customer_consign_info add `workflow_type` int(11) NOT NULL DEFAULT '0' COMMENT '工作流类型';
ALTER TABLE erp_order add `statement_date` int(20) COMMENT '结算时间（天），20和31两种情况，如果为空取系统设定';
ALTER TABLE erp_order add `is_k3_order` int(20)  NOT NULL DEFAULT '0' COMMENT '是否为K3订单，1是0否';
ALTER TABLE erp_k3_return_order_detail add `real_product_count` int(11) NOT NULL DEFAULT 0 COMMENT '实际退货数量';

ALTER TABLE erp_order_product add `renting_product_count` int(11) NOT NULL DEFAULT 0 COMMENT '在租商品总数';
ALTER TABLE erp_order_material add `renting_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '在租配件总说';

ALTER TABLE erp_statement_order add `statement_coupon_amount` decimal(15,5) DEFAULT 0 COMMENT '结算单优惠券优惠总和';
ALTER TABLE erp_statement_order_detail add `statement_coupon_amount` decimal(15,5) DEFAULT 0 COMMENT '结算单优惠券优惠总和';
ALTER TABLE erp_k3_return_order add `delivery_sub_company_id` int(20) NOT NULL COMMENT '发货分公司';
ALTER TABLE erp_order add `order_message` text CHARACTER SET utf8 COMMENT '订单消息[JSON格式，userId,userRealName,createTime,content]';

ALTER TABLE erp_order_product add `FEntry_id` int(11) NOT NULL DEFAULT 0 COMMENT '行数';
ALTER TABLE erp_order_product add `product_number` varchar(64) COMMENT '商品编码';

ALTER TABLE erp_order_material add `FEntry_id` int(11) NOT NULL DEFAULT 0 COMMENT '行数';
ALTER TABLE erp_order_material add `product_number` varchar(64) COMMENT '商品编码';

DROP TABLE if exists `erp_switch`;
CREATE TABLE `erp_switch` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `interface_url` varchar(100) NOT NULL COMMENT '接口URL',
  `is_open` int(11) NOT NULL DEFAULT 1 COMMENT '是否开启，0-否，1-是',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=500001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理功能开关表';



---------------------- 未执行 ----------------------

DROP TABLE if exists `erp_coupon_batch`;
CREATE TABLE `erp_coupon_batch` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `coupon_batch_name` varchar(100) NOT NULL COMMENT '批次名称',
  `coupon_batch_describe` varchar(1000) NOT NULL  DEFAULT '' COMMENT '批次描述',
  `coupon_type` int(11) NOT NULL COMMENT '优惠券类型，1-设备优惠券',
  `effective_start_time` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `coupon_batch_total_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券总数',
  `coupon_batch_used_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已使用总数',
  `total_face_amount` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券总面值',
  `total_used_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '已使用总面值',
  `total_deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣总金额',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券批次表';

DROP TABLE if exists `erp_coupon_batch_detail`;
CREATE TABLE `erp_coupon_batch_detail` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `coupon_batch_id` int(20) NOT NULL COMMENT '批次ID',
  `coupon_total_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券总数',
  `coupon_received_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已领取总数',
  `coupon_used_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已使用总数',
  `coupon_cancel_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券作废总数',
  `face_value` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券面值',
  `total_face_amount` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券总面值',
  `total_used_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '已使用总面值',
  `total_deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣总金额',
  `is_online` int(11) NOT NULL COMMENT '是否线上，0-否，1-是',
  `effective_start_time` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券批次详情表';

DROP TABLE if exists `erp_coupon`;
CREATE TABLE `erp_coupon` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `coupon_batch_id` int(20) NOT NULL COMMENT '批次ID',
  `coupon_batch_detail_id` int(20) NOT NULL COMMENT '批次详情ID',
  `face_value` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券面值',
  `coupon_code` varchar(10) NOT NULL COMMENT '优惠券编号：规则LX+8位大写字母数字组合(不要O和0)',
  `deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣金额',
  `coupon_status` int(11) NOT NULL DEFAULT 0  COMMENT '优惠券状态，0-未领取，4-可用，8-已用，12-作废',
  `customer_no` varchar(100) COMMENT '客戶编号',
  `is_online` int(11) NOT NULL COMMENT '是否线上，0-否，1-是',
  `order_id` int(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_product_id` int(20) DEFAULT NULL COMMENT '订单商品项ID',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `effective_start_time` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_customer_no ( `customer_no` ),
  INDEX index_order_id ( `order_id` ),
  INDEX index_order_no ( `order_no` ),
  INDEX index_order_product_id ( `order_product_id` )
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券表';

DROP TABLE if exists `erp_sms_log`;
CREATE TABLE `erp_sms_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `out_id` varchar(50)  COMMENT '外部流水扩展字段',
  `request_id` varchar(50) COMMENT '请求ID',
  `template_code` varchar(50) NOT NULL COMMENT '短信模板ID',
  `phone` varchar(24) NOT NULL COMMENT '手机号',
  `refer_type` int(11) COMMENT '引用类型（如客户，用户等）',
  `refer_id` int(20) COMMENT '发送目标（如客户，用户等）引用ID',
  `refer_name` varchar(200) COMMENT '发送目标（如客户，用户等）名称',
  `send_type` int(11) NOT NULL DEFAULT 0 COMMENT '发送类型：0-注册，1-登录，2-找回密码，3-交易支付，4-推广，5-其他',
  `send_status` int(11) NOT NULL DEFAULT 0 COMMENT '发送状态：0-发送失败，1-发送成功',
  `request_json` varchar(2000) COMMENT '请求JSON',
  `response_json` varchar(2000) COMMENT '返回JSON',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='短信发送日志表';

ALTER TABLE erp_workflow_link MODIFY  `verify_user_group_id` VARCHAR(100) COMMENT '审核人组UUID，审核人为空时，该字段有值';
ALTER TABLE erp_workflow_verify_user_group MODIFY  `verify_user_group_id` VARCHAR(100) COMMENT '审核人组UUID';
ALTER TABLE erp_workflow_link_detail MODIFY  `verify_user_group_id` varchar(100) DEFAULT NULL COMMENT '审核人组UUID，审核人为空时，该字段有值';
ALTER TABLE erp_k3_return_order ADD COLUMN `return_reason_type` int(11) NOT NULL COMMENT '退还原因类型：1-客户方设备不愿或无法退还，2-期满正常收回，3-提前退租，4-未按时付款或风险等原因上门收回，5-设备故障等我方原因导致退货，6-主观因素等客户方原因导致退货，7-更换设备，8-公司倒闭，9-设备闲置，10-满三个月或六个月随租随还，11-其它';

DROP TABLE IF EXISTS `erp_version_history`;
CREATE TABLE `erp_version_history` (
   `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `versoin_no` VARCHAR(100) NOT NULL COMMENT '版本编号',
  `versoin_summary` VARCHAR(120) DEFAULT "" COMMENT '版本摘要',
  `online_time` DATETIME COMMENT '实际上线时间',
  `expect_online_start_time` DATETIME COMMENT '预计上线开始时间',
  `expect_online_end_time` DATETIME COMMENT '预计上线结束时间',
  `online_status` INT(11) NOT NULL DEFAULT '0' COMMENT '上线状态，0-未上线，1-上线中，2-已上线，3-取消上线',
  `is_use` INT(11) NOT NULL DEFAULT '0' COMMENT '是否为当前使用的版本，1是，0否',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本历史表';



DROP TABLE IF EXISTS `erp_version_history_detail`;
CREATE TABLE `erp_version_history_detail` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `versoin_history_id` INT(20) NOT NULL COMMENT '版本历史表',
  `versoin_history_no` VARCHAR(100) NOT NULL COMMENT '版本历史编号',
  `versoin_describe` VARCHAR(150) NOT NULL COMMENT '变更描述',
  `detail_status` INT(11) NOT NULL DEFAULT '0' COMMENT '上线状态，0-未上线，1-上线中，2-已上线，3-取消上线',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_versoin_history_id ( `versoin_history_id` ),
  INDEX index_versoin_history_no ( `versoin_history_no` )
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本历史明细表';

DROP TABLE IF EXISTS `erp_version_history_user`;
CREATE TABLE `erp_version_history_user` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` INT(20) NOT NULL COMMENT '用户ID',
  `versoin_history_id` INT(20) NOT NULL COMMENT '版本历史ID',
  `versoin_history_no` VARCHAR(100) NOT NULL COMMENT '版本历史编号',
  `before_online_send` INT(20) NOT NULL COMMENT '上线前是否推送，0-否，1-是',
  `after_online_send` INT(20) NOT NULL COMMENT '上线后是否推送，0-否，1-是',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_user_id ( `user_id` ),
  INDEX index_versoin_history_id ( `versoin_history_id` ),
  INDEX index_versoin_history_no ( `versoin_history_no` )
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本用户推送记录表';





DROP TABLE IF EXISTS `mall_verify_code`;
CREATE TABLE `mall_verify_code` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `phone` VARCHAR(24) NOT NULL COMMENT '手机号',
  `verify_code` VARCHAR(8) NOT NULL COMMENT '验证码',
  `is_used` INT(11) NOT NULL DEFAULT '0' COMMENT '是否使用：0-未使用，1-已使用',
  `code_type` INT(11) NOT NULL DEFAULT '99' COMMENT '验证码类型，1-绑定ERP用户验证码，99-其他',
  `overdue_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='短信验证码表';

ALTER TABLE erp_bank_slip add `localization_count` int(11) COMMENT '属地化数量[总公司时有值]';
ALTER TABLE erp_bank_slip_detail add `sub_company_id` int(11) NOT NULL COMMENT '分公司ID';
ALTER TABLE erp_bank_slip_detail add `is_localization` int(11) COMMENT '是否已属地化,0-否，1-是[总公司时有值]';
ALTER TABLE erp_coupon add `statement_order_id` int(20) COMMENT '结算单ID';
ALTER TABLE erp_coupon add `statement_order_no` varchar(100) COMMENT '结算单编码';
ALTER TABLE erp_coupon add `statement_order_detail_id` int(20) COMMENT '结算单详情ID';
ALTER TABLE erp_customer add `is_risk` int(4) NOT NULL DEFAULT '0' COMMENT '是否授信，0-未授信；1-已授信';
ALTER TABLE erp_customer add `is_risk` int(4) NOT NULL DEFAULT '0' COMMENT '是否授信，0-未授信；1-已授信';

ALTER TABLE erp_statement_order add `statement_penalty_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单违约金';
ALTER TABLE erp_statement_order add `statement_penalty_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已结算的结算单违约金';
ALTER TABLE erp_statement_order_detail add `statement_detail_penalty_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单违约金';
ALTER TABLE erp_statement_order_detail add `statement_detail_penalty_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已结算的结算单违约金';
ALTER TABLE erp_order add `cancel_order_reason_type` int(11) COMMENT '取消订单原因类型，1-下错单，2-变更数量，3-变更单价，4-变更配件，5-变更结算日，6-变更支付方式，7-变更时间/租期，8-变更型号/配置，9-变更收货人信息，10-同行调货选错，12-设备故障换货，13-客户名称错误，14-客户取消订单，15-缺货取消，16-实际出货与订单不符';

---------------------- 已执行 ----------------------
ALTER TABLE erp_customer_consign_info add `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态：0未提交；1初审通过；2终审通过';
ALTER TABLE erp_customer_consign_info ADD COLUMN `is_business_address` int(11) NOT NULL DEFAULT '0' COMMENT '是否为经营地址，0否1是';
ALTER TABLE erp_workflow_link ADD COLUMN `verify_user_group_id` int(20) COMMENT '审核人组ID，审核人为空时，该字段有值';
ALTER TABLE erp_customer_company ADD COLUMN `simple_company_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '简单公司名称';
ALTER TABLE erp_workflow_link_detail ADD COLUMN `verify_user_group_id` int(20) DEFAULT NULL COMMENT '审核人组ID，审核人为空时，该字段有值';
INSERT INTO `erp_workflow_template` (`workflow_name`, `workflow_desc`, `workflow_type`, `data_status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES ('客户审批流程', '客户审批流程', '16', '1', NULL, '2018-02-05 15:11:43', '500001', '2018-02-05 15:11:43', '500001');
INSERT INTO `erp_workflow_node` (`workflow_node_name`, `workflow_template_id`, `workflow_step`, `workflow_previous_node_id`, `workflow_next_node_id`, `workflow_department_type`, `workflow_department`, `workflow_role`, `workflow_user`, `data_status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `workflow_role_type`) VALUES ('分公司总经理审批客户', '16', '1', NULL, '20', NULL, NULL, NULL, NULL, '1', NULL, '2018-01-01 09:50:07', '500001', '2018-01-01 09:50:07', '500001', '100004');
INSERT INTO `erp_workflow_node` (`workflow_node_name`, `workflow_template_id`, `workflow_step`, `workflow_previous_node_id`, `workflow_next_node_id`, `workflow_department_type`, `workflow_department`, `workflow_role`, `workflow_user`, `data_status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `workflow_role_type`) VALUES ('总公司风控审批客户', '16', '2', '19', NULL, '300013', '400017', NULL, NULL, '1', NULL, '2018-01-01 09:50:07', '500001', '2018-01-01 09:50:07', '500001', NULL);
UPDATE `erp_role` SET role_type= 100004 WHERE role_name LIKE ('%分公司总经理%');
DROP TABLE if exists `erp_sub_company_city_cover`;
CREATE TABLE `erp_sub_company_city_cover` ;
DROP TABLE if exists `erp_workflow_verify_user_group`;
CREATE TABLE `erp_workflow_verify_user_group` ;
DROP TABLE if exists `erp_customer_risk_management_history`;
CREATE TABLE `erp_customer_risk_management_history` ;

ALTER TABLE erp_customer_company add `address_verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '公司经营地址审核状态：0未提交；1.已提交 2.初审通过；3.终审通过';
ALTER TABLE erp_order add `is_peer` int(11) NOT NULL DEFAULT '0' COMMENT '是否同行调拨，0-否，1是';
ALTER TABLE erp_customer_consign_info add `workflow_type` int(11) NOT NULL DEFAULT '0' COMMENT '工作流类型';
ALTER TABLE erp_order add `statement_date` int(20) COMMENT '结算时间（天），20和31两种情况，如果为空取系统设定';
ALTER TABLE erp_order add `is_k3_order` int(20)  NOT NULL DEFAULT '0' COMMENT '是否为K3订单，1是0否';
ALTER TABLE erp_k3_return_order_detail add `real_product_count` int(11) NOT NULL DEFAULT 0 COMMENT '实际退货数量';

ALTER TABLE erp_order_product add `renting_product_count` int(11) NOT NULL DEFAULT 0 COMMENT '在租商品总数';
ALTER TABLE erp_order_material add `renting_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '在租配件总说';

ALTER TABLE erp_statement_order add `statement_coupon_amount` decimal(15,5) DEFAULT 0 COMMENT '结算单优惠券优惠总和';
ALTER TABLE erp_statement_order_detail add `statement_coupon_amount` decimal(15,5) DEFAULT 0 COMMENT '结算单优惠券优惠总和';
ALTER TABLE erp_k3_return_order add `delivery_sub_company_id` int(20) NOT NULL COMMENT '发货分公司';
ALTER TABLE erp_order add `order_message` text CHARACTER SET utf8 COMMENT '订单消息[JSON格式，userId,userRealName,createTime,content]';

ALTER TABLE erp_order_product add `FEntry_id` int(11) NOT NULL DEFAULT 0 COMMENT '行数';
ALTER TABLE erp_order_product add `product_number` varchar(64) COMMENT '商品编码';

ALTER TABLE erp_order_material add `FEntry_id` int(11) NOT NULL DEFAULT 0 COMMENT '行数';
ALTER TABLE erp_order_material add `product_number` varchar(64) COMMENT '商品编码';

DROP TABLE if exists `erp_switch`;
CREATE TABLE `erp_switch` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `interface_url` varchar(100) NOT NULL COMMENT '接口URL',
  `is_open` int(11) NOT NULL DEFAULT 1 COMMENT '是否开启，0-否，1-是',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=500001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理功能开关表';



---------------------- 未执行 ----------------------

DROP TABLE if exists `erp_coupon_batch`;
CREATE TABLE `erp_coupon_batch` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `coupon_batch_name` varchar(100) NOT NULL COMMENT '批次名称',
  `coupon_batch_describe` varchar(1000) NOT NULL  DEFAULT '' COMMENT '批次描述',
  `coupon_type` int(11) NOT NULL COMMENT '优惠券类型，1-设备优惠券',
  `effective_start_time` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `coupon_batch_total_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券总数',
  `coupon_batch_used_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已使用总数',
  `total_face_amount` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券总面值',
  `total_used_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '已使用总面值',
  `total_deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣总金额',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券批次表';

DROP TABLE if exists `erp_coupon_batch_detail`;
CREATE TABLE `erp_coupon_batch_detail` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `coupon_batch_id` int(20) NOT NULL COMMENT '批次ID',
  `coupon_total_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券总数',
  `coupon_received_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已领取总数',
  `coupon_used_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已使用总数',
  `coupon_cancel_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券作废总数',
  `face_value` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券面值',
  `total_face_amount` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券总面值',
  `total_used_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '已使用总面值',
  `total_deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣总金额',
  `is_online` int(11) NOT NULL COMMENT '是否线上，0-否，1-是',
  `effective_start_time` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券批次详情表';

DROP TABLE if exists `erp_coupon`;
CREATE TABLE `erp_coupon` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `coupon_batch_id` int(20) NOT NULL COMMENT '批次ID',
  `coupon_batch_detail_id` int(20) NOT NULL COMMENT '批次详情ID',
  `face_value` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券面值',
  `coupon_code` varchar(10) NOT NULL COMMENT '优惠券编号：规则LX+8位大写字母数字组合(不要O和0)',
  `deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣金额',
  `coupon_status` int(11) NOT NULL DEFAULT 0  COMMENT '优惠券状态，0-未领取，4-可用，8-已用，12-作废',
  `customer_no` varchar(100) COMMENT '客戶编号',
  `is_online` int(11) NOT NULL COMMENT '是否线上，0-否，1-是',
  `order_id` int(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_product_id` int(20) DEFAULT NULL COMMENT '订单商品项ID',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `effective_start_time` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_customer_no ( `customer_no` ),
  INDEX index_order_id ( `order_id` ),
  INDEX index_order_no ( `order_no` ),
  INDEX index_order_product_id ( `order_product_id` )
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券表';

DROP TABLE if exists `erp_sms_log`;
CREATE TABLE `erp_sms_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `out_id` varchar(50)  COMMENT '外部流水扩展字段',
  `request_id` varchar(50) COMMENT '请求ID',
  `template_code` varchar(50) NOT NULL COMMENT '短信模板ID',
  `phone` varchar(24) NOT NULL COMMENT '手机号',
  `refer_type` int(11) COMMENT '引用类型（如客户，用户等）',
  `refer_id` int(20) COMMENT '发送目标（如客户，用户等）引用ID',
  `refer_name` varchar(200) COMMENT '发送目标（如客户，用户等）名称',
  `send_type` int(11) NOT NULL DEFAULT 0 COMMENT '发送类型：0-注册，1-登录，2-找回密码，3-交易支付，4-推广，5-其他',
  `send_status` int(11) NOT NULL DEFAULT 0 COMMENT '发送状态：0-发送失败，1-发送成功',
  `request_json` varchar(2000) COMMENT '请求JSON',
  `response_json` varchar(2000) COMMENT '返回JSON',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='短信发送日志表';

ALTER TABLE erp_workflow_link MODIFY  `verify_user_group_id` VARCHAR(100) COMMENT '审核人组UUID，审核人为空时，该字段有值';
ALTER TABLE erp_workflow_verify_user_group MODIFY  `verify_user_group_id` VARCHAR(100) COMMENT '审核人组UUID';
ALTER TABLE erp_workflow_link_detail MODIFY  `verify_user_group_id` varchar(100) DEFAULT NULL COMMENT '审核人组UUID，审核人为空时，该字段有值';
ALTER TABLE erp_k3_return_order ADD COLUMN `return_reason_type` int(11) NOT NULL COMMENT '退还原因类型：1-客户方设备不愿或无法退还，2-期满正常收回，3-提前退租，4-未按时付款或风险等原因上门收回，5-设备故障等我方原因导致退货，6-主观因素等客户方原因导致退货，7-更换设备，8-公司倒闭，9-设备闲置，10-满三个月或六个月随租随还，11-其它';

DROP TABLE if exists `erp_version_history`
DROP TABLE IF EXISTS `erp_version_history`;
CREATE TABLE `erp_version_history` (
  `id` int(20) NOT NULL ATUO_INCREMENT COMMENT '唯一标识',
  `versoin_no` varchar(100) NOT NULL COMMENT '版本编号',
  `versoin_summary` varchar(120) DEFAULT "" COMMENT '版本摘要',
  `online_time` datetime COMMENT '实际上线时间',
  `expect_online_start_time` datetime COMMENT '预计上线开始时间',
  `expect_online_end_time` datetime COMMENT '预计上线结束时间',
  `online_status` int(11) NOT NULL DEFAULT '0' COMMENT '上线状态，0-未上线，1-上线中，2-已上线，3-取消上线',
  `is_use` int(11) NOT NULL DEFAULT '0' COMMENT '是否为当前使用的版本，1是，0否',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
   `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `versoin_no` VARCHAR(100) NOT NULL COMMENT '版本编号',
  `versoin_summary` VARCHAR(120) DEFAULT "" COMMENT '版本摘要',
  `online_time` DATETIME COMMENT '实际上线时间',
  `expect_online_start_time` DATETIME COMMENT '预计上线开始时间',
  `expect_online_end_time` DATETIME COMMENT '预计上线结束时间',
  `online_status` INT(11) NOT NULL DEFAULT '0' COMMENT '上线状态，0-未上线，1-上线中，2-已上线，3-取消上线',
  `is_use` INT(11) NOT NULL DEFAULT '0' COMMENT '是否为当前使用的版本，1是，0否',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本更改历史表';
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本历史表';

DROP TABLE if exists `erp_version_history_detail`


DROP TABLE IF EXISTS `erp_version_history_detail`;
CREATE TABLE `erp_version_history_detail` (
  `id` int(20) NOT NULL ATUO_INCREMENT COMMENT '唯一标识',
  `versoin_history_id` int(20) NOT NULL COMMENT '版本ID',
  `versoin_history_no` varchar(100) NOT NULL COMMENT '版本编号',
  `versoin_describe` varchar(150) NOT NULL COMMENT '变更描述',
  `detail_status` int(11) NOT NULL DEFAULT '0' COMMENT '上线状态，0-未上线，1-上线中，2-已上线，3-取消上线',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `versoin_history_id` INT(20) NOT NULL COMMENT '版本历史表',
  `versoin_history_no` VARCHAR(100) NOT NULL COMMENT '版本历史编号',
  `versoin_describe` VARCHAR(150) NOT NULL COMMENT '变更描述',
  `detail_status` INT(11) NOT NULL DEFAULT '0' COMMENT '上线状态，0-未上线，1-上线中，2-已上线，3-取消上线',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_versoin_history_id ( `versoin_history_id` ),
  INDEX index_versoin_history_no ( `versoin_history_no` )
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本更改历史明细表';
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本历史明细表';

DROP TABLE if exists `erp_version_history_user`
DROP TABLE IF EXISTS `erp_version_history_user`;
CREATE TABLE `erp_version_history_user` (
  `id` int(20) NOT NULL ATUO_INCREMENT COMMENT '唯一标识',
  `user_id` int(20) NOT NULL COMMENT '用户ID',
  `versoin_history_id` int(20) NOT NULL COMMENT '版本ID',
  `versoin_history_no` varchar(100) NOT NULL COMMENT '版本编号',
  `before_online_is_send` int(20) NOT NULL COMMENT '上线前是否推送，0-否，1-是',
  `after_online_is_send` int(20) NOT NULL COMMENT '上线后是否推送，0-否，1-是',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` INT(20) NOT NULL COMMENT '用户ID',
  `versoin_history_id` INT(20) NOT NULL COMMENT '版本历史ID',
  `versoin_history_no` VARCHAR(100) NOT NULL COMMENT '版本历史编号',
  `before_online_send` INT(20) NOT NULL COMMENT '上线前是否推送，0-否，1-是',
  `after_online_send` INT(20) NOT NULL COMMENT '上线后是否推送，0-否，1-是',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_user_id ( `user_id` ),
  INDEX index_versoin_history_id ( `versoin_history_id` ),
  INDEX index_versoin_history_no ( `versoin_history_no` )
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本变更推送记录表';
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本用户推送记录表';





DROP TABLE IF EXISTS `mall_verify_code`;
CREATE TABLE `mall_verify_code` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `phone` VARCHAR(24) NOT NULL COMMENT '手机号',
  `verify_code` VARCHAR(8) NOT NULL COMMENT '验证码',
  `is_used` INT(11) NOT NULL DEFAULT '0' COMMENT '是否使用：0-未使用，1-已使用',
  `code_type` INT(11) NOT NULL DEFAULT '99' COMMENT '验证码类型，1-绑定ERP用户验证码，99-其他',
  `overdue_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='短信验证码表';

ALTER TABLE erp_bank_slip add `localization_count` int(11) COMMENT '属地化数量[总公司时有值]';
ALTER TABLE erp_bank_slip_detail add `sub_company_id` int(11) NOT NULL COMMENT '分公司ID';
ALTER TABLE erp_bank_slip_detail add `is_localization` int(11) COMMENT '是否已属地化,0-否，1-是[总公司时有值]';
ALTER TABLE erp_statement_order add `statement_coupon_amount` decimal(15,5) DEFAULT 0 COMMENT '结算单优惠券优惠总和';
ALTER TABLE erp_statement_order_detail add `statement_coupon_amount` decimal(15,5) DEFAULT 0 COMMENT '结算单优惠券优惠总和';
ALTER TABLE erp_coupon add `statement_order_id` int(20) COMMENT '结算单ID';
ALTER TABLE erp_coupon add `statement_order_no` varchar(100) COMMENT '结算单编码';
ALTER TABLE erp_coupon add `statement_order_detail_id` int(20) COMMENT '结算单详情ID';
ALTER TABLE erp_k3_return_order add `delivery_sub_company_id` int(20) NOT NULL COMMENT '发货分公司';
ALTER TABLE erp_customer add `is_risk` int(4) NOT NULL DEFAULT '0' COMMENT '是否授信，0-未授信；1-已授信';
ALTER TABLE erp_customer add `is_risk` int(4) NOT NULL DEFAULT '0' COMMENT '是否授信，0-未授信；1-已授信';

ALTER TABLE erp_statement_order add `statement_penalty_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单违约金';
ALTER TABLE erp_statement_order add `statement_penalty_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已结算的结算单违约金';
ALTER TABLE erp_statement_order_detail add `statement_detail_penalty_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单违约金';
ALTER TABLE erp_statement_order_detail add `statement_detail_penalty_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已结算的结算单违约金';
ALTER TABLE erp_order add `cancel_order_reason_type` int(11) COMMENT '取消订单原因类型，1-下错单，2-变更数量，3-变更单价，4-变更配件，5-变更结算日，6-变更支付方式，7-变更时间/租期，8-变更型号/配置，9-变更收货人信息，10-同行调货选错，12-设备故障换货，13-客户名称错误，14-客户取消订单，15-缺货取消，16-实际出货与订单不符';



--  钉钉功能脚本
alter table erp_user add dingding_user_id varchar(31) COMMENT "钉钉用户id" AFTER last_login_ip; #
-- erp_workflow_template表新增映射钉钉模板代码字段
alter table erp_workflow_template add dingding_process_code varchar(127) COMMENT "钉钉模板代码"; #
-- erp_department表新增映射钉钉的部门编号字段
alter table erp_department add dingding_dept_id varchar(31) COMMENT "钉钉部门编号"; #
-- erp_workflow_link表新增钉钉的工作流编号字段
alter table erp_workflow_link add dingding_workflow_id varchar(63) COMMENT "钉钉工作流id"; #

CREATE TABLE `erp_workflow_template_dingding` (
`id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
`dingding_process_code` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '钉钉工作流模板代码',
`name` varchar(31) COLLATE utf8_bin NOT NULL COMMENT '模版表单名称',
`name_index` int(11) NOT NULL DEFAULT '1' COMMENT '钉钉模板表单元素名所在的位置，从上到下依次为1,2,3以此类推',
`data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
`remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
`create_time` datetime DEFAULT NULL COMMENT '添加时间',
`create_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '添加人',
`update_time` datetime DEFAULT NULL COMMENT '修改时间',
`update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='钉钉工作流模板表';



DROP TABLE IF EXISTS `erp_relet_order`;
CREATE TABLE `erp_relet_order` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `relet_order_no` VARCHAR(100) NOT NULL COMMENT '续租订单编号',
  `order_id` INT(20) NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(100) NOT NULL COMMENT '订单编号',
  `buyer_customer_id` INT(20) NOT NULL COMMENT '购买人ID',
  `buyer_customer_no` VARCHAR(100) NOT NULL COMMENT '购买人编号',
  `buyer_customer_name` VARCHAR(64) NOT NULL COMMENT '客户名称',
  `order_sub_company_id` INT(20) DEFAULT NULL COMMENT '订单所属分公司',
  `delivery_sub_company_id` INT(20) NOT NULL COMMENT '订单发货分公司',
   `rent_type` INT(20) NOT NULL COMMENT '租赁类型',
   `rent_time_length` INT(20) NOT NULL COMMENT '租赁时长',
  `rent_length_type` INT(20) NOT NULL COMMENT '租赁时长类型',
  `rent_start_time` DATETIME NOT NULL COMMENT '起租时间',
  `total_product_count` INT(11) DEFAULT 0 COMMENT '商品总数',
  `total_product_amount` DECIMAL(15,5) DEFAULT 0 COMMENT '商品总价',
  `total_material_count` INT(11) DEFAULT 0 COMMENT '配件总数',
  `total_material_amount` DECIMAL(15,5) DEFAULT 0 COMMENT '配件总价',
  `total_order_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '订单总价，实际支付价格，商品金额+配件金额-优惠(无运费，区别与订单)',
  `total_paid_order_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '已经支付金额',
  `order_seller_id` INT(20) NOT NULL COMMENT '订单销售员',
  `order_union_seller_id` INT(20) COMMENT '订单联合销售员',
  `total_discount_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '共计优惠金额',
  `relet_order_status` INT(11) NOT NULL DEFAULT '0' COMMENT '订单状态，0-待提交,4-审核中,8-续租中,12-部分归还,16-全部归还,20-取消,24-结束',
  `pay_status` INT(11) NOT NULL DEFAULT '0' COMMENT '支付状态，0未支付，1已支付，2已退款,3逾期中',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `expect_return_time` DATETIME DEFAULT NULL COMMENT '预计归还时间',
  `actual_return_time` DATETIME DEFAULT NULL COMMENT '实际归还时间，最后一件设备归还的时间',
  `high_tax_rate` INT(11) NOT NULL DEFAULT 0 COMMENT '17%税率',
  `low_tax_rate` INT(11) NOT NULL DEFAULT 0 COMMENT '6%税率',
  `tax_rate` DOUBLE NOT NULL DEFAULT 0 COMMENT '税率',
  `statement_date` INT(20) COMMENT '结算时间（天），20和31两种情况，如果为空取系统设定',
  `buyer_remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '购买人备注',
  `product_summary` VARCHAR(500)  CHARACTER SET utf8 DEFAULT NULL COMMENT '商品摘要',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `owner` INT(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_relet_order_no` (`relet_order_no`),
  INDEX index_order_id ( `order_id` ),
  INDEX index_order_no ( `order_no` ),
  INDEX index_buyer_customer_id ( `buyer_customer_id` ),
  INDEX index_buyer_customer_no ( `buyer_customer_no` ),
  INDEX index_order_sub_company_id ( `order_sub_company_id` ),
  INDEX index_delivery_sub_company_id ( `delivery_sub_company_id` ),
  INDEX index_order_seller_id ( `order_seller_id` ),
  INDEX index_order_union_seller_id ( `order_union_seller_id` )
) ENGINE=INNODB AUTO_INCREMENT=3000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='续租订单表';


DROP TABLE IF EXISTS `erp_relet_order_product`;
CREATE TABLE `erp_relet_order_product` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `relet_order_id` INT(20) NOT NULL COMMENT '续租订单ID',
  `relet_order_no` VARCHAR(100) NOT NULL COMMENT '续租订单编号',
  `order_id` INT(20) NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(100) NOT NULL COMMENT '订单编号',
  `order_product_id` INT(20) NOT NULL COMMENT '订单商品项ID',
  `product_id` INT(20) COMMENT '商品ID',
  `product_name` VARCHAR(100) COLLATE utf8_bin COMMENT '商品名称',
  `product_sku_id` INT(20) COMMENT '商品SKU ID',
  `product_sku_name` VARCHAR(100) COLLATE utf8_bin COMMENT '商品SKU名称',
  `product_count` INT(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `product_unit_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `product_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '商品价格',
  `product_sku_snapshot` TEXT COMMENT '商品冗余信息，防止商品修改留存快照',
  `payment_cycle` INT(11) NOT NULL DEFAULT 0 COMMENT '付款期数',
  `pay_mode` INT(11) NOT NULL DEFAULT '0' COMMENT '支付方式：1先用后付，2先付后用',
  `is_new_product` INT(11) NOT NULL DEFAULT 0 COMMENT '是否是全新机，1是0否',
  `renting_product_count` INT(11) NOT NULL DEFAULT 0 COMMENT '在租商品总数',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_relet_order_id ( `relet_order_id` ),
  INDEX index_relet_order_no ( `relet_order_no` ),
  INDEX index_order_id ( `order_id` ),
  INDEX index_order_no ( `order_no` ),
  INDEX index_order_product_id ( `order_product_id` ),
  INDEX index_product_id ( `product_id` ),
  INDEX index_product_sku_id ( `product_sku_id` )
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='续租订单商品项表';


DROP TABLE IF EXISTS `erp_relet_order_material`;
CREATE TABLE `erp_relet_order_material` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `relet_order_id` INT(20) NOT NULL COMMENT '续租订单ID',
  `relet_order_no` VARCHAR(100) NOT NULL COMMENT '续租订单编号',
  `order_id` INT(20) NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(100) NOT NULL COMMENT '订单编号',
  `order_material_id` INT(20) NOT NULL COMMENT '订单配件项ID',
  `material_id` INT(20) COMMENT '配件ID',
  `material_name` VARCHAR(100) COLLATE utf8_bin COMMENT '配件名称',
  `material_count` INT(11) NOT NULL DEFAULT '0' COMMENT '配件总数',
  `material_unit_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `material_amount` DECIMAL(15,5) NOT NULL DEFAULT 0 COMMENT '配件价格',
  `material_snapshot` TEXT COMMENT '配件冗余信息，防止商品修改留存快照',
  `payment_cycle` INT(11) NOT NULL DEFAULT 0 COMMENT '付款期数',
  `pay_mode` INT(11) NOT NULL DEFAULT '0' COMMENT '支付方式：1先用后付，2先付后用',
  `is_new_material` INT(11) NOT NULL DEFAULT 0 COMMENT '是否是全新机，1是0否',
  `renting_material_count` INT(11) NOT NULL DEFAULT 0 COMMENT '在租配件总数',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_relet_order_id ( `relet_order_id` ),
  INDEX index_relet_order_no ( `relet_order_no` ),
  INDEX index_order_id ( `order_id` ),
  INDEX index_order_no ( `order_no` ),
  INDEX index_order_material_id ( `order_material_id` ),
  INDEX index_material_id ( `material_id` )
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='续租订单配件项表';

-- erp_k3_return_order表新处理成功的状态字段
alter table erp_k3_return_order add success_status int(11) NOT NULL DEFAULT 1 COMMENT "处理成功的状态0 未成功处理 1 处理成功"; #

alter table erp_coupon_batch add `coupon_batch_lock_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券锁定总数';
alter table erp_coupon_batch_detail add `coupon_lock_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券锁定总数';


alter table erp_joint_product add is_new  INT(11) NOT NULL COMMENT '是否全新：0-否，1-是';

DROP TABLE IF EXISTS `erp_print_log`;
CREATE TABLE `erp_print_log` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `refer_no` varchar(100) NOT NULL COMMENT '关联NO',
  `refer_type` int(11) COMMENT '关联项类型，1-交货单,2-退货单',
  `print_count` int(11) NOT NULL DEFAULT 0 COMMENT '打印次数',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='打印记录表';

DROP TABLE IF EXISTS `erp_business_commission_config`;
CREATE TABLE `erp_business_commission_config` (
  `id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `department_type` INT(11) NOT NULL COMMENT '部门类型',
  `data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
  `create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
  `update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务提成配置表';

insert into erp_business_commission_config(department_type, data_status, create_time, create_user, update_time, update_user) values(300010, 1, NOW(), 500001, NOW(), 500001)
ALTER TABLE erp_customer_update_log add `is_owner_update_flag` int(11)  COMMENT '是否变更了归属人，0否1是';
ALTER TABLE erp_customer_update_log add `is_union_user_update_flag` int(11)  COMMENT '是否变更了联合开发人，0否1是';
ALTER TABLE erp_customer_update_log add `old_owner` int(20)  COMMENT '变更之前的开发人';
ALTER TABLE erp_customer_update_log add `old_union_user` int(20)  COMMENT '变更之前的联合开发人';

DROP TABLE IF EXISTS `erp_order_split_detail`;
CREATE TABLE `erp_order_split_detail` (
	`id` INT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
	`order_no` varchar(100) NOT NULL COMMENT '订单号',
	`order_id` int(20) NOT NULL COMMENT '订单ID',
	`order_item_type` int(20) NOT NULL COMMENT '订单项类型，1为商品，2为配件',
	`order_item_refer_id` int(20) NOT NULL COMMENT '订单项ID',
	`split_count` int(20) NOT NULL DEFAULT 0 COMMENT '拆分数量',
	`is_peer` int(11) NOT NULL DEFAULT '0' COMMENT '是否同行调拨，0-否，1是',
	`delivery_sub_company_id` INT(20) COMMENT '发货分公司id[非同行调拨时必填]',
	`delivery_sub_company_name` varchar(50) DEFAULT '' COMMENT '发货分公司名称',
	`data_status` INT(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
	`remark` VARCHAR(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
	`create_time` DATETIME DEFAULT NULL COMMENT '添加时间',
	`create_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '添加人',
	`update_time` DATETIME DEFAULT NULL COMMENT '修改时间',
	`update_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '修改人',
	PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单拆单明细表';

alter table `erp_bank_slip` change `slip_month` `slip_day` datetime NOT NULL COMMENT '导入日期'

ALTER TABLE erp_order_product add `order_joint_product_id` int(20) COMMENT '订单组合商品id';
ALTER TABLE erp_order_material add `order_joint_product_id` int(20) COMMENT '订单组合商品id';
ALTER TABLE erp_joint_product DROP COLUMN is_new;

-- 如果索引存在则删除索引  added by liuyong begin++ 2018-05-15 12:00
DROP PROCEDURE IF EXISTS del_index;
CREATE PROCEDURE del_index(IN p_tablename varchar(200), IN p_idxname VARCHAR(200))
BEGIN
DECLARE str VARCHAR(250);
DECLARE cur_database VARCHAR(100);
SELECT DATABASE() INTO cur_database;
set @str=concat(' DROP INDEX ',p_idxname,' ON ',p_tablename);
SELECT count(*) INTO @cnt FROM information_schema.statistics WHERE table_schema=cur_database and table_name=p_tablename and index_name=p_idxname ;
IF @cnt > 0 THEN
    PREPARE stmt FROM @str;
    EXECUTE stmt ;
END IF;
END ;

-- index for table [erp_order_product]
call del_index('erp_order_product','index_create_time');
call del_index('erp_order_product','index_order_id');
ALTER TABLE erp_order_product ADD INDEX index_create_time (create_time);
ALTER TABLE erp_order_product ADD INDEX index_order_id (order_id);

-- index for table [erp_order]
call del_index('erp_order','index_buyer_customer_id');
call del_index('erp_order','index_rent_start_time');
-- ALTER TABLE erp_order DROP INDEX index_buyer_customer_id;
-- ALTER TABLE erp_order DROP INDEX index_create_time;
ALTER TABLE erp_order ADD INDEX index_rent_start_time (rent_start_time);
ALTER TABLE erp_order ADD INDEX index_buyer_customer_id (buyer_customer_id);
-- index for table [erp_k3_return_order_detail]
call del_index('erp_k3_return_order_detail','index_order_no');
call del_index('erp_k3_return_order_detail','index_return_order_id');
-- ALTER TABLE erp_k3_return_order_detail DROP INDEX index_return_order_id;
-- ALTER TABLE erp_k3_return_order_detail DROP INDEX index_order_no;
ALTER TABLE erp_k3_return_order_detail ADD INDEX index_order_no (order_no);
ALTER TABLE erp_k3_return_order_detail ADD INDEX index_return_order_id (return_order_id);
-- index for table [erp_k3_return_order]
call del_index('erp_k3_return_order','index_return_time');
-- ALTER TABLE erp_k3_return_order DROP INDEX index_return_time;
ALTER TABLE erp_k3_return_order ADD INDEX index_return_time (return_time);

-- index for table [erp_statement_order_detail]
call del_index('erp_statement_order_detail','index_statement_expect_pay_time');
-- ALTER TABLE erp_statement_order_detail DROP INDEX index_statement_expect_pay_time;
ALTER TABLE erp_statement_order_detail ADD INDEX index_statement_expect_pay_time (statement_expect_pay_time);

-- added by liuyong end++ 2018-05-15 12:00

