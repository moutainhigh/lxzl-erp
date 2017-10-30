CREATE DATABASE IF NOT EXISTS lxzl_erp DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE lxzl_erp;

DROP TABLE if exists `erp_user`;
CREATE TABLE `erp_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `role_desc` varchar(500) COLLATE utf8_bin COMMENT '角色描述',
  `parent_role_id` bigint(20) COMMENT '上级角色',
  `is_super_admin` int(11) NOT NULL DEFAULT '0' COMMENT '是否是超级管理员，0不是，1是',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

DROP TABLE if exists `erp_user_role`;
CREATE TABLE `erp_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` bigint(20) COMMENT '功能ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色功能表';

DROP TABLE if exists `erp_role_data`;
CREATE TABLE `erp_role_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `department_id` bigint(20) COMMENT '部门ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色功能表';

DROP TABLE if exists `erp_user_data`;
CREATE TABLE `erp_user_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `active_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '观察者用户ID',
  `passive_user_id` bigint(20) COMMENT '被观察者用户ID',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色功能表';

DROP TABLE if exists `erp_sys_menu`;
CREATE TABLE `erp_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '功能编号，唯一',
  `menu_name` varchar(20) NOT NULL DEFAULT '' COMMENT '功能名称',
  `parent_menu_id` bigint(20) NOT NULL DEFAULT 200000 COMMENT '父功能ID',
  `menu_order` int(11) NOT NULL DEFAULT '0' COMMENT '功能排序，越大排越后',
  `is_folder` int(11) NOT NULL DEFAULT '0' COMMENT '0为功能夹，1为功能，2为按钮（按钮只能放在功能下）',
  `menu_url` varchar(50) NOT NULL DEFAULT '' COMMENT '功能URL，如is_folder为0，则为空',
  `menu_icon` varchar(50) NOT NULL DEFAULT '' COMMENT '功能图标',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别，1为根一级，以此类推',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统功能表';

DROP TABLE if exists `erp_img`;
CREATE TABLE `erp_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID，唯一',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID，唯一',
  `data_name` varchar(20) NOT NULL DEFAULT '' COMMENT '数据名称',
  `parent_dictionary_id` bigint(20) NOT NULL DEFAULT 300000 COMMENT '父ID',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越后',
  `data_type` int(11) NOT NULL DEFAULT '0' COMMENT '数据类型',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=300001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据字典表';

-- ****************************************用户模块**************************************** --

-- ****************************************商品模块**************************************** --

DROP TABLE if exists `erp_product_category`;
CREATE TABLE `erp_product_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `category_name` varchar(20) NOT NULL DEFAULT '' COMMENT '分类名称',
  `parent_category_id` bigint(20) NOT NULL DEFAULT 800000 COMMENT '父ID',
  `category_type` int(11) NOT NULL DEFAULT '0' COMMENT '分类类型',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '数据排序排序，越大排越后',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `category_id` bigint(20) NOT NULL COMMENT '所属类目ID',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `property_name` varchar(64) NOT NULL DEFAULT '' COMMENT '属性名称(大名称)',
  `category_id` bigint(20) NOT NULL COMMENT '所属类目ID',
  `property_type` int(11) COMMENT '属性类型，1为销售属性，2为商品属性',
  `is_input` int(11) NOT NULL DEFAULT '0' COMMENT '是否是输入属性：0否1是',
  `is_checkbox` int(11) NOT NULL DEFAULT '0' COMMENT '是否为多选，0否1是',
  `is_required` int(11) NOT NULL DEFAULT '0' COMMENT '是否为必选，0否1是',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '属性排序，越大排越后',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `property_value_name` varchar(64) NOT NULL DEFAULT '' COMMENT '属性值',
  `property_id` bigint(20) NOT NULL COMMENT '所属属性ID',
  `category_id` bigint(20) NOT NULL COMMENT '所属类目ID',
  `data_order` int(11) NOT NULL DEFAULT '0' COMMENT '属性排序，越大排越后',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_no` varchar(100) NOT NULL COMMENT '商品编码',
  `product_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '商品名称',
  `brand_id` bigint(20) COMMENT '所属品牌ID',
  `category_id` bigint(20) NOT NULL COMMENT '所属类目ID',
  `subtitle` varchar(60) NOT NULL DEFAULT '' COMMENT '副标题',
  `unit` int(11) COMMENT '单位，对应字典ID',
  `list_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '列表展示价格',
  `is_sale` int(11) NOT NULL DEFAULT '0' COMMENT '是否可定制：0下架；1上架',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `sku_name` varchar(100) COLLATE utf8_bin COMMENT 'SKU名称',
  `product_id` bigint(20) NOT NULL COMMENT '所属产品ID',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `sku_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品本身的价值',
  `original_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '原价',
  `sale_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '销售价格',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU property ID',
  `product_id` bigint(20) NOT NULL COMMENT '所属产品ID',
  `property_id` bigint(20) NOT NULL COMMENT '属性ID',
  `property_value_id` bigint(20) NOT NULL COMMENT '属性值ID',
  `is_sku` int(11) NOT NULL DEFAULT '0' COMMENT '是否是sku，1是0否',
  `sku_id` bigint(20) COMMENT 'SKU ID',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品SKU基本属性表';

DROP TABLE if exists `erp_product_equipment`;
CREATE TABLE `erp_product_equipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `equipment_no` varchar(100) NOT NULL COMMENT '设备编号唯一',
  `product_id` bigint(20) NOT NULL COMMENT '所属产品ID',
  `sku_id` bigint(20) NOT NULL COMMENT '所属SKU ID',
  `equipment_status` int(11) NOT NULL DEFAULT '0' COMMENT '设备状态，0闲置，1租赁中，2报废',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品设备表';

DROP TABLE if exists `erp_materiel`;
CREATE TABLE `erp_materiel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `materiel_no` varchar(100) NOT NULL COMMENT '物料唯一编号',
  `materiel_name` varchar(100) COLLATE utf8_bin COMMENT '物料名称',
  `materiel_type` int(11) NOT NULL DEFAULT '0' COMMENT '物料类型，1机箱，2内存条，3电源，4CPU，5散热器，6显卡，7主板，8固态硬盘，9机械硬盘等',
  `materiel_price` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '物料本身的价值',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='物料表';

DROP TABLE if exists `erp_product_img`;
CREATE TABLE `erp_product_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID，唯一',
  `img_type` int(11) NOT NULL DEFAULT '0' COMMENT '图片类型',
  `original_name` varchar(200) NOT NULL DEFAULT '' COMMENT '文件原名',
  `product_id` bigint(20) COMMENT '所属产品ID',
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

-- ****************************************商品模块**************************************** --

-- ****************************************订单模块**************************************** --

DROP TABLE if exists `erp_order`;
CREATE TABLE `erp_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_no` varchar(100) NOT NULL COMMENT '订单编号',
  `order_status` int(11) NOT NULL DEFAULT '0' COMMENT '订单状态，未付款,支付中,已付款,已发货,租赁中,逾期中,退租,取消交易',
  `buyer_user_id` bigint(20) NOT NULL COMMENT '购买人ID',
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
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商城订单表';

DROP TABLE if exists `erp_order_product`;
CREATE TABLE `erp_order_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) COMMENT '商品ID',
  `product_name` varchar(100) COLLATE utf8_bin COMMENT '商品名称',
  `product_sku_id` bigint(20) COMMENT '商品SKU ID',
  `product_sku_name` varchar(100) COLLATE utf8_bin COMMENT '商品SKU名称',
  `product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品总数',
  `product_unit_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品单价',
  `product_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '商品价格',
  `product_mode_params` text COMMENT '商品sku属性值，冗余，防止商品修改留存快照',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商城订单商品项表';


DROP TABLE if exists `erp_order_consign_info`;
CREATE TABLE `erp_order_consign_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `consignee_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(24) CHARACTER SET ascii DEFAULT NULL COMMENT '收货人手机号',
  `province` bigint(11) DEFAULT NULL COMMENT '省份ID，对应字典ID',
  `city` bigint(11) DEFAULT NULL COMMENT '城市ID，对应字典ID',
  `district` bigint(11) DEFAULT NULL COMMENT '区ID，对应字典ID',
  `address` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `data_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0不可用；1可用；2删除',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_user` varchar(20) NOT NULL DEFAULT '' COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商城订单收货地址表';

-- ****************************************订单模块**************************************** --

-- ****************************************支付模块**************************************** --

DROP TABLE if exists `erp_third_party_pay_record`;
CREATE TABLE `erp_third_party_pay_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
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
) ENGINE=InnoDB AUTO_INCREMENT=8000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商城第三方支付记录表';

DROP TABLE if exists `erp_order_pay_plan`;
CREATE TABLE `erp_order_pay_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `pay_no` varchar(100) NOT NULL COMMENT '支付编号',
  `parent_pay_no` varchar(100) COMMENT '关联的支付编号，从哪个生成的',
  `order_id` bigint(20) NOT NULL COMMENT '订单号',
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
) ENGINE=InnoDB AUTO_INCREMENT=4000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商城订单付款计划';


DROP TABLE if exists `erp_order_refund_record`;
CREATE TABLE `erp_order_refund_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `pay_no` varchar(100) NOT NULL COMMENT '支付编号',
  `refund_no` varchar(100) NOT NULL COMMENT '退款编号',
  `parent_refund_no` varchar(100) COMMENT '关联的退款编号，从哪个生成的',
  `order_id` bigint(20) NOT NULL COMMENT '订单号',
  `refund_message` varchar(500) COMMENT '退款信息（展示給客戶）',
  `refund_status` int(11) NOT NULL DEFAULT '0' COMMENT '支付状态，0初始化，1退款中，2退款成功，3退款关闭，4退款异常',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态，0待提交，1已提交，2审批通过，3审批驳回，4取消',
  `verify_user` varchar(20) NOT NULL DEFAULT '' COMMENT '审核人',
  `verify_time` datetime DEFAULT NULL COMMENT '审核时间',
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
) ENGINE=InnoDB AUTO_INCREMENT=4000001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商城退款记录';

-- ****************************************支付模块**************************************** --