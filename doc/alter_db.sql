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





