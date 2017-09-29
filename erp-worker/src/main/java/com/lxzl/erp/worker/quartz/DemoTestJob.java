package com.lxzl.erp.worker.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lxzl.erp.core.service.UserService;
import com.lxzl.se.core.quartz.job.BaseJob;


/**
 * 
 * @author lxzl
 *
 */
@Component("demoTestJob")
public class DemoTestJob extends BaseJob {

	@Autowired
	private UserService userService;
	
	@Override
	public Object executeJob(JobExecutionContext ctx) throws JobExecutionException {
		userService.findByUsername("haha");
		System.out.println("DemoTestJob executeJob");
		return "Test demoTestJob";
	}

}
