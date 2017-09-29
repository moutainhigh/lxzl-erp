package com.lxzl.erp.core.service;

import com.lxzl.se.core.service.BaseService;

public interface MessageService extends BaseService {
	
	boolean sendCommentMsg(String message);
}
