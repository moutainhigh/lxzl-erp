package com.lxzl.erp.core.service;

import org.springframework.stereotype.Service;

import com.lxzl.se.core.service.StartupCallback;

@Service("startupCallback")
public class InitStartService implements StartupCallback {

	@Override
	public void businessHandle() {
		System.out.println("erp startupCallback");
	}
}
