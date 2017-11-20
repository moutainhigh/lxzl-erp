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

    @RequestMapping("/user-manage/password")
    public String userManagePassword() {
        return "/component/user/password";
    }

    @RequestMapping("/user-manage/list")
    public String userManageList() {
        return "/userManage/userList";
    }
    @RequestMapping("/user-manage/view")
    public String userManageViewUser() {
        return "/component/user/view";
    }
    @RequestMapping("/user-manage/addUser")
    public String userManageAddUser() {
        return "/component/user/add";
    }
    @RequestMapping("/user-manage/editUser")
    public String userManageEditUser() {
        return "/component/user/edit";
    }
    @RequestMapping("/user-manage/userAccessData")
    public String userManageUserAccessData() {
        return "/component/user/userAccessData";
    }

    @RequestMapping("/user-manage/role")
    public String userManageRole() {
        return "/userManage/roleList";
    }
    @RequestMapping("/user-manage/addRole")
    public String userManageAddRole() {
        return "/component/user/addRole";
    }
    @RequestMapping("/user-manage/editRole")
    public String userManageEditRole() {
        return "/component/user/editRole";
    }
    @RequestMapping("/user-manage/roleAccessFunction")
    public String roleAccessFunction() {
        return "/component/user/roleAccessFunction";
    }
    @RequestMapping("/user-manage/roleAccessData")
    public String roleAccessData() {
        return "/component/user/roleAccessData";
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

    //物料管理
    @RequestMapping("/material-manage/list")
    public String materialManageMaterialList() {
        return "/materialManage/materialList";
    }
    @RequestMapping("/material-manage/detail")
    public String materialManageMaterialDetail() {
        return "/materialManage/materialDetail";
    }
    @RequestMapping("/material-manage/add")
    public String materialManageMaterialAdd() {
        return "/materialManage/materialAdd";
    }
    @RequestMapping("/material-manage/edit")
    public String materialManageMaterialEdit() {
        return "/materialManage/materialEdit";
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

    //采购管理
    @RequestMapping("/purchase-manage/list")
    public String purchaseManageList() {
        return "/purchaseManage/purchaseList";
    }
    @RequestMapping("/purchase-manage/add")
    public String purchaseManageAdd() {
        return "/purchaseManage/purchaseAdd";
    }
    @RequestMapping("/purchase-manage/edit")
    public String purchaseManageEdit() {
        return "/purchaseManage/purchaseEdit";
    }
    @RequestMapping("/purchase-manage/detail")
    public String purchaseManageDetail() {
        return "/purchaseManage/purchaseDetail";
    }


    //供应商管理
    @RequestMapping("/supplier-manage/list")
    public String supplierManageList() {
        return "/supplierManage/supplierList";
    }
    @RequestMapping("/supplier-manage/detail")
    public String supplierManageDetail() {
        return "/supplierManage/supplierList";
    }
    @RequestMapping("/supplier-manage/add")
    public String supplierManageAdd() {
        return "/supplierManage/supplierList";
    }
    @RequestMapping("/supplier-manage/edit")
    public String supplierManageEdit() {
        return "/supplierManage/supplierList";
    }

    //仓库商管理
    @RequestMapping("/warehouse-manage/list")
    public String warehouseManageList() {
        return "/warehouseManage/warehouseList";
    }
    @RequestMapping("/warehouse-manage/detail")
    public String warehouseManageDetail() {
        return "/warehouseManage/warehouseList";
    }
    @RequestMapping("/warehouse-manage/add")
    public String warehouseManageAdd() {
        return "/warehouseManage/warehouseList";
    }
    @RequestMapping("/warehouse-manage/edit")
    public String warehouseManageEdit() {
        return "/warehouseManage/warehouseList";
    }

    //收料通知
    @RequestMapping("/purchase-receive-manage/list")
    public String purchaseReceiveManageList() {
        return "/purchaseReceiveManage/purchaseReceiveList";
    }
    @RequestMapping("/purchase-receive-manage/detail")
    public String purchaseReceiveManageDetail() {
        return "/purchaseReceiveManage/purchaseReceiveDetail";
    }
    @RequestMapping("/purchase-receive-manage/edit")
    public String purchaseReceiveManageEdit() {
        return "/purchaseReceiveManage/purchaseReceiveEdit";
    }


    //选择仓库Modal
    @RequestMapping("/warehouse/choose")
    public String warehouseChoose() {
        return "/component/warehouse/chooseModal";
    }
    //选择供应商Modal
    @RequestMapping("/supplier/choose")
    public String supplierChoose() {
        return "/component/supplier/chooseModal";
    }
    //选择商品Modal
    @RequestMapping("/product/choose")
    public String productChoose() {
        return "/component/product/chooseModal";
    }
    //采购单选择审核人Modal
    @RequestMapping("/audit-user/choose")
    public String auditUserChoose() {
        return "/component/purchase/chooseAuditUserModal";
    }

}
