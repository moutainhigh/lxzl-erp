-- erp_k3_return_order表新处理成功的状态字段
alter table erp_k3_return_order add success_status int(11) NOT NULL DEFAULT 1 COMMENT "处理成功的状态0 未成功处理 1 处理成功"; # 
