package com.lxzl.erp.web.controller;

import com.lxzl.se.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gaochao on 2016/11/2.
 */
@Controller("pageController")
public class PageController extends BaseController {

    @RequestMapping("/index")
    public String index() {
        return "/index";
    }

    @RequestMapping("/test")
    public String test() {
        return "/test";
    }

    @RequestMapping("/login")
    public String login() {
        return "/index";
    }
}
