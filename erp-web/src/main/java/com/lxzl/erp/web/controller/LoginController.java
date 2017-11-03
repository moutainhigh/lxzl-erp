package com.lxzl.erp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lxzl.erp.web.config.LoginConfig;
import com.lxzl.se.common.util.CommonUtil;
import com.lxzl.se.web.controller.BaseController;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private LoginConfig loginConfig;

	@RequestMapping(value = "/")
	public String root(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "redirect:/login";
	}

	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/login";
	}

}
