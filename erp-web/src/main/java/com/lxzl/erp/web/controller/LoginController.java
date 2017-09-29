package com.lxzl.erp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lxzl.erp.dataaccess.domain.UserDO;
import com.lxzl.erp.web.config.LoginConfig;
import com.lxzl.se.common.util.CommonUtil;
import com.lxzl.se.web.controller.BaseController;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private LoginConfig loginConfig;

	@RequestMapping(value = "/")
	public String root(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "redirect:/index";
	}

	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = "zhangsan";
		UserDO user = new UserDO();
		user.setId(12);
		user.setUsername(username);
		user.setNick("张三");
		user.setPassword("abc123456");
		String environment = CommonUtil.getEnvironment();

		if (environment == null) {
			environment = "";
		}
		
		environment = environment.trim();
		model.addAttribute("environment", environment);
		model.addAttribute("user", user);
		return "/index";
	}

}
