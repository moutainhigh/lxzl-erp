CREATE DATABASE IF NOT EXISTS db_lxzl_payment_gateway DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE lxzl_erp;

DROP TABLE if exists `erp_user`;
CREATE TABLE `erp_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_type` int(11) DEFAULT NULL COMMENT '用户类型，1为总部人员',
  `user_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `real_name` varchar(64) COLLATE utf8_bin COMMENT '真实姓名,1为普通用户',
  `password` varchar(128) CHARACTER SET ascii COMMENT '密码',
  `email` varchar(128) CHARACTER SET ascii DEFAULT NULL COMMENT 'email',
  `email_verify_code` varchar(8) CHARACTER SET ascii DEFAULT NULL COMMENT 'email验证码',
  `email_verify_time` datetime DEFAULT NULL COMMENT 'email验证时间',
  `phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '手机号',
  `phone_verify_code` varchar(8) CHARACTER SET ascii DEFAULT NULL COMMENT '手机号验证码',
  `phone_verify_time` datetime DEFAULT NULL COMMENT '手机验证时间',
  `is_activated` tinyint(4) NOT NULL COMMENT '是否激活，0不可用；1可用',
  `is_disabled` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否禁用，0启用；1禁用',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(40) CHARACTER SET ascii DEFAULT NULL COMMENT '最后登录IP',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=500001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理系统用户信息表';

DROP TABLE if exists `erp_user_login_log`;
CREATE TABLE `erp_user_login_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `login_ip` varchar(40) CHARACTER SET ascii DEFAULT NULL COMMENT '登录IP',
  `login_mac_address` varchar(40) CHARACTER SET ascii DEFAULT NULL COMMENT '登录mac地址',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户登录日志表';

DROP TABLE if exists `erp_role`;
CREATE TABLE `erp_role` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `role_desc` varchar(500) COLLATE utf8_bin COMMENT '角色描述',
  `department_id` int(20) NOT NULL COMMENT '部门ID',
  `is_super_admin` int(11) NOT NULL DEFAULT '0' COMMENT '是否是超级管理员，0不是，1是',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=600001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

DROP TABLE if exists `erp_user_role`;
CREATE TABLE `erp_user_role` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(20) NOT NULL COMMENT '用户ID',
  `role_id` int(20) NOT NULL COMMENT '角色ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色表';

DROP TABLE if exists `erp_role_menu`;
CREATE TABLE `erp_role_menu` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `role_id` int(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` int(20) COMMENT '功能ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色功能表';

DROP TABLE if exists `erp_role_department_data`;
CREATE TABLE `erp_role_department_data` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `role_id` int(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `department_id` int(20) NOT NULL COMMENT '部门ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色可观察部门表';

DROP TABLE if exists `erp_role_user_data`;
CREATE TABLE `erp_role_user_data` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `active_user_id` int(20) NOT NULL DEFAULT '0' COMMENT '观察者用户ID',
  `passive_user_id` int(20) COMMENT '被观察者用户ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户可观察用户表';

DROP TABLE if exists `erp_department`;
CREATE TABLE `erp_department` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `department_name` varchar(20) NOT NULL DEFAULT '' COMMENT '功能名称',
  `department_type` int(20) NOT NULL COMMENT '部门类型，对应字典ID',
  `parent_department_id` int(20) NOT NULL DEFAULT 400000 COMMENT '上级部门ID',
  `sub_company_id` int(20) NOT NULL COMMENT '所属分公司',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=400001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='部门表';

DROP TABLE if exists `erp_sub_company`;
CREATE TABLE `erp_sub_company` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `sub_company_name` varchar(20) NOT NULL DEFAULT '' COMMENT '子公司名称',
  `sub_company_type` int(11) NOT NULL COMMENT '公司类型:1-总公司2-分公司3-供应商',
  `sub_company_code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '分公司编码',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='分公司表';

DROP TABLE if exists `erp_role_user_final`;
CREATE TABLE `erp_role_user_final` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `active_user_id` int(20) NOT NULL DEFAULT '0' COMMENT '观察者用户ID',
  `passive_user_id` int(20) COMMENT '被观察者用户ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='最终用户可观察用户数据权限表';

DROP TABLE if exists `erp_sys_menu`;
CREATE TABLE `erp_sys_menu` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '功能编号，唯一',
  `menu_name` varchar(20) NOT NULL DEFAULT '' COMMENT '功能名称',
  `parent_menu_id` int(20) NOT NULL DEFAULT 200000 COMMENT '父功能ID',
  `menu_order` int(11) NOT NULL DEFAULT '0' COMMENT '功能排序，越大排越前',
  `is_folder` int(11) NOT NULL DEFAULT '0' COMMENT '0为功能夹，1为功能，2为按钮（按钮只能放在功能下）',
  `menu_url` varchar(50) NOT NULL DEFAULT '' COMMENT '功能URL，如is_folder为0，则为空',
  `menu_icon` varchar(50) NOT NULL DEFAULT '' COMMENT '功能图标',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别，1为根一级，以此类推',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统功能表';

DROP TABLE if exists `erp_img`;
CREATE TABLE `erp_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID，唯一',
  `img_type` int(11) NOT NULL DEFAULT '0' COMMENT '图片类型',
  `original_name` varchar(200) NOT NULL DEFAULT '' COMMENT '文件原名',
  `ref_id` varchar(20) COMMENT '根据不同的业务，对应不同的ID',
  `img_url` varchar(200) NOT NULL DEFAULT '' COMMENT '图片URL',
  `img_order` int(11) NOT NULL DEFAULT '0' COMMENT '图片排序，越大越前面',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='图片表';

DROP TABLE if exists `erp_data_dictionary`;
CREATE TABLE `erp_data_dictionary` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID，唯一',
  `data_name` varchar(20) NOT NULL DEFAULT '' COMMENT '数据名称',
  `parent_dictionary_id` int(20) NOT NULL DEFAULT 300000 COMMENT '父ID',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_type` int(11) NOT NULL DEFAULT '0' COMMENT '数据类型',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=300001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据字典表';


DROP TABLE if exists `erp_supplier`;
CREATE TABLE `erp_supplier` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `supplier_no` varchar(100) NOT NULL COMMENT '供应商编码',
  `supplier_type` int(11) DEFAULT 0 COMMENT '用户类型,0为普通供应商，1为发票供应商',
  `supplier_name` varchar(100) NOT NULL DEFAULT '' COMMENT '供应商名称',
  `supplier_code` varchar(100) COLLATE utf8_bin COMMENT '供应商自定义编码',
  `province` int(20) COMMENT '省份ID，省份ID',
  `city` int(20) COMMENT '城市ID，对应城市ID',
  `district` int(20) COMMENT '区ID，对应区ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `tel` varchar(100) COMMENT '电话号码',
  `contact_name` varchar(100) COMMENT '联系人姓名',
  `contact_phone` varchar(20) COMMENT '联系手机号',
  `beneficiary_name` varchar(100) COMMENT '收款户名',
  `beneficiary_account` varchar(50) COMMENT '收款帐号',
  `beneficiary_bank_name` varchar(100) COMMENT '收款开户行',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_supplier_no` (`supplier_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='供应商表';

DROP TABLE if exists `erp_peer`;
CREATE TABLE `erp_peer` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_no` varchar(100) NOT NULL COMMENT '同行编码',
  `peer_name` varchar(100) NOT NULL DEFAULT '' COMMENT '同行名称',
  `peer_code` varchar(100) COLLATE utf8_bin COMMENT '同行自定义编码',
  `province` int(20) COMMENT '省份ID，省份ID',
  `city` int(20) COMMENT '城市ID，对应城市ID',
  `district` int(20) COMMENT '区ID，对应区ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `tel` varchar(100) COMMENT '电话号码',
  `contact_name` varchar(100) COMMENT '联系人姓名',
  `contact_phone` varchar(20) COMMENT '联系手机号',
  `beneficiary_name` varchar(100) COMMENT '收款户名',
  `beneficiary_account` varchar(50) COMMENT '收款帐号',
  `beneficiary_bank_name` varchar(100) COMMENT '收款开户行',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_peer_no` (`peer_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行表';

-- ****************************************地区表**************************************** --

DROP TABLE if exists `erp_area_province`;
CREATE TABLE `erp_area_province` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `province_name` varchar(64) NOT NULL COMMENT '地区名称',
  `area_type` int(11) COMMENT '区域类型，1-华东，2-华南，3-华中，4-华北，5-西北，6-西南，7-东北，8-港澳台',
  `abb_cn` varchar(64) COMMENT '中文简称',
  `abb_en` varchar(64) COMMENT '英文简称',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='地区省份表';

DROP TABLE if exists `erp_area_city`;
CREATE TABLE `erp_area_city` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `province_id` int(20) NOT NULL COMMENT '地区省份ID',
  `city_name` varchar(64) NOT NULL COMMENT '地区名称',
  `city_code` varchar(64) COMMENT '城市区号',
  `post_code` varchar(64) COMMENT '邮政编码',
  `abb_cn` varchar(64) COMMENT '中文简称',
  `abb_en` varchar(64) COMMENT '英文简称',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='地区城市表';

DROP TABLE if exists `erp_area_district`;
CREATE TABLE `erp_area_district` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `province_id` int(20) NOT NULL COMMENT '地区省份ID',
  `city_id` int(20) NOT NULL COMMENT '地区省份ID',
  `district_name` varchar(64) NOT NULL COMMENT '地区名称',
  `post_code` varchar(64) COMMENT '邮政编码',
  `abb_cn` varchar(64) COMMENT '中文简称',
  `abb_en` varchar(64) COMMENT '英文简称',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='地区行政区表';

-- ****************************************客户表**************************************** --
DROP TABLE if exists `erp_customer`;
CREATE TABLE `erp_customer` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_type` int(11) DEFAULT NULL COMMENT '用户类型,1为企业用户，2为个人用户',
  `customer_no` varchar(100) NOT NULL COMMENT '客戶编码',
  `customer_name` varchar(64) NOT NULL COMMENT '客户名称',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人，跟单员',
  `union_user` int(20) COMMENT '联合开发人',
  `localization_time` datetime DEFAULT NULL COMMENT '属地化时间',
  `short_limit_receivable_amount` decimal(15,2) COMMENT '短租应收上限',
  `statement_date` int(20) COMMENT '结算时间（天），20和31两种情况，如果为空取系统设定',
  `customer_status` int(4) NOT NULL DEFAULT '0' COMMENT '客户状态，0初始化，1资料提交，2审核通过，3资料驳回',
  `first_apply_amount` decimal(15,2) DEFAULT '0.00' COMMENT '首期申请额度',
  `later_apply_amount` decimal(15,2) DEFAULT '0.00' COMMENT '后期申请额度',
  `is_disabled` int(4) NOT NULL DEFAULT '0' COMMENT '是否禁用，0不可用；1可用',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=700001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='客户表';

DROP TABLE if exists `erp_customer_update_log`;
CREATE TABLE `erp_customer_update_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `owner` int(20) COMMENT '数据归属人，跟单员',
  `union_user` int(20) COMMENT '联合开发人',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='客户变更记录表';

DROP TABLE if exists `erp_customer_person`;
CREATE TABLE `erp_customer_person` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `person_no` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '身份证号码',
  `connect_real_name` varchar(24) COLLATE utf8_bin NOT NULL COMMENT '紧急联系人姓名',
  `connect_phone` varchar(24) COLLATE utf8_bin NOT NULL COMMENT '紧急联系人电话',
  `real_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '真实姓名',
  `email` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(24) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` text COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='个人客户表';

DROP TABLE if exists `erp_customer_company`;
CREATE TABLE `erp_customer_company` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `customer_no` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '客户编码',
  `company_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '公司名称',
  `company_abb` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '公司简称',
  `is_legal_person_apple` int(10) DEFAULT '0' COMMENT '是否法人代表申请， 0否，1是',
  `legal_person` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '法人姓名',
  `legal_person_no` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '法人身份证号',
  `legal_person_phone` varchar(24) COLLATE utf8_bin DEFAULT NULL COMMENT '法人手机号',
  `agent_person_name` varchar(24) COLLATE utf8_bin DEFAULT NULL COMMENT '经办人姓名',
  `agent_person_no` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '经办人身份证号码',
  `agent_person_phone` varchar(24) COLLATE utf8_bin DEFAULT NULL COMMENT '经办人电话',
  `business_license_no` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '营业执照号',
  `unit_insured_number` int(20) DEFAULT NULL COMMENT '单位参保人数',
  `customer_origin` int(20) DEFAULT NULL COMMENT '客户来源,1地推活动，2展会了解，3业务联系，4百度推广，5朋友推荐，6其他广告',
  `registered_capital` decimal(15,2) DEFAULT '0.00' COMMENT '注册资本',
  `industry` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '所属行业',
  `company_found_time` datetime DEFAULT NULL COMMENT '企业成立时间',
  `landline` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '座机电话',
  `connect_real_name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '紧急联系人',
  `connect_phone` varchar(24) COLLATE utf8_bin DEFAULT NULL COMMENT '紧急联系人手机号',
  `product_purpose` varchar(24) COLLATE utf8_bin DEFAULT NULL COMMENT '设备用途',
  `customer_company_need_first_json` text COLLATE utf8_bin COMMENT '首次所需设备',
  `customer_company_need_later_json` text COLLATE utf8_bin COMMENT '后期所需设备',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '详细地址',
  `office_number` int(20) DEFAULT NULL COMMENT '办公人数',
  `unified_credit_code` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '统一信用代码',
  `operating_area` double DEFAULT NULL COMMENT '经营面积',
  `affiliated_enterprise` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '关联企业',
  `remark` text CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=400001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='公司客户表';

DROP TABLE if exists `erp_customer_risk_management`;
CREATE TABLE `erp_customer_risk_management` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `credit_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '授信额度',
  `credit_amount_used` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '已用授信额度',
  `short_collect_credit_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '短租待收授信金额',
  `short_collect_credit_amount_used` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '已用短租待收授信金额',
  `deposit_cycle` int(11) DEFAULT NULL COMMENT '押金期数',
  `payment_cycle` int(11) DEFAULT NULL COMMENT '付款期数',
  `apple_deposit_cycle` int(11) DEFAULT NULL COMMENT '苹果设备押金期数',
  `apple_payment_cycle` int(11) DEFAULT NULL COMMENT '苹果设备付款期数',
  `new_deposit_cycle` int(11) DEFAULT NULL COMMENT '全新押金期数',
  `new_payment_cycle` int(11) DEFAULT NULL COMMENT '全新付款期数',
  `pay_mode` int(11) DEFAULT NULL COMMENT '其他设备支付方式',
  `apple_pay_mode` int(11) DEFAULT NULL COMMENT '苹果设备付款方式',
  `new_pay_mode` int(11) DEFAULT NULL COMMENT '全新付款方式',
  `is_limit_apple` int(11) NOT NULL DEFAULT '0' COMMENT '是否限制苹果，1是，0否',
  `is_limit_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否限制全新，1是，0否',
  `single_limit_price` decimal(15,2) COMMENT '单台限制价值',
  `return_visit_frequency` int(11) NOT NULL DEFAULT '0' COMMENT '回访频率（单位，月）',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` text CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='客户风控信息';

DROP TABLE if exists `erp_customer_risk_flow`;
CREATE TABLE `erp_customer_risk_flow` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `flow_type` int(11) NOT NULL DEFAULT '0' COMMENT '流水类型，1人为修改，2租赁，3换货，4退还，',
  `old_credit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '原授信额度',
  `old_credit_amount_used` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '原已用授信额度',
  `old_deposit_cycle` int(11) COMMENT '原押金期数',
  `old_payment_cycle` int(11) COMMENT '原付款期数',
  `new_credit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '现授信额度',
  `new_credit_amount_used` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '现已用授信额度',
  `new_deposit_cycle` int(11) COMMENT '现押金期数',
  `new_payment_cycle` int(11) COMMENT '现付款期数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='客户风控信息流水';

DROP TABLE if exists `erp_customer_consign_info`;
CREATE TABLE `erp_customer_consign_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `customer_id` int(20) NOT NULL COMMENT '用户ID',
  `consignee_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '收货人手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `is_main` int(11) NOT NULL DEFAULT '0' COMMENT '是否为默认地址，0否1是',
  `last_use_time` datetime DEFAULT NULL COMMENT '最后使用时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='客户收货配置信息表';

-- ****************************************审批流程**************************************** --

DROP TABLE if exists `erp_workflow_template`;
CREATE TABLE `erp_workflow_template` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `workflow_name` varchar(100) NOT NULL DEFAULT '' COMMENT '工作流名称',
  `workflow_desc` varchar(500) COLLATE utf8_bin COMMENT '工作流描述',
  `workflow_type` int(11) NOT NULL DEFAULT '0' COMMENT '工作流类型',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_workflow_type` (`workflow_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流模板表';

DROP TABLE if exists `erp_workflow_node`;
CREATE TABLE `erp_workflow_node` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `workflow_node_name` varchar(100) NOT NULL DEFAULT '' COMMENT '工作流子节点名称',
  `workflow_template_id` int(20) NOT NULL COMMENT '流程模板ID',
  `workflow_step` int(20) NOT NULL COMMENT '流程步骤',
  `workflow_previous_node_id` int(20) COMMENT '上节点ID',
  `workflow_next_node_id` int(20) COMMENT '下节点ID',
  `workflow_department_type` int(20) COMMENT '可审核的部门类型，如果没有指定部门，则由本公司的该部门审核',
  `workflow_department` int(20) COMMENT '本步骤可审批部门',
  `workflow_role` int(20) COMMENT '本步骤可审批角色',
  `workflow_user` int(20) COMMENT '本步骤可审批人员',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流节点表';

DROP TABLE if exists `erp_workflow_link`;
CREATE TABLE `erp_workflow_link` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `workflow_link_no` varchar(100) NOT NULL COMMENT '工作流编码',
  `workflow_type` int(11) NOT NULL DEFAULT '0' COMMENT '工作流类型',
  `workflow_template_id` int(20) NOT NULL COMMENT '工作流模板ID',
  `workflow_refer_no` varchar(100) NOT NULL COMMENT '工作流关联NO',
  `workflow_step` int(20) NOT NULL COMMENT '流程当前步骤',
  `workflow_last_step` int(20) NOT NULL COMMENT '流程最后步骤',
  `workflow_current_node_id` int(20) NOT NULL COMMENT '当前结点ID',
  `commit_user` int(20) COMMENT '提交人',
  `current_verify_user` int(20) COMMENT '审核人',
  `current_verify_status` int(20) COMMENT '审核状态',
  `verify_matters` varchar(500) COMMENT '审核事项',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_workflow_type_refer` (`workflow_type`,`workflow_refer_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流线表';

DROP TABLE if exists `erp_workflow_link_detail`;
CREATE TABLE `erp_workflow_link_detail` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `workflow_link_id` int(20) NOT NULL COMMENT '工作流线ID',
  `workflow_refer_no` varchar(100) NOT NULL COMMENT '工作流关联NO',
  `workflow_step` int(20) NOT NULL COMMENT '流程当前步骤',
  `workflow_current_node_id` int(20) NOT NULL COMMENT '当前结点ID',
  `workflow_previous_node_id` int(20) COMMENT '上节点ID',
  `workflow_next_node_id` int(20) COMMENT '下节点ID',
  `verify_user` int(20) COMMENT '审核人',
  `verify_time` datetime COMMENT '审核时间',
  `verify_status` int(20) COMMENT '审核状态',
  `verify_opinion` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核意见',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流线明细表';

-- ****************************************商品模块**************************************** --

DROP TABLE if exists `erp_product_category`;
CREATE TABLE `erp_product_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `category_name` varchar(20) NOT NULL DEFAULT '' COMMENT '分类名称',
  `parent_category_id` int(20) NOT NULL DEFAULT 800000 COMMENT '父ID',
  `category_type` int(11) NOT NULL DEFAULT '0' COMMENT '分类类型，1组装机，2配件',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=800001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='产品分类表';


DROP TABLE if exists `erp_brand`;
CREATE TABLE `erp_brand` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `brand_name` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '品牌名称',
  `brand_english_name` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '品牌英文名称',
  `brand_desc` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '品牌描述',
  `brand_story` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '品牌故事',
  `logo_url` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'logo地址',
  `home_url` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '官网地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='品牌表';

DROP TABLE if exists `erp_product_category_property`;
CREATE TABLE `erp_product_category_property` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `property_name` varchar(64) NOT NULL DEFAULT '' COMMENT '属性名称(大名称)',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `property_type` int(11) COMMENT '属性类型，1为租赁属性，2为商品属性',
  `material_type` int(11) COMMENT '配件类型',
  `is_input` int(11) NOT NULL DEFAULT '0' COMMENT '是否是输入属性：0否1是',
  `is_checkbox` int(11) NOT NULL DEFAULT '0' COMMENT '是否为多选，0否1是',
  `is_required` int(11) NOT NULL DEFAULT '0' COMMENT '是否为必选，0否1是',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '属性排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='产品分类表';


DROP TABLE if exists `erp_product_category_property_value`;
CREATE TABLE `erp_product_category_property_value` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `property_value_name` varchar(64) NOT NULL DEFAULT '' COMMENT '属性值',
  `property_id` int(20) NOT NULL COMMENT '所属属性ID',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '属性排序，越大排越前',
  `property_capacity_value` double(11,2) DEFAULT NULL COMMENT '属性容量值',
  `material_model_id` int(20) DEFAULT NULL COMMENT '配件型号',
  `refer_id` int(11) DEFAULT NULL,
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='产品分类属性值表';

DROP TABLE if exists `erp_product`;
CREATE TABLE `erp_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_no` varchar(100) NOT NULL COMMENT '商品编码',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称',
  `product_model` varchar(100) COMMENT '商品型号',
  `brand_id` int(20) COMMENT '所属品牌ID',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `subtitle` varchar(60) NOT NULL DEFAULT '' COMMENT '副标题',
  `unit` int(20) COMMENT '单位，对应字典ID',
  `list_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '列表展示价格',
  `is_rent` int(11) NOT NULL DEFAULT '0' COMMENT '是否在租：0下架；1上架',
  `product_desc` text COLLATE utf8_bin COMMENT '商品描述',
  `keyword` text COMMENT '多个字段的组合，便于搜索',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品表';

DROP TABLE if exists `erp_product_sku`;
CREATE TABLE `erp_product_sku` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `sku_name` varchar(100) COLLATE utf8_bin COMMENT 'SKU名称',
  `product_id` int(20) NOT NULL COMMENT '所属产品ID',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `sku_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品本身的价值',
  `day_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '天租赁价格',
  `month_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '月租赁价格',
  `new_sku_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '全新商品本身的价值',
  `new_day_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '全新天租赁价格',
  `new_month_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '全新月租赁价格',
  `custom_code` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '自定义编码',
  `bar_code` varchar(60) COMMENT '商品条形码',
  `properties` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '属性字符串，按照;分隔',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品SKU表';

DROP TABLE if exists `erp_product_sku_property`;
CREATE TABLE `erp_product_sku_property` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU property ID',
  `product_id` int(20) NOT NULL COMMENT '所属产品ID',
  `property_id` int(20) NOT NULL COMMENT '属性ID',
  `property_value_id` int(20) NOT NULL COMMENT '属性值ID',
  `is_sku` int(11) NOT NULL DEFAULT '0' COMMENT '是否是sku，1是0否',
  `sku_id` int(20) COMMENT 'SKU ID',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品SKU基本属性表';

DROP TABLE if exists `erp_material_type`;
CREATE TABLE `erp_material_type` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `material_type_name` varchar(100) COLLATE utf8_bin COMMENT '配件类型名称',
  `is_main_material` int(11) NOT NULL DEFAULT '0' COMMENT '是否为主要配件',
  `is_capacity_material` int(11) NOT NULL DEFAULT '0' COMMENT '是否为带字面量配件,硬盘、内存之类是',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='配件类型表';


DROP TABLE if exists `erp_material`;
CREATE TABLE `erp_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '配件ID',
  `material_no` varchar(100) NOT NULL COMMENT '配件唯一编号',
  `material_name` varchar(100) COLLATE utf8_bin COMMENT '配件名称，取属性与属性值全称',
  `material_model` varchar(100) COMMENT '商品型号（英文）',
  `material_type` int(20) NOT NULL COMMENT '配件类型',
  `material_model_id` int(20) DEFAULT NULL COMMENT '配件型号ID',
  `material_capacity_value` double(11,2) DEFAULT NULL COMMENT '配件字面量',
  `is_main_material` int(20) NOT NULL COMMENT '是否是主配件（四大件）',
  `brand_id` int(20) COMMENT '所属品牌ID',
  `material_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件本身的价值(单价)',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `is_rent` int(11) NOT NULL DEFAULT '0' COMMENT '是否在租：0下架；1上架',
  `day_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '天租赁价格',
  `month_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '月租赁价格',
  `new_material_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '全新配件本身的价值(单价)',
  `new_day_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '全新天租赁价格',
  `new_month_rent_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '全新月租赁价格',
  `material_desc` text COLLATE utf8_bin COMMENT '配件描述',
  `is_consumable` int(20) NOT NULL DEFAULT 0 COMMENT '配件型号ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='配件表';

DROP TABLE if exists `erp_material_img`;
CREATE TABLE `erp_material_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID，唯一',
  `img_type` int(11) NOT NULL DEFAULT '0' COMMENT '图片类型',
  `original_name` varchar(200) NOT NULL DEFAULT '' COMMENT '文件原名',
  `material_id` int(20) COMMENT '所属配件ID',
  `img_url` varchar(200) NOT NULL DEFAULT '' COMMENT '图片URL',
  `is_main` int(11) NOT NULL DEFAULT '0' COMMENT '是否是主图，0否1是',
  `img_order` int(11) NOT NULL DEFAULT '0' COMMENT '图片排序，越大越前面',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='配件图片表';

DROP TABLE if exists `erp_material_model`;
CREATE TABLE `erp_material_model` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `material_type` int(11) NOT NULL DEFAULT '0' COMMENT '图片类型',
  `model_name` varchar(200) NOT NULL DEFAULT '' COMMENT '文件原名',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='配件型号表';

DROP TABLE if exists `erp_product_material`;
CREATE TABLE `erp_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '配件总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_product_material` (`product_sku_id`,`material_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品配件表';

DROP TABLE if exists `erp_product_equipment`;
CREATE TABLE `erp_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `product_id` int(20) NOT NULL COMMENT '所属产品ID',
  `sku_id` int(20) NOT NULL COMMENT '所属SKU ID',
  `order_no` varchar(100) DEFAULT NULL COMMENT '关联订单号，租赁中状态时有值',
  `current_warehouse_id` int(20) NOT NULL COMMENT '目前仓库ID',
  `current_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '目前仓位ID',
  `owner_warehouse_id` int(20) NOT NULL COMMENT '归属仓库ID',
  `owner_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '归属目前仓位ID',
  `equipment_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '设备本身价值',
  `purchase_price` decimal(15,2) COMMENT '采购价格',
  `equipment_status` int(11) NOT NULL DEFAULT '0' COMMENT '设备状态，1设备空闲，2租赁中，3维修中，4报废，5调拨中',
  `is_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否全新，1是，0否',
  `purchase_receive_remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '采购收料备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品设备表';

DROP TABLE if exists `erp_product_equipment_material`;
CREATE TABLE `erp_product_equipment_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '配件总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备配件表';

DROP TABLE if exists `erp_bulk_material`;
CREATE TABLE `erp_bulk_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '散料ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料唯一编号',
  `bulk_material_type` int(20) NOT NULL COMMENT '散料类型',
  `is_main_material` int(20) NOT NULL COMMENT '是否是主配件（四大件）',
  `bulk_material_name` varchar(100) COLLATE utf8_bin COMMENT '散料名称，从配件生成',
  `material_id` int(20) NOT NULL COMMENT '配件ID，从配件生成的',
  `material_no` varchar(100) NOT NULL COMMENT '配件编号，从配件生成的',
  `order_no` varchar(100) DEFAULT NULL COMMENT '关联订单号，租赁中状态时有值',
  `current_warehouse_id` int(20) NOT NULL COMMENT '目前仓库ID',
  `current_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '目前仓位ID',
  `owner_warehouse_id` int(20) NOT NULL COMMENT '归属仓库ID',
  `owner_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '归属仓位ID',
  `brand_id` int(20) COMMENT '所属品牌ID',
  `material_model_id` int(20) DEFAULT NULL COMMENT '配件型号ID',
  `material_capacity_value` double(11,2) DEFAULT NULL COMMENT '配件字面量',
  `bulk_material_price` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '散料本身的价值(单价)',
  `purchase_price` decimal(15,2) COMMENT '采购价格',
  `current_equipment_id` int(20) COMMENT '当前设备ID',
  `current_equipment_no` varchar(100) COMMENT '当前设备编号',
  `bulk_material_status` int(11) NOT NULL DEFAULT '0' COMMENT '散料状态，1散料空闲，2租赁中，3维修中，4报废，5调拨中',
  `is_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否全新，1是，0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_bulk_material_no` (`bulk_material_no`),
  KEY `index_current_equipment_id` (`current_equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='散料表';

DROP TABLE if exists `erp_product_equipment_bulk_material`;
CREATE TABLE `erp_product_equipment_bulk_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `equipment_id` int(20) COMMENT '设备ID',
  `equipment_no` varchar(100) COMMENT '设备编号',
  `bulk_material_id` int(20) COMMENT '散料ID',
  `bulk_material_no` varchar(100) COMMENT '散料编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备散料表';

DROP TABLE if exists `erp_product_img`;
CREATE TABLE `erp_product_img` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID，唯一',
  `img_type` int(11) NOT NULL DEFAULT '0' COMMENT '图片类型',
  `original_name` varchar(200) NOT NULL DEFAULT '' COMMENT '文件原名',
  `product_id` int(20) COMMENT '所属产品ID',
  `img_url` varchar(200) NOT NULL DEFAULT '' COMMENT '图片URL',
  `is_main` int(11) NOT NULL DEFAULT '0' COMMENT '是否是主图，0否1是',
  `img_order` int(11) NOT NULL DEFAULT '0' COMMENT '图片排序，越大越前面',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品图片表';


DROP TABLE if exists `erp_repair_order`;
CREATE TABLE `erp_repair_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `repair_order_no` varchar(100) NOT NULL COMMENT '维修单编号',
  `repair_equipment_count` int(11) NOT NULL DEFAULT 0 COMMENT '送修设备数量',
  `repair_bulk_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '送修配件数量',
  `fix_equipment_count` int(11) NOT NULL DEFAULT 0 COMMENT '修复设备数量',
  `fix_bulk_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '修复配件数量',
  `warehouse_no` varchar(100) NOT NULL COMMENT '仓库编号',
  `repair_reason` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '维修原因，由发起人填写',
  `repair_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '维修单状态，0-初始化维修单,4-审核中,8-待维修,12-维修中,16-维修完成回库,20-取消维修',
  `data_status` int(11) NOT NULL DEFAULT 0 COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  `repair_end_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '维修完成的备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备维修单';

DROP TABLE if exists `erp_repair_order_equipment`;
CREATE TABLE `erp_repair_order_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `repair_order_no` varchar(100) NOT NULL COMMENT '维修单编号',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `repair_end_time` datetime DEFAULT NULL COMMENT '维修完成时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `order_id` int(20) DEFAULT NULL COMMENT '订单ID，如果是在客户手里出现的维修，此字段不能为空',
  `order_product_id` int(20) DEFAULT NULL COMMENT '订单商品项ID,如果是在客户手里出现的维修，此字段不能为空',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  `repair_end_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '维修完成的备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备维修单明细';

DROP TABLE if exists `erp_repair_order_bulk_material`;
CREATE TABLE `erp_repair_order_bulk_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `repair_order_no` varchar(100) NOT NULL COMMENT '维修单编号',
  `bulk_material_id` int(20) NOT NULL COMMENT '散料ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料编号唯一',
  `repair_end_time` datetime DEFAULT NULL COMMENT '维修完成时间',
  `order_id` int(20) DEFAULT NULL COMMENT '订单ID，如果是在客户手里出现的维修，此字段不能为空',
  `order_material_id` int(20) DEFAULT NULL COMMENT '订单配件项ID,如果是在客户手里出现的维修，此字段不能为空',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  `repair_end_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '维修完成的备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='散料维修单明细';

-- ****************************************仓库模块**************************************** --

DROP TABLE if exists `erp_warehouse`;
CREATE TABLE `erp_warehouse` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `warehouse_no` varchar(100) NOT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) COLLATE utf8_bin COMMENT '仓库名称',
  `warehouse_type` int(11) NOT NULL COMMENT '仓库类型,详见WarehouseType',
  `sub_company_id` int(20) NOT NULL COMMENT '分公司ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='仓库表';

DROP TABLE if exists `erp_warehouse_position`;
CREATE TABLE `erp_warehouse_position` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `warehouse_position_name` varchar(100) COLLATE utf8_bin COMMENT '仓位名称',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='仓位表';

DROP TABLE if exists `erp_stock_order`;
CREATE TABLE `erp_stock_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `stock_order_no` varchar(100) NOT NULL COMMENT '出入库单编号',
  `operation_type` int(11) NOT NULL COMMENT '操作类型，1入库，2出库',
  `cause_type` int(11) NOT NULL COMMENT '起因类型，1采购入库，2退货回库，3维修回库，4用户租赁',
  `refer_no` varchar(100) NOT NULL COMMENT '关联单号',
  `stock_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '出入库单状态，1未出库，2已出库',
  `src_warehouse_id` int(20) COMMENT '源仓库ID',
  `src_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '源仓位ID',
  `target_warehouse_id` int(20) NOT NULL COMMENT '目标仓库ID',
  `target_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '目标仓位ID',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_stock_type_refer` (`cause_type`,`refer_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备出入库单（仅记录）';

DROP TABLE if exists `erp_stock_order_equipment`;
CREATE TABLE `erp_stock_order_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `stock_order_no` varchar(100) NOT NULL COMMENT '出入库单编号',
  `item_refer_id` int(11) COMMENT '关联项ID',
  `item_refer_type` int(11) COMMENT '关联项类型，1-采购收货单商品项',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备出入库单明细';

  DROP TABLE if exists `erp_stock_order_bulk_material`;
CREATE TABLE `erp_stock_order_bulk_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `stock_order_no` varchar(100) NOT NULL COMMENT '出入库单编号',
  `item_refer_id` int(11) COMMENT '关联项ID',
  `item_refer_type` int(11) COMMENT '关联项类型，1-采购收货单商品项，2-采购收货单配件项',
  `bulk_material_id` int(20) NOT NULL COMMENT '散料ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料编号唯一',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='散料出入库单明细';

-- ****************************************订单模块**************************************** --

DROP TABLE if exists `erp_order`;
CREATE TABLE `erp_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_no` varchar(100) NOT NULL COMMENT '订单编号',
  `delivery_mode` int(11) COMMENT '发货方式，1快递，2自提,3凌雄配送',
  `buyer_customer_id` int(20) NOT NULL COMMENT '购买人ID',
  `expect_delivery_time` datetime NOT NULL COMMENT '送货时间',
  `rent_start_time` datetime NOT NULL COMMENT '起租时间',
  `total_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '应该支付的押金金额（系统生成）',
  `total_must_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '必须要支付的押金金额（系统先生成，支持修改）',
  `total_credit_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '授信押金金额',
  `total_rent_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '总租赁押金金额',
  `total_insurance_amount` decimal(15,2) DEFAULT 0 COMMENT '保险金额',
  `total_product_count` int(11) DEFAULT 0 COMMENT '商品总数',
  `total_product_amount` decimal(15,2) DEFAULT 0 COMMENT '商品总价',
  `total_product_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品应该支付的押金金额',
  `total_product_credit_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品授信押金金额',
  `total_product_rent_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品总租赁押金金额',
  `total_material_count` int(11) DEFAULT 0 COMMENT '配件总数',
  `total_material_amount` decimal(15,2) DEFAULT 0 COMMENT '配件总价',
  `total_material_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件应该支付的押金金额',
  `total_material_credit_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件授信押金金额',
  `total_material_rent_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件总租赁押金金额',
  `total_order_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '订单总价，实际支付价格，商品金额+配件金额+运费-优惠',
  `total_paid_order_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '已经支付金额',
  `total_discount_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '共计优惠金额',
  `logistics_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '运费',
  `order_seller_id` int(20) NOT NULL COMMENT '订单销售员',
  `order_sub_company_id` int(20) DEFAULT NULL COMMENT '订单所属分公司',
  `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态，0-待提交,4-审核中,8-待发货,12-处理中,16-已发货,20-确认收货,24-全部归还,28-取消,32-结束',
  `first_need_pay_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '首次需要交金额',
  `pay_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态，0未支付，1已支付，2已退款,3逾期中',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `confirm_delivery_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `expect_return_time` datetime DEFAULT NULL COMMENT '预计归还时间',
  `actual_return_time` datetime DEFAULT NULL COMMENT '实际归还时间，最后一件设备归还的时间',
  `high_tax_rate` int(11) NOT NULL DEFAULT 0 COMMENT '17%税率',
  `low_tax_rate` int(11) NOT NULL DEFAULT 0 COMMENT '6%税率',
  `tax_rate` double NOT NULL DEFAULT 0 COMMENT '税率',
  `buyer_remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '购买人备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';

DROP TABLE if exists `erp_order_product`;
CREATE TABLE `erp_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `rent_type` int(11) NOT NULL DEFAULT '0' COMMENT '租赁方式，1按天租，2按月租',
  `rent_time_length` int(11) NOT NULL DEFAULT '0' COMMENT '租赁期限',
  `rent_length_type` int(11) COMMENT '租赁期限类型，1短租，2长租',
  `product_id` int(20) COMMENT '商品ID',
  `product_name` varchar(100) COLLATE utf8_bin COMMENT '商品名称',
  `product_sku_id` int(20) COMMENT '商品SKU ID',
  `product_sku_name` varchar(100) COLLATE utf8_bin COMMENT '商品SKU名称',
  `product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `product_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品价格',
  `rent_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '租赁押金金额',
  `deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '押金金额',
  `credit_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '授信押金金额',
  `insurance_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '保险金额',
  `product_sku_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照',
  `deposit_cycle` int(11) NOT NULL DEFAULT 0 COMMENT '押金期数',
  `payment_cycle` int(11) NOT NULL DEFAULT 0 COMMENT '付款期数',
  `pay_mode` int(11) NOT NULL DEFAULT '0' COMMENT '支付方式：1先用后付，2先付后用',
  `is_new_product` int(11) NOT NULL DEFAULT 0 COMMENT '是否是全新机，1是0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单商品项表';

DROP TABLE if exists `erp_order_product_equipment`;
CREATE TABLE `erp_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `order_product_id` int(20) NOT NULL COMMENT '订单项ID',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `rent_start_time` datetime NOT NULL COMMENT '起租时间',
  `expect_return_time` datetime DEFAULT NULL COMMENT '预计归还时间',
  `actual_return_time` datetime DEFAULT NULL COMMENT '实际归还时间',
  `product_equipment_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '设备单价',
  `expect_rent_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '预计租金',
  `actual_rent_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '实际租金',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单商品设备表';

DROP TABLE if exists `erp_order_material`;
CREATE TABLE `erp_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `rent_type` int(11) NOT NULL DEFAULT '0' COMMENT '租赁方式，1按天租，2按月租',
  `rent_time_length` int(11) NOT NULL DEFAULT '0' COMMENT '租赁期限',
  `rent_length_type` int(11) COMMENT '租赁期限类型，1短租，2长租',
  `material_id` int(20) COMMENT '配件ID',
  `material_name` varchar(100) COLLATE utf8_bin COMMENT '配件名称',
  `material_count` int(11) NOT NULL DEFAULT '0' COMMENT '配件总数',
  `material_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件价格',
  `rent_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '租赁押金金额',
  `deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '押金金额',
  `credit_deposit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '授信押金金额',
  `insurance_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '保险金额',
  `material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照',
  `deposit_cycle` int(11) NOT NULL DEFAULT 0 COMMENT '押金期数',
  `payment_cycle` int(11) NOT NULL DEFAULT 0 COMMENT '付款期数',
  `pay_mode` int(11) NOT NULL DEFAULT '0' COMMENT '支付方式：1先用后付，2先付后用',
  `is_new_material` int(11) NOT NULL DEFAULT 0 COMMENT '是否是全新机，1是0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单配件项表';

DROP TABLE if exists `erp_order_material_bulk`;
CREATE TABLE `erp_order_material_bulk` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `order_material_id` int(20) NOT NULL COMMENT '订单配件项ID',
  `bulk_material_id` int(20) NOT NULL COMMENT '设备ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `rent_start_time` datetime NOT NULL COMMENT '起租时间',
  `expect_return_time` datetime DEFAULT NULL COMMENT '预计归还时间',
  `actual_return_time` datetime DEFAULT NULL COMMENT '实际归还时间',
  `material_bulk_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `expect_rent_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '预计租金',
  `actual_rent_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '实际租金',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单配件散料表';

DROP TABLE if exists `erp_order_consign_info`;
CREATE TABLE `erp_order_consign_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `customer_consign_id` int(20) NOT NULL COMMENT '客户关联地址ID',
  `consignee_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '收货人手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单收货地址表';

DROP TABLE if exists `erp_order_time_axis`;
CREATE TABLE `erp_order_time_axis` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态，0-待提交,4-审核中,8-待发货,12-处理中,16-已发货,20-确认收货,24-全部归还,28-取消,32-结束',
  `generation_time` datetime NOT NULL COMMENT '产生时间',
  `order_snapshot` mediumtext COMMENT '订单快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单时间轴表';

-- -------------------------------------货物调拨单（库房间调拨）-------------------------------------------------
DROP TABLE if exists `erp_deployment_order`;
CREATE TABLE `erp_deployment_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `deployment_order_no` varchar(100) NOT NULL COMMENT '调配单编号',
  `deployment_type` int(11) NOT NULL COMMENT '调配类型，1借调，2售调',
  `src_warehouse_id` int(20) COMMENT '源仓库ID',
  `src_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '源仓位ID',
  `target_warehouse_id` int(20) NOT NULL COMMENT '目标仓库ID',
  `target_warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '目标仓位ID',
  `deployment_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '调配单状态，0未提交，1审批中，2处理中，3确认收货',
  `total_product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `total_product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品总价',
  `total_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '配件总数',
  `total_material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件总价',
  `total_order_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '订单总价',
  `total_discount_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '共计优惠金额',
  `expect_return_time` datetime DEFAULT NULL COMMENT '预计归还时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_deployment_order_no` (`deployment_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='货物库房间调配单';


DROP TABLE if exists `erp_deployment_order_product`;
CREATE TABLE `erp_deployment_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `deployment_order_id` int(20) NOT NULL COMMENT '调拨单ID',
  `deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `deployment_product_sku_id` int(20) NOT NULL  COMMENT '货物调拨单商品SKU_ID',
  `deployment_product_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `deployment_product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品总价格',
  `deployment_product_sku_count` int(11) NOT NULL DEFAULT 0 COMMENT '货物调拨单商品SKU数量',
  `deployment_product_sku_snapshot` text COMMENT '货物调拨单商品SKU快照',
  `is_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否全新，1是，0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='货物调拨商品项表';

DROP TABLE if exists `erp_deployment_order_material`;
CREATE TABLE `erp_deployment_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `deployment_material_id` int(20) NOT NULL COMMENT '货物调拨配件ID',
  `deployment_material_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `deployment_material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件总价格',
  `deployment_product_material_count` int(11) COMMENT '货物调拨配件数量',
  `deployment_product_material_snapshot` text COMMENT '货物调拨配件快照',
  `is_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否全新，1是，0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='货物调拨配件项表';

DROP TABLE if exists `erp_deployment_order_product_equipment`;
CREATE TABLE `erp_deployment_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `deployment_order_product_id` int(20) NOT NULL COMMENT '货物调拨商品项ID',
  `deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号',
  `return_time` datetime DEFAULT NULL COMMENT '退还时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='货物调拨商品项设备表';

DROP TABLE if exists `erp_deployment_order_material_bulk`;
CREATE TABLE `erp_deployment_order_material_bulk` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `deployment_order_material_id` int(20) NOT NULL COMMENT '货物调拨配件项ID',
  `deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `bulk_material_id` int(20) NOT NULL COMMENT '散料ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料编号',
  `return_time` datetime DEFAULT NULL COMMENT '退还时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='货物调拨配件项散料表';


-- -------------------------------------同行货物调拨单（同行间调拨）-------------------------------------------------
DROP TABLE if exists `erp_peer_deployment_order`;
CREATE TABLE `erp_peer_deployment_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_deployment_order_no` varchar(100) NOT NULL COMMENT '同行调配单编号',
  `peer_id` int(20) NOT NULL COMMENT '同行ID',
  `rent_start_time` datetime COMMENT '起租时间',
  `rent_type` int(11) NOT NULL DEFAULT '0' COMMENT '租赁方式，1按天租，2按月租',
  `rent_time_length` int(11) NOT NULL DEFAULT '0' COMMENT '租赁期限',
  `warehouse_id` int(20) NOT NULL COMMENT '目标仓库ID',
  `warehouse_position_id` int(20) NOT NULL DEFAULT 0 COMMENT '目标仓位ID',
  `delivery_mode` int(11) COMMENT '发货方式，1快递，2自提',
  `tax_rate` double NOT NULL DEFAULT 0 COMMENT '税率',
  `peer_deployment_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '调配单状态，0未提交，4审批中，8处理中，16确认收货，20退回审批中，24退回处理中，28已退回，32取消',
  `total_product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `total_product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品总价',
  `total_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '配件总数',
  `total_material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件总价',
  `total_order_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '订单总价',
  `total_discount_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '共计优惠金额',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `expect_return_time` datetime DEFAULT NULL COMMENT '预计归还时间',
  `real_return_time` datetime DEFAULT NULL COMMENT '实际归还时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_peer_deployment_order_no` (`peer_deployment_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=9000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行货物调配单';

DROP TABLE if exists `erp_peer_deployment_order_product`;
CREATE TABLE `erp_peer_deployment_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_deployment_order_id` int(20) NOT NULL COMMENT '调拨单ID',
  `peer_deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `product_sku_id` int(20) NOT NULL  COMMENT '货物调拨单商品SKU_ID',
  `product_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品总价格',
  `product_sku_count` int(11) NOT NULL DEFAULT 0 COMMENT '货物调拨单商品SKU数量',
  `product_sku_snapshot` text COMMENT '货物调拨单商品SKU快照',
  `is_new` int(11) NOT NULL DEFAULT 0 COMMENT '是否全新机',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行货物调拨商品项表';

DROP TABLE if exists `erp_peer_deployment_order_material`;
CREATE TABLE `erp_peer_deployment_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `peer_deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `material_id` int(20) NOT NULL COMMENT '货物调拨配件ID',
  `material_unit_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件总价格',
  `material_count` int(11) COMMENT '货物调拨配件数量',
  `material_snapshot` text COMMENT '货物调拨配件快照',
  `is_new` int(11) NOT NULL DEFAULT 0 COMMENT '是否全新机',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行货物调拨配件项表';

DROP TABLE if exists `erp_peer_deployment_order_product_equipment`;
CREATE TABLE `erp_peer_deployment_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_deployment_order_product_id` int(20) NOT NULL COMMENT '货物调拨商品项ID',
  `peer_deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `peer_deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号',
  `return_time` datetime DEFAULT NULL COMMENT '退还时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `index_peer_deployment_order_id` (`peer_deployment_order_id`),
  KEY `index_equipment_id` (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行货物调拨商品项设备表';

DROP TABLE if exists `erp_peer_deployment_order_material_bulk`;
CREATE TABLE `erp_peer_deployment_order_material_bulk` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_deployment_order_material_id` int(20) NOT NULL COMMENT '货物调拨配件项ID',
  `peer_deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `peer_deployment_order_no` varchar(100) NOT NULL COMMENT '货物调拨单编号',
  `bulk_material_id` int(20) NOT NULL COMMENT '散料ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料编号',
  `return_time` datetime DEFAULT NULL COMMENT '退还时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行货物调拨配件项散料表';

DROP TABLE if exists `erp_peer_deployment_order_consign_info`;
CREATE TABLE `erp_peer_deployment_order_consign_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `peer_deployment_order_id` int(20) NOT NULL COMMENT '货物调拨单ID',
  `contact_name` varchar(64) COLLATE utf8_bin COMMENT '联系人姓名',
  `contact_phone` varchar(24) CHARACTER SET ascii COMMENT '联系人手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='同行货物调拨单收货地址表';

-- -------------------------------------租赁退还订单-------------------------------------------------
DROP TABLE if exists `erp_return_order`;
CREATE TABLE `erp_return_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_order_no` varchar(100) NOT NULL COMMENT '退还编号',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `customer_no` varchar(100) NOT NULL COMMENT '客户编号',
  `is_charging` int(11) NOT NULL COMMENT '是否计租赁费用',
  `total_return_product_count` int(11) NOT NULL DEFAULT '0' COMMENT '退还商品总数',
  `total_return_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '退还配件总数',
  `real_total_return_product_count` int(11) NOT NULL DEFAULT '0' COMMENT '实际退还商品总数',
  `real_total_return_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '实际退还配件总数',
  `total_rent_cost` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '租赁期间产生总费用',
  `service_cost` decimal(15,2)  COMMENT '服务费',
  `damage_cost` decimal(15,2) COMMENT '损坏加收费用',
  `is_damage` int(11) COMMENT '是否有损坏',
  `return_mode` int(11) NOT NULL COMMENT '退还方式，1-上门取件，2邮寄',
  `return_reason_type` int(11) NOT NULL COMMENT '退还原因类型：1-客户方设备不愿或无法退还，2-期满正常收回，3-提前退租，4-未按时付款或风险等原因上门收回，5-设备故障等我方原因导致退货，6-主观因素等客户方原因导致退货，7-更换设备，8-公司倒闭，9-设备闲置，10-满三个月或六个月随租随还，11-其它',
  `return_time` datetime NOT NULL COMMENT '退还时间',
  `return_order_status` int(11) NOT NULL DEFAULT 0 COMMENT '归还订单状态，0-待提交，4-审核中，8-待取货，12-处理中，16-已取消，20-已完成',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_return_no` (`return_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁退还订单表';

DROP TABLE if exists `erp_return_order_product`;
CREATE TABLE `erp_return_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_order_id` int(20) NOT NULL COMMENT '退还ID',
  `return_order_no` varchar(100) NOT NULL COMMENT '退还编号',
  `return_product_sku_id` int(20) NOT NULL  COMMENT '退还商品SKU_ID',
  `return_product_sku_count` int(11) NOT NULL DEFAULT 0 COMMENT '退还商品SKU数量',
  `real_return_product_sku_count` int(11) NOT NULL DEFAULT 0 COMMENT '实际退还商品数量',
  `return_product_sku_snapshot` text COMMENT '退还商品SKU快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁退还商品项表';

DROP TABLE if exists `erp_return_order_material`;
CREATE TABLE `erp_return_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_order_id` int(20) NOT NULL COMMENT '退还ID',
  `return_order_no` varchar(100) NOT NULL COMMENT '退还编号',
  `return_material_id` int(20) NOT NULL COMMENT '退还配件ID',
  `return_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '退还配件数量',
  `real_return_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '实际退还配件数量',
  `return_material_snapshot` text COMMENT '退还配件快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁退还配件项表';

DROP TABLE if exists `erp_return_order_product_equipment`;
CREATE TABLE `erp_return_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_order_product_id` int(20) NOT NULL COMMENT '租赁退还商品项ID',
  `return_order_id` int(20) NOT NULL COMMENT '退还ID',
  `return_order_no` varchar(100) NOT NULL COMMENT '退还编号',
  `order_no` varchar(100) COMMENT '订单编号',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁退还商品项设备表';

DROP TABLE if exists `erp_return_order_material_bulk`;
CREATE TABLE `erp_return_order_material_bulk` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_order_material_id` int(20) NOT NULL COMMENT '租赁退还配件项ID',
  `return_order_id` int(20) NOT NULL COMMENT '退还ID',
  `return_order_no` varchar(100) NOT NULL COMMENT '退还编号',
  `order_no` varchar(100) COMMENT '订单编号',
  `bulk_material_id` int(20) NOT NULL COMMENT '散料ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁退还配件项散料表';

DROP TABLE if exists `erp_return_order_consign_info`;
CREATE TABLE `erp_return_order_consign_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `return_order_id` int(20) NOT NULL COMMENT '退还ID',
  `return_order_no` varchar(100) NOT NULL COMMENT '退还编号',
  `consignee_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '收货人手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁退还订单地址表';

-- -------------------------------------租赁退还订单-------------------------------------------------
-- -------------------------------------租赁换货订单-------------------------------------------------
DROP TABLE if exists `erp_change_order`;
CREATE TABLE `erp_change_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `change_order_no` varchar(100) NOT NULL COMMENT '换货编号',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `customer_no` varchar(100) NOT NULL COMMENT '客户编号',
  `rent_start_time` datetime COMMENT '起租时间',
  `total_change_product_count` int(11) NOT NULL DEFAULT '0' COMMENT '换货商品总数',
  `total_change_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '换货配件总数',
  `real_total_change_product_count` int(11) NOT NULL DEFAULT '0' COMMENT '实际换货商品总数',
  `real_total_change_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '实际换货配件总数',
  `total_price_diff` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '总差价（目标总本身价值-原总本身价值）',
  `service_cost` decimal(15,2) COMMENT '服务费',
  `damage_cost` decimal(15,2) COMMENT '损坏加收费用',
  `is_damage` int(11) COMMENT '是否有损坏，0否1是',
  `change_reason_type` int(11) NOT NULL DEFAULT 0 COMMENT '换货原因类型,0-升级 ，1-损坏，2-其他',
  `change_reason` text COMMENT '换货原因',
  `change_mode` int(11) NOT NULL COMMENT '换货方式，1-上门取件，2邮寄',
  `change_order_status` int(11) NOT NULL DEFAULT 0 COMMENT '换货订单状态，0-待提交，4-审核中，8-待备货，12-备货中，16-已发货待取货，20-处理中，24-已完成',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '换货跟单员',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_change_no` (`change_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁换货订单表';

DROP TABLE if exists `erp_change_order_product`;
CREATE TABLE `erp_change_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `change_order_id` int(20) NOT NULL COMMENT '换货ID',
  `change_order_no` varchar(100) NOT NULL COMMENT '换货编号',
  `src_change_product_sku_id` int(20) NOT NULL  COMMENT '原商品SKU_ID',
  `dest_change_product_sku_id` int(20) NOT NULL  COMMENT '目标商品SKU_ID',
  `change_product_sku_count` int(11) NOT NULL DEFAULT 0 COMMENT '换货商品SKU数量',
  `real_change_product_sku_count` int(11) NOT NULL DEFAULT 0 COMMENT '实际换货数量',
  `src_change_product_sku_snapshot` text COMMENT '原商品SKU快照',
  `dest_change_product_sku_snapshot` text COMMENT '目标商品SKU快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁换货商品项表';

DROP TABLE if exists `erp_change_order_product_equipment`;
CREATE TABLE `erp_change_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `change_order_product_id` int(20) NOT NULL COMMENT '租赁换货商品项ID',
  `change_order_id` int(20) NOT NULL COMMENT '换货ID',
  `change_order_no` varchar(100) NOT NULL COMMENT '换货编号',
  `order_no` varchar(100) COMMENT '订单编号',
  `src_equipment_id` int(20) COMMENT '原设备ID',
  `src_equipment_no` varchar(100)  COMMENT '原设备编号',
  `dest_equipment_id` int(20) NOT NULL COMMENT '目标设备ID',
  `dest_equipment_no` varchar(100) NOT NULL COMMENT '目标设备编号',
  `price_diff` decimal(15,2) DEFAULT 0 COMMENT '差价，可以是正值或负值，差价计算标准为每月',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁换货商品项设备表';

DROP TABLE if exists `erp_change_order_material`;
CREATE TABLE `erp_change_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `change_order_id` int(20) NOT NULL COMMENT '换货ID',
  `change_order_no` varchar(100) NOT NULL COMMENT '换货编号',
  `src_change_material_id` int(20) NOT NULL  COMMENT '原配件ID',
  `dest_change_material_id` int(20) NOT NULL  COMMENT '目标配件ID',
  `change_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '换货配件数量',
  `real_change_material_count` int(11) NOT NULL DEFAULT 0 COMMENT '实际换货配件数量',
  `src_change_material_snapshot` text COMMENT '原配件快照',
  `dest_change_material_snapshot` text COMMENT '目标配件快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁换货配件项表';

DROP TABLE if exists `erp_change_order_material_bulk`;
CREATE TABLE `erp_change_order_material_bulk` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `change_order_material_id` int(20) NOT NULL COMMENT '租赁换货配件项ID',
  `change_order_id` int(20) NOT NULL COMMENT '换货ID',
  `change_order_no` varchar(100) NOT NULL COMMENT '换货编号',
  `src_equipment_id` int(20) COMMENT '原设备ID,如果更换的散料是租赁设备上的，此字段有值',
  `src_equipment_no` varchar(100) COMMENT '原设备编号,如果更换的散料是租赁设备上的，此字段有值',
  `src_bulk_material_id` int(20) COMMENT '原散料ID',
  `src_bulk_material_no` varchar(100) COMMENT '原散料编号',
  `dest_bulk_material_id` int(20) NOT NULL COMMENT '目标散料ID',
  `dest_bulk_material_no` varchar(100) NOT NULL COMMENT '目标散料编号',
  `order_no` varchar(100) COMMENT '订单编号',
  `price_diff` decimal(15,2) DEFAULT 0 COMMENT '差价，可以是正值或负值，差价计算标准为每月',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁换货配件项散料表';

DROP TABLE if exists `erp_change_order_consign_info`;
CREATE TABLE `erp_change_order_consign_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `change_order_id` int(20) NOT NULL COMMENT '换货ID',
  `change_order_no` varchar(100) NOT NULL COMMENT '换货编号',
  `consignee_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '收货人手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，省份ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应城市ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应区ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租赁换货订单地址表';

-- -------------------------------------租赁换货订单-------------------------------------------------
-- ****************************************订单模块**************************************** --

-- ****************************************支付模块**************************************** --

DROP TABLE if exists `erp_third_party_pay_record`;
CREATE TABLE `erp_third_party_pay_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `record_type` int(11) DEFAULT NULL COMMENT '记录类型，1订单支付记录，2退款记录等',
  `refer_order_id` varchar(100) NOT NULL COMMENT '关联ID，type为1则关联订单',
  `refer_id` varchar(100) COMMENT '关联其他ID，type为1则关联订单',
  `pay_type` int(11) NOT NULL DEFAULT '0' COMMENT '支付方式类型，1微信公众号',
  `record_status` int(11) NOT NULL DEFAULT '0' COMMENT '记录状态，0初始化，1扣款成功，2扣款失败，3支付结果未知，4审核未通过，5审核通过，6执行转账中，7全部转账成功，8部分转账成功，9全部转账失败',
  `record_status_desc` VARCHAR(64) NULL COMMENT '记录状态描述',
  `third_party_pay_order_id` VARCHAR(64) NULL COMMENT '第三方支付订单号',
  `pay_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '支付金额',
  `pay_fee` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '手续费',
  `record_ack` varchar(64) COMMENT '订单处理请求返回码',
  `record_ack_desc` text COMMENT '订单处理请求返回信息',
  `return_text` text COMMENT '支付返回结果信息',
  `end_time` datetime NULL COMMENT '结束时间，即收到返回时间',
  `verify_time` datetime NULL COMMENT '和第三方支付校验时间',
  `verify_result` int NOT NULL DEFAULT 0 COMMENT '校验结果，0=未校验，1=校验一致，2=校验不一致',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='第三方支付记录表';

DROP TABLE if exists `erp_statement_order`;
CREATE TABLE `erp_statement_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `statement_order_no` varchar(100) NOT NULL COMMENT '结算单编码',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `statement_expect_pay_time` date NOT NULL COMMENT '结算单预计支付时间',
  `statement_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '结算单金额，结算单明细总和',
  `statement_other_amount` decimal(15,2) DEFAULT 0 COMMENT '其他费用（运费等）',
  `statement_other_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已支付其他费用',
  `statement_rent_deposit_amount` decimal(15,2) DEFAULT 0 COMMENT '结算租金押金金额',
  `statement_rent_deposit_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已付租金押金金额',
  `statement_rent_deposit_return_amount` decimal(15,2) DEFAULT 0 COMMENT '退还租金押金金额',
  `statement_deposit_amount` decimal(15,2) DEFAULT 0 COMMENT '结算押金金额',
  `statement_deposit_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已付押金金额',
  `statement_deposit_return_amount` decimal(15,2) DEFAULT 0 COMMENT '退还押金金额',
  `statement_rent_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单租金金额',
  `statement_rent_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '租金已付金额',
  `statement_paid_time` datetime DEFAULT NULL COMMENT '结算单支付时间',
  `statement_overdue_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单逾期金额，结算单明细逾期总和',
  `statement_status` int(11) NOT NULL DEFAULT '0' COMMENT '结算状态，0未结算，1已结算',
  `statement_start_time` date NOT NULL COMMENT '结算开始时间，结算单明细最早的一个',
  `statement_end_time` date NOT NULL COMMENT '结算结束时间，结算单明细最晚的一个',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='结算单';

DROP TABLE if exists `erp_statement_order_return_detail`;
CREATE TABLE `erp_statement_order_return_detail` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `statement_order_id` int(20) NOT NULL COMMENT '结算单ID',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `order_item_refer_id` int(20) NOT NULL COMMENT '订单项ID',
  `return_type` int(20) NOT NULL COMMENT '退还类型，1退租金押金，2退押金。',
  `return_amount` decimal(15,2) DEFAULT 0 COMMENT '退还租金押金金额',
  `return_time` datetime DEFAULT NULL COMMENT '退还租金押金时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='结算单退还明细';

DROP TABLE if exists `erp_statement_order_detail`;
CREATE TABLE `erp_statement_order_detail` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `statement_order_id` int(20) NOT NULL COMMENT '结算单ID',
  `customer_id` int(20) NOT NULL COMMENT '客户ID',
  `order_type` int(20) NOT NULL COMMENT '单子类型，1是订单，2是调配单，3是换货单，4是退货单，5是维修单',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `order_item_type` int(20) NOT NULL COMMENT '订单项类型，1为商品，2为配件',
  `order_item_refer_id` int(20) NOT NULL COMMENT '订单项ID',
  `rent_length_type` int(11) COMMENT '租赁期限类型，1短租，2长租',
  `statement_detail_type` int(20) COMMENT '结算单明细类型',
  `statement_detail_phase` int(20) COMMENT '期数',
  `statement_detail_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '结算单总金额（租金+租金押金）',
  `statement_detail_other_amount` decimal(15,2) DEFAULT 0 COMMENT '其他费用（运费等）',
  `statement_detail_other_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已支付其他费用',
  `statement_detail_rent_deposit_amount` decimal(15,2) DEFAULT 0 COMMENT '结算租金押金金额',
  `statement_detail_rent_deposit_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已付租金押金金额',
  `statement_detail_rent_deposit_return_amount` decimal(15,2) DEFAULT 0 COMMENT '退还租金押金金额',
  `statement_detail_rent_deposit_return_time` datetime DEFAULT NULL COMMENT '退还租金押金时间',
  `statement_detail_deposit_amount` decimal(15,2) DEFAULT 0 COMMENT '结算押金金额',
  `statement_detail_deposit_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '已付押金金额',
  `statement_detail_deposit_return_amount` decimal(15,2) DEFAULT 0 COMMENT '退还押金金额',
  `statement_detail_deposit_return_time` decimal(15,2) DEFAULT 0 COMMENT '退还押金时间',
  `statement_detail_rent_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单租金金额',
  `statement_detail_rent_paid_amount` decimal(15,2) DEFAULT 0 COMMENT '租金已付金额',
  `statement_detail_paid_time` datetime DEFAULT NULL COMMENT '结算单支付时间',
  `statement_detail_overdue_amount` decimal(15,2) DEFAULT 0 COMMENT '结算单逾期金额',
  `statement_detail_status` int(11) NOT NULL DEFAULT '0' COMMENT '结算状态，0未结算，1已结算',
  `statement_expect_pay_time` datetime NOT NULL COMMENT '结算单预计支付时间',
  `statement_start_time` date NOT NULL COMMENT '结算开始时间',
  `statement_end_time` date NOT NULL COMMENT '结算结束时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='结算单明细';

DROP TABLE if exists `erp_statement_pay_order`;
CREATE TABLE `erp_statement_pay_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `statement_pay_order_no` varchar(100) NOT NULL COMMENT '结算单支付编码',
  `statement_order_id` int(20) NOT NULL COMMENT '结算单ID',
  `pay_type` int(11) NOT NULL DEFAULT '0' COMMENT '支付方式类型，1余额支付，2微信支付',
  `pay_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态，详见paystatus',
  `payment_order_no` varchar(100) COMMENT '支付系统支付编号',
  `pay_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  `pay_rent_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付租金金额',
  `pay_rent_deposit_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付租金押金金额',
  `pay_deposit_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付押金金额',
  `other_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付其他金额',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间，即发起支付时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间，即收到返回时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='结算单支付记录';

DROP TABLE if exists `erp_order_pay_plan`;
CREATE TABLE `erp_order_pay_plan` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `pay_plan_no` varchar(100) NOT NULL COMMENT '支付编号',
  `parent_pay_plan_no` varchar(100) COMMENT '关联的支付编号，从哪个生成的',
  `order_id` int(20) NOT NULL COMMENT '订单号',
  `pay_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态，0未支付，1支付中，2已经支付，3付款失败，4付款失效',
  `pay_time` datetime COMMENT '发起支付时间',
  `pay_time_plan` datetime NOT NULL COMMENT '原定还款时间',
  `pay_time_real` datetime COMMENT '实际还款时间',
  `expect_amount` decimal(15,2) NOT NULL COMMENT '预计支付金额',
  `pay_amount` decimal(15,2) COMMENT '实际支付金额',
  `overdue_amount` decimal(15,2) COMMENT '逾期金额',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单付款计划';


DROP TABLE if exists `erp_order_refund_record`;
CREATE TABLE `erp_order_refund_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `pay_no` varchar(100) NOT NULL COMMENT '支付编号',
  `refund_no` varchar(100) NOT NULL COMMENT '退款编号',
  `parent_refund_no` varchar(100) COMMENT '关联的退款编号，从哪个生成的',
  `order_id` int(20) NOT NULL COMMENT '订单号',
  `refund_message` varchar(500) COMMENT '退款信息（展示給客戶）',
  `refund_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态，0初始化，1退款中，2退款成功，3退款关闭，4退款异常',
  `refund_time` datetime COMMENT '发起支付时间',
  `refund_time_real` datetime COMMENT '实际还款时间',
  `total_amount` decimal(15,2) COMMENT '总金额',
  `refund_amount` decimal(15,2) COMMENT '实际支付金额',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='退款记录';

-- ****************************************采购模块**************************************** --

-- -----------------------------------------采购单----------------------------------------- --
DROP TABLE if exists `erp_purchase_order`;
CREATE TABLE `erp_purchase_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_no` varchar(100) NOT NULL COMMENT '采购单编号',
  `product_supplier_id` int(20) NOT NULL COMMENT '商品供应商ID',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `warehouse_snapshot` text NOT NULL COMMENT '收货方仓库快照，JSON格式',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `tax_rate` double NOT NULL DEFAULT 0 COMMENT '税率',
  `purchase_order_amount_total` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '采购单总价',
  `purchase_order_amount_real` decimal(15,2) COMMENT '采购单实收',
  `purchase_order_amount_statement` decimal(15,2) COMMENT '采购单结算金额',
  `purchase_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购单状态，0-待提交，3-审核中，6-采购中，9-部分采购，12-全部采购，15-结束采购，18-取消',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `purchase_type` int(11) NOT NULL COMMENT '采购类型：1-整机及四大件，2-小配件',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单表';

DROP TABLE if exists `erp_purchase_order_product`;
CREATE TABLE `erp_purchase_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID冗余',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余',
  `product_snapshot` text COMMENT '商品快照',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `product_count` int(11) NOT NULL DEFAULT '1' COMMENT '商品总数',
  `product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品项表';


DROP TABLE if exists `erp_purchase_order_material`;
CREATE TABLE `erp_purchase_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID冗余',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余',
  `material_snapshot` text COMMENT '配件快照',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '配件总数',
  `material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单配件项表';


DROP TABLE if exists `erp_purchase_order_product_material`;
CREATE TABLE `erp_purchase_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_product_id` int(20) NOT NULL COMMENT '采购单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余',
  `material_snapshot` text COMMENT '配件信息，防止商品修改留存快照',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '配件总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品配件表';

-- -----------------------------------------采购发货单----------------------------------------- --

DROP TABLE if exists `erp_purchase_delivery_order`;
CREATE TABLE `erp_purchase_delivery_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_delivery_no` varchar(100) NOT NULL COMMENT '采购发货单编号',
  `warehouse_id` int(20) NOT NULL COMMENT '收货方仓库ID',
  `warehouse_snapshot` text NOT NULL COMMENT '收货方仓库快照，JSON格式',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `purchase_delivery_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购发货单状态，0待发货，1已发货',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner_supplier_id` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购发货单表';

DROP TABLE if exists `erp_purchase_delivery_order_product`;
CREATE TABLE `erp_purchase_delivery_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_delivery_order_id` int(20) NOT NULL COMMENT '采购发货单ID',
  `purchase_order_product_id` int(20) NOT NULL COMMENT '采购单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID，来源（采购单），不可变更',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余，不可修改',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID 不可变更',
  `product_snapshot` text COMMENT '商品信息，防止商品修改留存快照，不可修改',
  `product_count` int(11) NOT NULL DEFAULT '1' COMMENT '商品总数，来源（采购单），不可变更',
  `real_product_id` int(20) NOT NULL COMMENT '实际商品ID',
  `real_product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余，可修改',
  `real_product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID 可修改',
  `real_product_snapshot` text COMMENT '商品信息，防止商品修改留存快照，可修改',
  `real_product_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际商品总数',
  `product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购发货单商品项表';

DROP TABLE if exists `erp_purchase_delivery_order_material`;
CREATE TABLE `erp_purchase_delivery_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_delivery_order_id` int(20) NOT NULL COMMENT '采购发货单ID',
  `purchase_order_material_id` int(20) NOT NULL COMMENT '采购单配件项ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID冗余',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余',
  `material_snapshot` text COMMENT '配件快照',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '配件总数',
  `material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `real_material_id` int(20) NOT NULL COMMENT '实际配件ID',
  `real_material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '实际配件ID名称',
  `real_material_snapshot` text COMMENT '实际配件快照',
  `real_material_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际配件总数',
  `real_material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '实际配件单价',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购发货单配件项表';

DROP TABLE if exists `erp_purchase_delivery_order_product_material`;
CREATE TABLE `erp_purchase_delivery_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_delivery_order_product_id` int(20) NOT NULL COMMENT '采购发货单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID，来源（采购单），不可变更',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余，不可修改',
  `material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照，不可修改',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '配件总数，来源（采购单），不可变更',
  `real_material_id` int(20) NOT NULL COMMENT '配件ID',
  `real_material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余，可修改',
  `real_material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照，可修改',
  `real_material_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际配件总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品配件表';

-- -----------------------------------------采购收货单----------------------------------------- --
DROP TABLE if exists `erp_purchase_receive_order`;
CREATE TABLE `erp_purchase_receive_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_delivery_order_id` int(20) NOT NULL COMMENT '采购发货单ID',
  `purchase_receive_no` varchar(100) NOT NULL COMMENT '采购收货单编号',
  `product_supplier_id` int(20) NOT NULL COMMENT '商品供应商ID',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `warehouse_snapshot` text NOT NULL COMMENT '收货方仓库快照，JSON格式',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `auto_allot_status` int(2) NOT NULL COMMENT '分拨情况，0-未分拨，1-已分拨，2-被分拨，没发票的分公司仓库单，将生成一个总公司收货单，并生成分拨单号，自动分拨到分公司仓库',
  `auto_allot_no` varchar(100) COMMENT '分拨单号，仅在is_auto_allot字段为1时有值',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `total_amount` decimal(15,2) DEFAULT 0 COMMENT '收料单采购总价',
  `purchase_receive_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购单收货状态，0待收货，1已签单，2已提交',
  `confirm_time` datetime DEFAULT NULL COMMENT '签单时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单表';

DROP TABLE if exists `erp_purchase_receive_order_product`;
CREATE TABLE `erp_purchase_receive_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_receive_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_product_id` int(20) DEFAULT NULL COMMENT '采购单项ID',
  `purchase_delivery_order_product_id` int(20) DEFAULT NULL COMMENT '采购发货单项ID',
  `product_id` int(20) DEFAULT NULL COMMENT '商品ID，来源（采购发货单），不可变更',
  `product_sku_id` int(20) DEFAULT NULL COMMENT '商品SKU ID 不可变更',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称冗余，不可修改',
  `product_snapshot` text DEFAULT NULL COMMENT '商品冗余信息，防止商品修改留存快照，不可修改',
  `product_count` int(11) DEFAULT '0' COMMENT '商品总数，来源（采购发货单），不可变更',
  `product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `real_product_id` int(20) DEFAULT NULL COMMENT '实际商品ID',
  `real_product_name` varchar(100) DEFAULT NULL  COLLATE utf8_bin COMMENT '商品名称冗余，可修改',
  `real_product_sku_id` int(20) DEFAULT NULL COMMENT '商品SKU ID',
  `real_product_snapshot` text DEFAULT NULL COMMENT '商品冗余信息，防止商品修改留存快照，可修改',
  `real_product_count` int(11) DEFAULT '0' COMMENT '实际商品总数',
  `is_src` int(11) NOT NULL DEFAULT '0' COMMENT '原单项标志，0-收货新添项，1-原单项',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单商品项表';

DROP TABLE if exists `erp_purchase_receive_order_material`;
CREATE TABLE `erp_purchase_receive_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_receive_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_material_id` int(20) DEFAULT NULL COMMENT '采购单配件项ID',
  `purchase_delivery_order_material_id` int(20) DEFAULT NULL COMMENT '采购发货单配件项ID',
  `material_id` int(20) DEFAULT NULL COMMENT '配件ID冗余',
  `material_name` varchar(100) DEFAULT NULL COMMENT '配件名称冗余',
  `material_snapshot` text DEFAULT NULL COMMENT '配件快照',
  `material_count` int(11) DEFAULT NULL COMMENT '配件总数',
  `material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `real_material_id` int(20) NOT NULL COMMENT '实际配件ID',
  `real_material_name` varchar(100) NOT NULL COMMENT '实际配件ID名称',
  `real_material_snapshot` text COMMENT '实际配件快照',
  `real_material_count` int(11) NOT NULL DEFAULT '0' COMMENT '实际配件总数',
  `is_src` int(11) NOT NULL DEFAULT '0' COMMENT '原单项标志，查原单时此标志要传入0，0-收货新添项，1-原单项',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单配件项表';

DROP TABLE if exists `erp_purchase_receive_order_product_material`;
CREATE TABLE `erp_purchase_receive_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_receive_order_product_id` int(20) NOT NULL COMMENT '采购收货单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '预计配件ID，来源（采购发货单），不可变更',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余，不可修改',
  `material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照，不可修改',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '预计配件总数，来源（采购发货单），不可变更',
  `material_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '配件单价',
  `real_material_id` int(20) NOT NULL COMMENT '实际配件ID',
  `real_material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余，可修改',
  `real_material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照，可修改',
  `real_material_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际配件总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单商品配件表';

-- -----------------------------------------采购申请单----------------------------------------- --
DROP TABLE if exists `erp_purchase_apply_order`;
CREATE TABLE `erp_purchase_apply_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_apply_order_no` varchar(100) NOT NULL COMMENT '申请单编号',
  `apply_user_id` int(20) NOT NULL DEFAULT 0 COMMENT  '申请人ID',
  `warehouse_id` int(20) NOT NULL DEFAULT 0 COMMENT  '仓库ID，总公司可以给分公司采购，申请人为分公司，则分公司只允许采购到本分公司仓库',
  `department_id` int(20) NOT NULL DEFAULT 0 COMMENT  '部门ID',
  `purchase_apply_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购申请单状态，0-待提交，3-审核中，6-待采购，9-采购中，12-部分采购，15-全部采购，18-取消，21-暂停，24-结束',
  `all_use_time` datetime DEFAULT NULL COMMENT '整单计划使用时间',
  `purchase_start_time` datetime DEFAULT NULL COMMENT '采购开始时间',
  `purchase_end_time` datetime DEFAULT NULL COMMENT '全部采购完成时间',
  `end_reason` varchar(500) DEFAULT NULL COMMENT '结束原因',
  `apply_product_total_count` int(20) NOT NULL DEFAULT 0 COMMENT '计划采购商品总数',
  `real_product_total_count` int(20) NOT NULL DEFAULT 0  COMMENT '实际采购商品总数',
  `purchasing_product_total_count` int(20) NOT NULL DEFAULT 0 COMMENT '采购中商品总数',
  `apply_material_total_count` int(20) NOT NULL DEFAULT 0  COMMENT '计划采购配件总数',
  `real_material_total_count` int(20) NOT NULL DEFAULT 0 COMMENT '实际采购配件总数',
  `purchasing_material_total_count` int(20) NOT NULL DEFAULT 0  COMMENT '采购中配件总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购申请单表';

DROP TABLE if exists `erp_purchase_apply_order_product`;
CREATE TABLE `erp_purchase_apply_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_apply_order_id` int(20) NOT NULL COMMENT '采购申请单ID',
  `purchase_apply_order_no` varchar(100) NOT NULL COMMENT '采购申请单编号',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `product_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照，不可修改',
  `apply_count` int(20) NOT NULL DEFAULT 0 COMMENT '计划采购数量',
  `real_count` int(20) NOT NULL DEFAULT 0  COMMENT '实际采购数量',
  `purchasing_count` int(20) NOT NULL DEFAULT 0  COMMENT '采购中数量',
  `lock_count` int(20) NOT NULL DEFAULT 0  COMMENT '锁定数量',
  `purchase_apply_order_item_status` int(20) NOT NULL DEFAULT 0 COMMENT '单项状态，0-待采购，3-采购中，6-部分采购，9-全部采购',
  `is_new` int(11) NOT NULL DEFAULT 0 COMMENT '是否全新，1是，0否',
  `use_time` datetime NOT NULL COMMENT '计划使用时间',
  `purchase_start_time` datetime DEFAULT NULL COMMENT '采购开始时间',
  `purchase_end_time` datetime DEFAULT NULL COMMENT '全部采购完成时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购申请单商品项表';

DROP TABLE if exists `erp_purchase_apply_order_material`;
CREATE TABLE `erp_purchase_apply_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_apply_order_id` int(20) NOT NULL COMMENT '采购申请单ID',
  `purchase_apply_order_no` varchar(100) NOT NULL COMMENT '采购申请单编号',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_no` varchar(100) NOT NULL COMMENT '配件编号',
  `material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照，不可修改',
  `apply_count` int(20) NOT NULL DEFAULT 0 COMMENT '计划采购数量',
  `real_count` int(20) NOT NULL DEFAULT 0  COMMENT '实际采购数量',
  `purchasing_count` int(20) NOT NULL DEFAULT 0  COMMENT '采购中数量',
  `lock_count` int(20) NOT NULL DEFAULT 0  COMMENT '锁定数量',
  `purchase_start_time` datetime DEFAULT NULL COMMENT '采购开始时间',
  `purchase_end_time` datetime DEFAULT NULL COMMENT '全部采购完成时间',
  `purchase_apply_order_item_status` int(20) NOT NULL DEFAULT 0 COMMENT '单项状态，0-待采购，3-采购中，6-部分采购，9-全部采购',
  `is_new` int(11) NOT NULL DEFAULT 0 COMMENT '是否全新，1是，0否',
  `use_time` datetime NOT NULL COMMENT '计划使用时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购申请单配件项表';

DROP TABLE if exists `erp_purchase_apply_product_relation`;
CREATE TABLE `erp_purchase_apply_product_relation` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_no` varchar(100) NOT NULL COMMENT '采购单编号',
  `purchase_order_product_id` int(20) NOT NULL COMMENT '采购单商品项ID',
  `purchase_apply_order_product_id` int(20) NOT NULL COMMENT '采购申请单商品项ID',
  `apply_count` int(20) NOT NULL DEFAULT 0  COMMENT '预计采购数量',
  `real_count` int(20) NOT NULL DEFAULT 0  COMMENT '实际采购数量',
  `purchase_start_time` datetime DEFAULT NULL COMMENT '采购开始时间',
  `purchase_end_time` datetime DEFAULT NULL COMMENT '全部采购完成时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品项-采购申请单商品项映射表';

DROP TABLE if exists `erp_purchase_apply_material_relation`;
CREATE TABLE `erp_purchase_apply_material_relation` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_no` varchar(100) NOT NULL COMMENT '采购单编号',
  `purchase_order_material_id` int(20) NOT NULL COMMENT '采购单配件项ID',
  `purchase_apply_order_material_id` int(20) NOT NULL COMMENT '采购申请单配件项ID',
  `apply_count` int(20) NOT NULL DEFAULT 0  COMMENT '预计采购数量',
  `real_count` int(20) NOT NULL DEFAULT 0  COMMENT '实际采购数量',
  `purchase_start_time` datetime DEFAULT NULL COMMENT '采购开始时间',
  `purchase_end_time` datetime DEFAULT NULL COMMENT '全部采购完成时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单配件项-采购申请单配件项映射表';

-- -----------------------------------------采购退货单----------------------------------------- --
DROP TABLE if exists `erp_purchase_back_order`;
CREATE TABLE `erp_purchase_back_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_back_no` varchar(100) NOT NULL COMMENT '采购退货单编号',
  `supplier_id` int(20) NOT NULL COMMENT '商品供应商ID',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `purchase_order_amount_total` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '采购单总价',
  `purchase_back_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购退货单状态，0待退货，1已退回',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购退货单表';

DROP TABLE if exists `erp_purchase_back_order_product`;
CREATE TABLE `erp_purchase_back_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_back_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_product_id` int(20) NOT NULL COMMENT '采购单项ID',
  `purchase_delivery_order_product_id` int(20) NOT NULL COMMENT '采购发货单项ID',
  `purchase_receive_order_product_id` int(20) NOT NULL COMMENT '采购收货单项ID',
  `equipment_id` int(20) COMMENT '设备ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID，来源（采购收货单），不可变更',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID 不可变更',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余，不可修改',
  `product_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照，不可修改',
  `product_count` int(11) NOT NULL DEFAULT '1' COMMENT '商品总数，来源（采购收货单），不可变更',
  `product_amount` decimal(15,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购退货单商品项表';

DROP TABLE if exists `erp_purchase_back_order_product_material`;
CREATE TABLE `erp_purchase_back_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_back_order_product_id` int(20) NOT NULL COMMENT '采购退货单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '预计配件ID，来源（采购收货单），不可变更',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配件名称冗余，不可修改',
  `material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照，不可修改',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '预计配件总数，来源（采购收货单），不可变更',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购退货单商品配件表';

DROP TABLE if exists `erp_message`;
CREATE TABLE `erp_message` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `sender_user_id` int(20) NOT NULL COMMENT '发送人ID',
  `receiver_user_id` int(20) NOT NULL COMMENT '接收人ID',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `read_time` datetime COMMENT '读取时间',
  `title` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '标题',
  `message_text` text COMMENT '发送内容',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='站内信表';

DROP TABLE if exists `erp_joint_product`;
CREATE TABLE `erp_joint_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `joint_product_name` varchar(64) NOT NULL COMMENT '组合商品名称',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组合商品表';

DROP TABLE if exists `erp_joint_product_sku`;
CREATE TABLE `erp_joint_product_sku` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `joint_product_id` int(20) NOT NULL COMMENT '组合商品ID',
  `sku_id` int(20) NOT NULL COMMENT 'SKU_ID',
  `sku_count` int(11) NOT NULL COMMENT 'sku数量',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组合商品sku项表';

DROP TABLE if exists `erp_joint_material`;
CREATE TABLE `erp_joint_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `joint_product_id` int(20) NOT NULL COMMENT '组合商品ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_no` varchar(100) NOT NULL COMMENT '配件编号',
  `material_count` int(11) NOT NULL COMMENT '配件数量',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组合商品配件项表';

DROP TABLE if exists `erp_assemble_order`;
CREATE TABLE `erp_assemble_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `assemble_order_no` varchar(100) NOT NULL COMMENT '组装单编号',
  `assemble_product_id` int(20) NOT NULL COMMENT '组装商品ID',
  `assemble_product_sku_id` int(20) NOT NULL COMMENT '组装商品SKU ID',
  `assemble_product_count` int(20) NOT NULL COMMENT '组装商品数量',
  `warehouse_id` int(20) DEFAULT NULL COMMENT '仓库ID，为哪个库房组装',
  `assemble_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '组装单状态，0初始化，4组装成功，8取消组装',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组装单表';

DROP TABLE if exists `erp_assemble_order_material`;
CREATE TABLE `erp_assemble_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `assemble_order_id` int(20) NOT NULL COMMENT '组装单ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_no` varchar(100) NOT NULL COMMENT '配件编号',
  `material_count` int(11) NOT NULL COMMENT '配件数量',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组装单配件项表';

DROP TABLE if exists `erp_assemble_order_product_equipment`;
CREATE TABLE `erp_assemble_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `assemble_order_id` int(20) NOT NULL COMMENT '组装单ID',
  `product_equipment_no` varchar(100) NOT NULL COMMENT '商品设备唯一编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='组装单组装成功设备表';

DROP TABLE if exists `erp_transfer_order`;
CREATE TABLE `erp_transfer_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `transfer_order_no` varchar(100) NOT NULL COMMENT '转移单编号',
  `transfer_order_name` varchar(100) NOT NULL COMMENT '转移单名称',
  `transfer_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '转移单状态，0初始化，4审批中，8转移成功，16取消转移',
  `transfer_order_mode` int(11) NOT NULL COMMENT '转移方式，1转入，2转出（凭空转入转出）',
  `transfer_order_type` int(11) NOT NULL COMMENT '	转入类型：1外借入库转入，2试验机转入，3原有资产，99其他。 转出类型：51丢失，52售出，99其他',
  `warehouse_id` int(20) DEFAULT NULL COMMENT '仓库ID，哪个库房转移',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='转移单表';

DROP TABLE if exists `erp_transfer_order_product`;
CREATE TABLE `erp_transfer_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `transfer_order_id` int(20) NOT NULL COMMENT '转移单ID',
  `product_id` int(20) NOT NULL COMMENT '转移商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '转移商品SKU ID',
  `product_count` int(11) NOT NULL COMMENT '配件数量',
  `product_sku_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照',
  `is_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否全新，1是，0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='转移单商品表';

DROP TABLE if exists `erp_transfer_order_material`;
CREATE TABLE `erp_transfer_order_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `transfer_order_id` int(20) NOT NULL COMMENT '转移单ID',
  `material_id` int(20) NOT NULL COMMENT '配件ID',
  `material_no` varchar(100) NOT NULL COMMENT '配件编号',
  `material_count` int(11) NOT NULL COMMENT '配件数量',
  `material_snapshot` text COMMENT '配件冗余信息，防止商品修改留存快照',
  `is_new` int(11) NOT NULL DEFAULT '0' COMMENT '是否全新，1是，0否',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='转移单配件表';

DROP TABLE if exists `erp_transfer_order_product_equipment`;
CREATE TABLE `erp_transfer_order_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `transfer_order_id` int(20) NOT NULL COMMENT '转移单ID',
  `transfer_order_product_id` int(20) NOT NULL COMMENT '转移单商品项ID',
  `product_equipment_no` varchar(100) NOT NULL COMMENT '商品设备唯一编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='转移单商品设备表';

DROP TABLE if exists `erp_transfer_order_material_bulk`;
CREATE TABLE `erp_transfer_order_material_bulk` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `transfer_order_id` int(20) NOT NULL COMMENT '转移单ID',
  `transfer_order_material_id` int(20) NOT NULL COMMENT '转移单ID',
  `bulk_material_no` varchar(100) NOT NULL COMMENT '散料唯一编号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `index_bulk_material_no` (`bulk_material_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='转移单配件散料表';

DROP TABLE if exists `erp_k3_mapping_category`;
CREATE TABLE `erp_k3_mapping_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_category_code` varchar(64) COMMENT 'erp的分类编码',
  `k3_category_code` varchar(64) COMMENT 'K3分类编码',
  `category_name` varchar(64) COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3分类映射';

DROP TABLE if exists `erp_k3_mapping_brand`;
CREATE TABLE `erp_k3_mapping_brand` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_brand_code` varchar(64) COMMENT 'erp的品牌编码',
  `k3_brand_code` varchar(64) COMMENT 'K3品牌编码',
  `brand_name` varchar(64) COMMENT '品牌名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3品牌映射';

DROP TABLE if exists `erp_k3_mapping_customer`;
CREATE TABLE `erp_k3_mapping_customer` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_customer_code` varchar(64) COMMENT 'erp的客户编码',
  `k3_customer_code` varchar(64) COMMENT 'K3客户编码',
  `customer_name` varchar(64) COMMENT '客户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3客户映射';

DROP TABLE if exists `erp_k3_mapping_supplier`;
CREATE TABLE `erp_k3_mapping_supplier` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_supplier_code` varchar(64) COMMENT 'erp的供应商编码',
  `k3_supplier_code` varchar(64) COMMENT 'K3供应商编码',
  `supplier_name` varchar(64) COMMENT '供应商名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3供应商映射';

DROP TABLE if exists `erp_k3_mapping_product`;
CREATE TABLE `erp_k3_mapping_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_product_code` varchar(64) COMMENT 'erp商品编码',
  `k3_product_code` varchar(64) COMMENT 'K3商品编码',
  `product_name` varchar(64) COMMENT '商品名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3商品映射';

DROP TABLE if exists `erp_k3_mapping_material`;
CREATE TABLE `erp_k3_mapping_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_material_code` varchar(64) COMMENT 'erp商品编码',
  `k3_material_code` varchar(64) COMMENT 'K3商品编码',
  `material_name` varchar(64) COMMENT '商品名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3配件映射';

DROP TABLE if exists `erp_k3_mapping_material_type`;
CREATE TABLE `erp_k3_mapping_material_type` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `erp_material_type_code` varchar(64) COMMENT 'erp配件类型编码',
  `k3_material_type_code` varchar(64) COMMENT 'K3配件类型编码',
  `material_type_name` varchar(64) COMMENT '配件类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3配件类型映射';

DROP TABLE if exists `erp_k3_send_record`;
CREATE TABLE `erp_k3_send_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `record_type` int(20) COMMENT '记录类型，1客户、2供应商、3商品、4配件、5订单',
  `record_json` text COMMENT '推送的json数据',
  `response_json` text COMMENT '返回的json数据',
  `send_result` int(20) COMMENT '是否推送成功，1是0否',
  `receive_result` int(20) COMMENT '是否接收成功，1是0否',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='K3数据发送记录表';