CREATE DATABASE IF NOT EXISTS lxzl_erp DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
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
  `is_disabled` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否禁用，0不可用；1可用',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(40) CHARACTER SET ascii DEFAULT NULL COMMENT '最后登录IP',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=500001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理系统用户信息表';

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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=400001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='部门表';

DROP TABLE if exists `erp_sub_company`;
CREATE TABLE `erp_sub_company` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `sub_company_name` varchar(20) NOT NULL DEFAULT '' COMMENT '子公司名称',
  `is_header_company` int(11) NOT NULL COMMENT '是否是总公司，0否1是',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，对应字典ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应字典ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应字典ID',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=300001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据字典表';


DROP TABLE if exists `erp_supplier`;
CREATE TABLE `erp_supplier` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID，唯一',
  `supplier_name` varchar(100) NOT NULL DEFAULT '' COMMENT '供应商名称',
  `province` int(20) COMMENT '省份ID，对应字典ID',
  `city` int(20) COMMENT '城市ID，对应字典ID',
  `district` int(20) COMMENT '区ID，对应字典ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `tel` varchar(100) NOT NULL COMMENT '电话号码',
  `contactName` varchar(100) COMMENT '联系人姓名',
  `contactPhone` varchar(20) COMMENT '联系手机号',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='供应商表';


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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `workflow_department` int(20) NOT NULL COMMENT '本步骤可审批部门',
  `workflow_role` int(20) NOT NULL COMMENT '本步骤可审批角色',
  `workflow_user` int(20) NOT NULL COMMENT '本步骤可审批人员',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流节点表';

DROP TABLE if exists `erp_workflow_link`;
CREATE TABLE `erp_workflow_link` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `workflow_type` int(11) NOT NULL DEFAULT '0' COMMENT '工作流类型',
  `workflow_template_id` int(20) NOT NULL COMMENT '工作流模板ID',
  `workflow_refer_id` int(20) NOT NULL COMMENT '工作流关联ID',
  `workflow_step` int(20) NOT NULL COMMENT '流程当前步骤',
  `workflow_last_step` int(20) NOT NULL COMMENT '流程最后步骤',
  `workflow_current_node_id` int(20) NOT NULL COMMENT '当前结点ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_workflow_type_refer` (`workflow_type`,`workflow_refer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流线表';

DROP TABLE if exists `erp_workflow_link_detail`;
CREATE TABLE `erp_workflow_link_detail` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `workflow_link_id` int(20) NOT NULL COMMENT '工作流线ID',
  `workflow_refer_id` int(20) NOT NULL COMMENT '工作流关联ID',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流线明细表';

-- ****************************************商品模块**************************************** --

DROP TABLE if exists `erp_product_category`;
CREATE TABLE `erp_product_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `category_name` varchar(20) NOT NULL DEFAULT '' COMMENT '分类名称',
  `parent_category_id` int(20) NOT NULL DEFAULT 800000 COMMENT '父ID',
  `category_type` int(11) NOT NULL DEFAULT '0' COMMENT '分类类型',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=800001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='产品分类表';


DROP TABLE if exists `erp_product_brand`;
CREATE TABLE `erp_product_brand` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `brand_name` varchar(20) NOT NULL DEFAULT '' COMMENT '品牌名称',
  `brand_english_name` varchar(20) NOT NULL DEFAULT '' COMMENT '品牌英文名称',
  `brand_desc` varchar(500) COLLATE utf8_bin COMMENT '品牌描述',
  `brand_story` varchar(500) COLLATE utf8_bin COMMENT '品牌故事',
  `logo_url` varchar(200) NOT NULL DEFAULT '' COMMENT 'logo地址',
  `home_url` varchar(200) NOT NULL DEFAULT '' COMMENT '官网地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='产品品牌表';

DROP TABLE if exists `erp_product_category_property`;
CREATE TABLE `erp_product_category_property` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `property_name` varchar(64) NOT NULL DEFAULT '' COMMENT '属性名称(大名称)',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `property_type` int(11) COMMENT '属性类型，1为销售属性，2为商品属性',
  `is_input` int(11) NOT NULL DEFAULT '0' COMMENT '是否是输入属性：0否1是',
  `is_checkbox` int(11) NOT NULL DEFAULT '0' COMMENT '是否为多选，0否1是',
  `is_required` int(11) NOT NULL DEFAULT '0' COMMENT '是否为必选，0否1是',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '属性排序，越大排越前',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='产品分类属性值表';

DROP TABLE if exists `erp_product`;
CREATE TABLE `erp_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_no` varchar(100) NOT NULL COMMENT '商品编码',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称',
  `brand_id` int(20) COMMENT '所属品牌ID',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `subtitle` varchar(60) NOT NULL DEFAULT '' COMMENT '副标题',
  `unit` int(20) COMMENT '单位，对应字典ID',
  `list_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '列表展示价格',
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
  `sku_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品本身的价值',
  `original_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '原价',
  `rent_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '销售价格',
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

DROP TABLE if exists `erp_material`;
CREATE TABLE `erp_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `material_no` varchar(100) NOT NULL COMMENT '物料唯一编号',
  `material_name` varchar(100) COLLATE utf8_bin COMMENT '物料名称，取属性与属性值全称',
  `brand_id` int(20) COMMENT '所属品牌ID',
  `category_id` int(20) NOT NULL COMMENT '所属类目ID',
  `property_id` int(20) NOT NULL COMMENT '属性ID',
  `property_value_id` int(20) NOT NULL COMMENT '属性值ID',
  `material_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '物料本身的价值(单价)',
  `original_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '原价',
  `rent_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '销售价格',
  `material_desc` text COLLATE utf8_bin COMMENT '物料描述',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_material_property` (`property_id`,`property_value_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='物料表';

DROP TABLE if exists `erp_product_material`;
CREATE TABLE `erp_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '物料ID',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '物料总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品物料表';

DROP TABLE if exists `erp_product_equipment`;
CREATE TABLE `erp_product_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `product_id` int(20) NOT NULL COMMENT '所属产品ID',
  `sku_id` int(20) NOT NULL COMMENT '所属SKU ID',
  `warehouse_id` int(20) NOT NULL COMMENT '目前仓库ID',
  `warehouse_position_id` int(20) NOT NULL COMMENT '目前仓位ID',
  `owner_warehouse_id` int(20) NOT NULL COMMENT '归属仓库ID',
  `owner_warehouse_position_id` int(20) NOT NULL COMMENT '归属目前仓位ID',
  `equipment_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '设备本身价值',
  `equipment_status` int(11) NOT NULL DEFAULT '0' COMMENT '设备状态，0闲置，1租赁中，2报废',
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
  `material_id` int(20) NOT NULL COMMENT '物料ID',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '物料总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备物料表';

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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品图片表';

DROP TABLE if exists `erp_product_equipment_repair_record`;
CREATE TABLE `erp_product_equipment_repair_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `order_id` int(20) COMMENT '订单ID',
  `order_product_id` int(20) COMMENT '订单项ID',
  `repair_reason` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '维修原因，由发起人填写',
  `repair_status` int(11) NOT NULL DEFAULT '0' COMMENT '维修状态，0发起维修，1维修中，2维修完成回库',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品维修记录表';

-- ****************************************仓库模块**************************************** --

DROP TABLE if exists `erp_warehouse`;
CREATE TABLE `erp_warehouse` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `warehouse_no` varchar(100) NOT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) COLLATE utf8_bin COMMENT '仓库名称',
  `sub_company_id` int(20) NOT NULL COMMENT '分公司ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='仓库表';

DROP TABLE if exists `erp_warehouse_position`;
CREATE TABLE `erp_warehouse_position` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `warehouse_position_name` varchar(100) COLLATE utf8_bin COMMENT '仓库名称',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='仓库表';

DROP TABLE if exists `erp_stock_order`;
CREATE TABLE `erp_stock_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `stock_order_no` varchar(100) NOT NULL COMMENT '出入库单编号',
  `operation_type` int(11) NOT NULL COMMENT '操作类型，1入库，2出库',
  `cause_type` int(11) NOT NULL COMMENT '起因类型，1采购入库，2退货回库，3维修回库，4用户租赁',
  `refer_no` varchar(100) COMMENT '关联单号',
  `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '出入库单状态，0未出库，1已出库',
  `src_warehouse_id` int(20) COMMENT '源仓库ID',
  `src_warehouse_position_id` int(20) COMMENT '源仓位ID',
  `target_warehouse_id` int(20) NOT NULL COMMENT '目标仓库ID',
  `target_warehouse_position_id` int(20) NOT NULL COMMENT '目标仓位ID',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备出入库单';

DROP TABLE if exists `erp_stock_order_equipment`;
CREATE TABLE `erp_stock_order_equipment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `stock_order_no` varchar(100) NOT NULL COMMENT '出入库单编号',
  `equipment_id` int(20) NOT NULL COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备出入库单明细';

-- ****************************************订单模块**************************************** --

DROP TABLE if exists `erp_order`;
CREATE TABLE `erp_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_no` varchar(100) NOT NULL COMMENT '订单编号',
  `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态，未付款,支付中,已付款,已发货,租赁中,逾期中,退租,取消交易',
  `buyer_user_id` int(20) NOT NULL COMMENT '购买人ID',
  `buyer_real_name` varchar(64) COLLATE utf8_bin COMMENT '真实姓名',
  `rent_type` int(11) NOT NULL DEFAULT '0' COMMENT '租赁方式，1按月租',
  `rent_time_length` int(11) NOT NULL DEFAULT '0' COMMENT '租赁期限',
  `pay_mode` int(11) NOT NULL DEFAULT '0' COMMENT '支付方式：根据支付方式，生成付款计划,1全部付款，2按月付款',
  `product_count_total` int(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `product_amount_total` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品总价',
  `order_amount_total` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '订单总价，实际支付价格',
  `discount_amount_total` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '共计优惠金额',
  `logistics_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '运费',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `confirm_delivery_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `expect_return_time` datetime DEFAULT NULL COMMENT '预计归还时间',
  `actual_return_time` datetime DEFAULT NULL COMMENT '实际归还时间',
  `buyer_remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '购买人备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';

DROP TABLE if exists `erp_order_product`;
CREATE TABLE `erp_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `product_id` int(20) COMMENT '商品ID',
  `product_name` varchar(100) COLLATE utf8_bin COMMENT '商品名称',
  `product_sku_id` int(20) COMMENT '商品SKU ID',
  `product_sku_name` varchar(100) COLLATE utf8_bin COMMENT '商品SKU名称',
  `product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `product_unit_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `product_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品价格',
  `product_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单商品项表';


DROP TABLE if exists `erp_order_consign_info`;
CREATE TABLE `erp_order_consign_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` int(20) NOT NULL COMMENT '订单ID',
  `consignee_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '收货人手机号',
  `province` int(20) DEFAULT NULL COMMENT '省份ID，对应字典ID',
  `city` int(20) DEFAULT NULL COMMENT '城市ID，对应字典ID',
  `district` int(20) DEFAULT NULL COMMENT '区ID，对应字典ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单收货地址表';

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
  `pay_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '支付金额',
  `pay_fee` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '手续费',
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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='第三方支付记录表';

DROP TABLE if exists `erp_order_pay_plan`;
CREATE TABLE `erp_order_pay_plan` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `pay_no` varchar(100) NOT NULL COMMENT '支付编号',
  `parent_pay_no` varchar(100) COMMENT '关联的支付编号，从哪个生成的',
  `order_id` int(20) NOT NULL COMMENT '订单号',
  `pay_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态，0未支付，1支付中，2已经支付，3付款失败，4付款失效',
  `pay_time` datetime COMMENT '发起支付时间',
  `pay_time_plan` datetime NOT NULL COMMENT '原定还款时间',
  `pay_time_real` datetime COMMENT '实际还款时间',
  `expect_amount` decimal(10,2) NOT NULL COMMENT '预计支付金额',
  `pay_amount` decimal(10,2) COMMENT '实际支付金额',
  `overdue_amount` decimal(10,2) COMMENT '逾期金额',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `total_amount` decimal(10,2) COMMENT '总金额',
  `refund_amount` decimal(10,2) COMMENT '实际支付金额',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `invoice_supplier_id` int(20) NOT NULL COMMENT '发票供应商ID',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `warehouse_snapshot` text NOT NULL COMMENT '收货方仓库快照，JSON格式',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `purchase_order_amount_total` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '采购单总价',
  `purchase_order_amount_real` decimal(10,2) COMMENT '采购单实收',
  `purchase_order_amount_statement` decimal(10,2) COMMENT '采购单结算金额',
  `purchase_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购单状态，0待采购，1部分采购，2全部采购',
  `commit_status` int(11) NOT NULL DEFAULT '0' COMMENT '提交状态，0未提交，1已提交',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单表';

DROP TABLE if exists `erp_purchase_order_product`;
CREATE TABLE `erp_purchase_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID冗余',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余',
  `product_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `product_count` int(11) NOT NULL DEFAULT '1' COMMENT '商品总数',
  `product_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品项表';

DROP TABLE if exists `erp_purchase_order_product_material`;
CREATE TABLE `erp_purchase_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_product_id` int(20) NOT NULL COMMENT '采购单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '物料ID',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '物料名称冗余',
  `material_snapshot` text COMMENT '物料信息，防止商品修改留存快照',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '物料总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品物料表';

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
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `product_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购发货单商品项表';

DROP TABLE if exists `erp_purchase_delivery_order_product_material`;
CREATE TABLE `erp_purchase_delivery_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_delivery_order_product_id` int(20) NOT NULL COMMENT '采购发货单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '物料ID，来源（采购单），不可变更',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '物料名称冗余，不可修改',
  `material_snapshot` text COMMENT '物料冗余信息，防止商品修改留存快照，不可修改',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '物料总数，来源（采购单），不可变更',
  `real_material_id` int(20) NOT NULL COMMENT '物料ID',
  `real_material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '物料名称冗余，可修改',
  `real_material_snapshot` text COMMENT '物料冗余信息，防止商品修改留存快照，可修改',
  `real_material_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际物料总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购单商品物料表';

-- -----------------------------------------采购收货单----------------------------------------- --
DROP TABLE if exists `erp_purchase_receive_order`;
CREATE TABLE `erp_purchase_receive_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_delivery_order_id` int(20) NOT NULL COMMENT '采购发货单ID',
  `purchase_receive_no` varchar(100) NOT NULL COMMENT '采购收货单编号',
  `product_supplier_id` int(20) NOT NULL COMMENT '商品供应商ID',
  `invoice_supplier_id` int(20) NOT NULL COMMENT '发票供应商ID',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `warehouse_snapshot` text NOT NULL COMMENT '收货方仓库快照，JSON格式',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `auto_allot_status` int(2) NOT NULL COMMENT '分拨情况，0-未分拨，1-已分拨，2-被分拨，没发票的分公司仓库单，将生成一个总公司收货单，并生成分拨单号，自动分拨到分公司仓库',
  `auto_allot_no` varchar(100) NOT NULL COMMENT '分拨单号，仅在is_auto_allot字段为1时有值',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `purchase_receive_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购单收货状态，0待收货，1已签单',
  `confirm_time` datetime DEFAULT NULL COMMENT '签单时间',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单表';

DROP TABLE if exists `erp_purchase_receive_order_product`;
CREATE TABLE `erp_purchase_receive_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_receive_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_order_product_id` int(20) NOT NULL COMMENT '采购单项ID',
  `purchase_delivery_order_product_id` int(20) NOT NULL COMMENT '采购发货单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID，来源（采购发货单），不可变更',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID 不可变更',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余，不可修改',
  `product_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照，不可修改',
  `product_count` int(11) NOT NULL DEFAULT '1' COMMENT '商品总数，来源（采购发货单），不可变更',
  `real_product_id` int(20) NOT NULL COMMENT '实际商品ID',
  `real_product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称冗余，可修改',
  `real_product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `real_product_snapshot` text COMMENT '商品冗余信息，防止商品修改留存快照，可修改',
  `real_product_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际商品总数',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单商品项表';

DROP TABLE if exists `erp_purchase_receive_order_product_material`;
CREATE TABLE `erp_purchase_receive_order_product_material` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_order_id` int(20) NOT NULL COMMENT '采购单ID',
  `purchase_receive_order_product_id` int(20) NOT NULL COMMENT '采购收货单项ID',
  `product_id` int(20) NOT NULL COMMENT '商品ID',
  `product_sku_id` int(20) NOT NULL COMMENT '商品SKU ID',
  `material_id` int(20) NOT NULL COMMENT '预计物料ID，来源（采购发货单），不可变更',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '物料名称冗余，不可修改',
  `material_snapshot` text COMMENT '物料冗余信息，防止商品修改留存快照，不可修改',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '预计物料总数，来源（采购发货单），不可变更',
  `real_material_id` int(20) NOT NULL COMMENT '实际物料ID',
  `real_material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '物料名称冗余，可修改',
  `real_material_snapshot` text COMMENT '物料冗余信息，防止商品修改留存快照，可修改',
  `real_material_count` int(11) NOT NULL DEFAULT '1' COMMENT '实际物料总数',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购收货单商品物料表';

-- -----------------------------------------采购退货单----------------------------------------- --
DROP TABLE if exists `erp_purchase_back_order`;
CREATE TABLE `erp_purchase_back_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `purchase_back_no` varchar(100) NOT NULL COMMENT '采购退货单编号',
  `supplier_id` int(20) NOT NULL COMMENT '商品供应商ID',
  `warehouse_id` int(20) NOT NULL COMMENT '仓库ID',
  `is_invoice` int(11) NOT NULL COMMENT '是否有发票，0否1是',
  `is_new` int(11) NOT NULL COMMENT '是否全新机',
  `purchase_order_amount_total` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '采购单总价',
  `purchase_back_order_status` int(11) NOT NULL DEFAULT '0' COMMENT '采购退货单状态，0待退货，1已退回',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `owner` int(20) NOT NULL DEFAULT 0 COMMENT '数据归属人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `product_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
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
  `material_id` int(20) NOT NULL COMMENT '预计物料ID，来源（采购收货单），不可变更',
  `material_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '物料名称冗余，不可修改',
  `material_snapshot` text COMMENT '物料冗余信息，防止商品修改留存快照，不可修改',
  `material_count` int(11) NOT NULL DEFAULT '1' COMMENT '预计物料总数，来源（采购收货单），不可变更',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购退货单商品物料表';
