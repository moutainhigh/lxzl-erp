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



---------------------- 未执行 ----------------------

ALTER TABLE erp_customer_company add `address_verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '公司经营地址审核状态：0未提交；1.已提交 2.初审通过；3.终审通过';


DROP TABLE if exists `erp_return_visit`;
CREATE TABLE `erp_return_visit` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_visit_describe` varchar(1000) NOT NULL COMMENT '回访描述',
  `customer_no` varchar(100) NOT NULL COMMENT '客戶编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  INDEX index_customer_no ( `customer_no` ) ,
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='回访记录表';

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
  `coupon_used_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券已使用总数',
  `coupon_received_count` int(11) NOT NULL  DEFAULT 0 COMMENT '优惠券线上已领取总数',
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
  `erp_batch_detail_id` int(20) NOT NULL COMMENT '批次详情ID',
  `face_value` decimal(15,5) NOT NULL DEFAULT 0 COMMENT '优惠券面值',
  `deduction_amount` decimal(15,5) NOT NULL DEFAULT 0  COMMENT '抵扣金额',
  `coupon_status` int(11) NOT NULL DEFAULT 0  COMMENT '优惠券状态，0-未领取，4-可用，8-已用',
  `customer_no` varchar(100) NOT NULL COMMENT '客戶编号',
  `is_online` int(11) NOT NULL COMMENT '是否线上，0-否，1-是',
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
  INDEX index_customer_no ( `customer_no` )
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='优惠券表';
