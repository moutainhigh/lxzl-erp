package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Menu;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.system.MenuService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RequestMapping("/menu")
@Controller
@ControllerLog
public class MenuController extends BaseController {


    @RequestMapping(value = "getMenu", method = RequestMethod.POST)
    public Result getMenu(HttpServletRequest request, HttpServletResponse response, Model model) {
        ServiceResult<String, List<Menu>> serviceResult = menuService.findAllMenu();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getHomeMenu", method = RequestMethod.POST)
    public Result getHomeMenu(HttpServletRequest request, HttpServletResponse response, Model model) {
        ServiceResult<String, List<Menu>> serviceResult = menuService.getHomeMenu();

        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getMenuByCode")
    public Result getMenuByCode(@RequestParam("menuId") Integer menuId) {
        ServiceResult<String, Menu> serviceResult = menuService.findByCode(menuId);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getMenuTest")
    public Result getMenuTest(HttpServletRequest request, HttpServletResponse response, Model model) {
        ServiceResult<String, List<Menu>> serviceResult = menuService.findAllMenu();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private MenuService menuService;

    @Autowired
    private ResultGenerator resultGenerator;
}
