package com.lxzl.erp.web.controller;

import com.lxzl.se.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gaochao on 2016/11/2.
 */
@Controller("pageController")
public class PageController extends BaseController {

    @RequestMapping("/home")
    public String index() {
        return "/home";
    }

}
