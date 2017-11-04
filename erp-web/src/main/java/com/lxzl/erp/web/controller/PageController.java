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
        return "/home";
    }

    @RequestMapping("/test")
    public String test() {
        return "/test";
    }

    @RequestMapping("/home")
    public String productList() {
        return "/home";
    }


    @RequestMapping("/user-manage/list")
    public String userManageList() {
        return "/userManage/userList";
    }
    @RequestMapping("/user-manage/addUser")
    public String userManageAddUser() {
        return "/component/user/add";
    }
    @RequestMapping("/user-manage/role")
    public String userManageRole() {
        return "/userManage/roleList";
    }

    @RequestMapping("/user-manage/password")
    public String userManagePassword() {
        return "/component/user/password";
    }

    //商品管理
    @RequestMapping("/product-manage/list")
    public String productManageList() {
        return "/productManage/productList";
    }
    @RequestMapping("/product-manage/detail")
    public String productManageDetail() {
        return "/productManage/productDetail";
    }
    @RequestMapping("/product-manage/add")
    public String productManageAdd() {
        return "/productManage/productAdd";
    }
    @RequestMapping("/product-manage/edit")
    public String productManageEdit() {
        return "/productManage/productEdit";
    }
    @RequestMapping("/product-manage/equipmentList")
    public String productManageEquipmentList() {
        return "/productManage/equipmentList";
    }
    @RequestMapping("/product-manage/skuList")
    public String productManageSkuList() {
        return "/productManage/skuList";
    }

    //订单管理
    @RequestMapping("/order-manage/list")
    public String orderManageList() {
        return "/orderManage/orderList";
    }
    @RequestMapping("/order-manage/detail")
    public String orderManageDetail() {
        return "/orderManage/orderDetail";
    }
    @RequestMapping("/order-manage/add")
    public String orderManageAdd() {
        return "/orderManage/orderAdd";
    }
    @RequestMapping("/order-manage/edit")
    public String orderManageEdit() {
        return "/orderManage/orderEdit";
    }
    @RequestMapping("/order-manage/orderItem")
    public String orderManageOrderItem() {
        return "/orderManage/orderItem";
    }

    //客户管理
    @RequestMapping("/customer-manage/list")
    public String customerManageList() {
        return "/customerManage/customerList";
    }
    @RequestMapping("/customer-manage/detail")
    public String customerManageDetail() {
        return "/customerManage/customerDetail";
    }
    @RequestMapping("/customer-manage/add")
    public String customerManageAdd() {
        return "/customerManage/customerAdd";
    }
    @RequestMapping("/customer-manage/edit")
    public String customerManageEdit() {
        return "/customerManage/customerEdit";
    }
    @RequestMapping("/customer-manage/info")
    public String customerManageInfo() {
        return "/customerManage/customerInfo";
    }

}
