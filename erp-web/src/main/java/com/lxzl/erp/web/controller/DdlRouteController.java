package com.lxzl.erp.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.domain.ResultCode;
import com.lxzl.se.core.domain.DdlRoute;
import com.lxzl.se.dataaccess.mysql.ddl.DdlConfig;
import com.lxzl.se.dataaccess.mysql.ddl.service.DdlRouteService;
import com.lxzl.se.web.controller.BaseController;

@RequestMapping("/ddl")
@Controller
public class DdlRouteController extends BaseController {

	@Autowired
	private DdlRouteService ddlRouteService;

	@RequestMapping("/listDdlConfig")
	public Result listDdlConfig(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<DdlConfig> ddlConfigList = ddlRouteService.getDdlConfigList();
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("ddlConfigList", ddlConfigList);
		return result;
	}

	@RequestMapping("/getDdlRoute")
	public Result getDdlRoute(HttpServletRequest request, HttpServletResponse response, Model model) {
		String table = getStringParameter("table");
		String columnValue = getStringParameter("columnValue");
		DdlRoute ddlRoute = ddlRouteService.getDdlRoute(table, columnValue);
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("ddlRoute", ddlRoute);
		return result;
	}
}
