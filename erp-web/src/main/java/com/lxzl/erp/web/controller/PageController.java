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

    @RequestMapping("/productList")
    public String productList() {
        return "/product/productList";
    }

    @RequestMapping("/supplierEdit")
    public String supplierEdit() {
        return "/supplier/supplierEdit";
    }

    @RequestMapping("/supplierList")
    public String supplierList() {
        return "/supplier/supplierList";
    }

    @RequestMapping("/supplierProductEdit")
    public String supplierProductEdit() {
        return "/supplier/supplierProductEdit";
    }

    @RequestMapping("/supplierProductList")
    public String supplierProductList() {
        return "/supplier/supplierProductList";
    }

    @RequestMapping("/userEdit")
    public String userEdit() {
        return "/user/userEdit";
    }

    @RequestMapping("/userList")
    public String userList() {
        return "/user/userList";
    }

    @RequestMapping("/productEdit")
    public String productEdit() {
        return "/product/productEdit";
    }

    @RequestMapping("/productDetail")
    public String productDetail() {
        return "/product/productDetail";
    }

    @RequestMapping("/productCategoryList")
    public String productCategoryList() {
        return "/product/productCategoryList";
    }

    @RequestMapping("/productCategoryEdit")
    public String productCategoryEdit() {
        return "/product/productCategoryEdit";
    }

    @RequestMapping("/supplierDetail")
    public String supplierDetail() {
        return "/supplier/supplierDetail";
    }

    @RequestMapping("/supplierColorList")
    public String supplierColorList() {
        return "/supplier/supplierColorList";
    }

    @RequestMapping("/supplierColorEdit")
    public String supplierColorEdit() {
        return "/supplier/supplierColorEdit";
    }

    @RequestMapping("/dictionaryEdit")
    public String dictionaryEdit() {
        return "/dictionary/dictionaryEdit";
    }

    @RequestMapping("/dictionaryList")
    public String dictionaryList() {
        return "/dictionary/dictionaryList";
    }

    @RequestMapping("/supplierProductDetail")
    public String materialDetail() {
        return "/product/materialDetail";
    }

    @RequestMapping("/supplierProductCategoryList")
    public String materialCategoryList() {
        return "/product/materialCategoryList";
    }

    @RequestMapping("/supplierProductCategoryEdit")
    public String materialCategoryEdit() {
        return "/product/materialCategoryEdit";
    }

    @RequestMapping("/communityList")
    public String communityList() {
        return "/user/communityList";
    }

    @RequestMapping("/communityEdit")
    public String communityEdit() {
        return "/user/communityEdit";
    }

    @RequestMapping("/communityDetail")
    public String communityDetail() {
        return "/user/communityDetail";
    }

    @RequestMapping("/ownerList")
    public String ownerList() {
        return "/user/ownerList";
    }

    @RequestMapping("/ownerEdit")
    public String ownerEdit() {
        return "/user/ownerEdit";
    }

    @RequestMapping("/ownerDetail")
    public String ownerDetail() {
        return "/user/ownerDetail";
    }

    @RequestMapping("/orderList")
    public String orderList() {
        return "/order/orderList";
    }

    @RequestMapping("/orderAdd")
    public String orderAdd() {
        return "/order/orderAdd";
    }

    @RequestMapping("/orderEdit")
    public String orderEdit() {
        return "/order/orderEdit";
    }

    @RequestMapping("/orderDetail")
    public String orderDetail() {
        return "/order/orderDetail";
    }

    @RequestMapping("/contractList")
    public String contractList() {
        return "/contract/contractList";
    }

    @RequestMapping("/contractEdit")
    public String contractEdit() {
        return "/contract/contractEdit";
    }

    @RequestMapping("/contractDetail")
    public String contractDetail() {
        return "/contract/contractDetail";
    }

    @RequestMapping("/purchaseEdit")
    public String purchaseEdit() {
        return "/purchase/purchaseEdit";
    }

    @RequestMapping("/purchaseDetail")
    public String purchaseDetail() {
        return "/purchase/purchaseDetail";
    }

    @RequestMapping("/purchaseList")
    public String purchaseList() {
        return "/purchase/purchaseList";
    }

    @RequestMapping("/purchaseEnquiry")
    public String purchaseEnquiry() {
        return "/purchase/purchaseEnquiry";
    }

    @RequestMapping("/purchaseProducts")
    public String purchaseProducts() {
        return "/purchase/purchaseProducts";
    }

    @RequestMapping("/financialList")
    public String financialList() {
        return "/financial/financialList";
    }

    @RequestMapping("/financialNopay")
    public String financialNopay() {
        return "/financial/financialNopay";
    }

    @RequestMapping("/installEdit")
    public String installEdit() {
        return "/install/installEdit";
    }

    @RequestMapping("/installDetail")
    public String installDetail() {
        return "/install/installDetail";
    }

    @RequestMapping("/installList")
    public String installList() {
        return "/install/installList";
    }

    @RequestMapping("/installProductList")
    public String installProductList() {
        return "/install/installProductList";
    }

    @RequestMapping("/orderPrint")
    public String orderPrint() {
        return "/order/orderPrint";
    }

    @RequestMapping("/groupedProductList")
    public String groupedProductList() {
        return "/product/groupedProductList";
    }

    @RequestMapping("/groupedProductEdit")
    public String groupedProductEdit() {
        return "/product/groupedProductEdit";
    }

    @RequestMapping("/groupedProductDetail")
    public String groupedProductDetail() {
        return "/product/groupedProductDetail";
    }

    @RequestMapping("/userFavoriteList")
    public String userFavoriteList() {
        return "/product/userFavoriteList";
    }

    @RequestMapping("/roleList")
    public String roleList() {
        return "/user/roleList";
    }

    @RequestMapping("/roleAdd")
    public String roleAdd() {
        return "/user/roleAdd";
    }

    @RequestMapping("/roleEdit")
    public String roleEdit() {
        return "/user/roleEdit";
    }

    @RequestMapping("/statisticsList")
    public String statisticsList() {
        return "/statistics/statisticsList";
    }

    @RequestMapping("/houseStatistics")
    public String houseStatistics() {
        return "/report/houseStatistics";
    }

    @RequestMapping("/settlementCollectPage")
    public String settlementCollectPage() {
        return "/settlement/collectPage";
    }

    @RequestMapping("/settlementOrderPage")
    public String settlementOrderPage() {
        return "/settlement/orderPage";
    }

    @RequestMapping("/settlementOrderDetail")
    public String settlementOrderDetail() {
        return "/settlement/orderDetail";
    }

    @RequestMapping("/settlementEdit")
    public String settlementEdit() {
        return "/settlement/settlementEdit";
    }

    @RequestMapping("/settlementPrint")
    public String settlementPrint() {
        return "/settlement/settlementPrint";
    }

    @RequestMapping("/purchaseOrderPrint")
    public String purchaseOrderPrint() {
        return "/purchase/purchaseOrderPrint";
    }

    @RequestMapping("/installOrderPrint")
    public String installOrderPrint() {
        return "/install/installOrderPrint";
    }

}
