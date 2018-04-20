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
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本更改历史表';

DROP TABLE if exists `erp_version_history_detail`
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
  PRIMARY KEY (`id`),
  INDEX index_versoin_history_id ( `versoin_history_id` ),
  INDEX index_versoin_history_no ( `versoin_history_no` )
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本更改历史明细表';

DROP TABLE if exists `erp_version_history_user`
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
  PRIMARY KEY (`id`),
  INDEX index_user_id ( `user_id` ),
  INDEX index_versoin_history_id ( `versoin_history_id` ),
  INDEX index_versoin_history_no ( `versoin_history_no` )
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='版本变更推送记录表';

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




