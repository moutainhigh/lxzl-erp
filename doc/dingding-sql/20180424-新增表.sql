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

