-- erp_workflow_template表新增映射钉钉模板代码字段
alter table erp_workflow_template add dingding_process_code varchar(127) COMMENT "钉钉模板代码"; # 
-- erp_department表新增映射钉钉的部门编号字段
alter table erp_department add dingding_dept_id varchar(31) COMMENT "钉钉部门编号"; # 
-- erp_workflow_link表新增钉钉的工作流编号字段
alter table erp_workflow_link add dingding_workflow_id varchar(63) COMMENT "钉钉工作流id"; # 


