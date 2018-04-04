package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gaochao on 2016/11/2.
 */
@Controller("pageController")
public class PageController extends BaseController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ResultGenerator resultGenerator;

//    @RequestMapping(value = "/uppwd")
//    public String uppwd(HttpServletRequest request, HttpServletResponse response, Model model) {
//        return "/uppwd";
//    }

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

    @RequestMapping("/user-manage/department")
    public String userManageDepartment() {
        return "/departmentManage/departmentList";
    }

    @RequestMapping("/user-manage/departmentAdd")
    public String userManageDepartmentAdd() {
        return "/departmentManage/departmentAdd";
    }

    @RequestMapping("/user-manage/departmentEdit")
    public String userManageDepartmentEdit() {
        return "/departmentManage/departmentEdit";
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

    //条形码打印
    @RequestMapping("/print-manage/barcode")
    public String printManageBarcode() {
        return "/productManage/printBarcode";
    }

    //物料型号管理
    @RequestMapping("/material-modal-manage/list")
    public String materialMaodalManageList() {
        return "/materialModalManage/materialModal";
    }

    @RequestMapping("/material-modal-manage/add")
    public String materialMaodalManageAdd() {
        return "/materialModalManage/materialModalAdd";
    }

    @RequestMapping("/material-modal-manage/edit")
    public String materialMaodalManageEdit() {
        return "/materialModalManage/materialModalEdit";
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

    //散料列表
    @RequestMapping("/material-manage/bulk-list")
    public String bulkMaterialManageMaterialList() {
        return "/materialManage/bulkMaterialList";
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

    @RequestMapping("/order-manage-input-picking-material/modal")
    public String orderManageInputPickingMaterialModal() {
        return "/component/order/inputPickingMaterial";
    }

    @RequestMapping("/order-manage/print")
    public String orderManagePrint() {
        return "/orderManage/orderPrint";
    }

    @RequestMapping("/order-manage/waite-for-delivery-list")
    public String orderManageWaiteForDeliveryList() {
        return "/orderManage/waiteForDeliveryOrderList";
    }

    //退货单
    @RequestMapping("/order-return-manage/list")
    public String returnOrderManageList() {
        return "/returnOrderManage/returnOrderList";
    }
    @RequestMapping("/order-return-manage/detail")
    public String returnOrderManageDtail() {
        return "/returnOrderManage/returnOrderDetail";
    }
    @RequestMapping("/order-return-manage/add")
    public String returnOrderManageAdd() {
        return "/returnOrderManage/returnOrderAdd";
    }
    @RequestMapping("/order-return-manage/edit")
    public String returnOrderManageEdit() {
        return "/returnOrderManage/returnOrderEdit";
    }
    //输入退还当服务费等信息
    @RequestMapping("/return-order-end-info-modal/input")
    public String inputReturnOrderEndInfoModal() {
        return "/component/returnOrder/inputEndInfoModal";
    }
    @RequestMapping("/input-return-material-info-modal/input")
    public String inputReturnMaterialModal() {
        return "/component/returnOrder/inputReturnMaterialModal";
    }

    //配货
    @RequestMapping("/order-manage/picking")
    public String orderManageDelivery() {
        return "/component/order/picking";
    }

    //个人客户管理
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

    @RequestMapping("/customer-manage/riskModal")
    public String customerManageInfo() {
        return "/component/customer/riskModal";
    }

    //企业客户
    @RequestMapping("/customer-business-manage/list")
    public String businessCustomerManageList() {
        return "/businessCustomerManage/businessCustomerList";
    }

    @RequestMapping("/customer-business-manage/detail")
    public String businessCustomerManageDetail() {
        return "/businessCustomerManage/businessCustomerDetail";
    }

    @RequestMapping("/customer-business-manage/add")
    public String businessCustomerManageAdd() {
        return "/businessCustomerManage/businessCustomerAdd";
    }

    @RequestMapping("/customer-business-manage/edit")
    public String businessCustomerManageEdit() {
        return "/businessCustomerManage/businessCustomerEdit";
    }

    @RequestMapping("/customer-consign-info/add")
    public String customerConsignInfoAdd() { return "/component/customerConsignInfo/add";}

    @RequestMapping("/customer-consign-info/edit")
    public String customerConsignInfoEdit() {
        return "/component/customerConsignInfo/edit";
    }

    @RequestMapping("/customer-manual-account/modal")
    public String customerManualAccountModal() {
        return "/component/customer/manualAccountAmountModal";
    }

    @RequestMapping("/customer/set-settlement-date-modal")
    public String customerSetSettlementDateModal() {
        return "/component/customer/setSettlementDateModal";
    }
    @RequestMapping("/customer/set-short-rental-upper-limit-modal")
    public String customerSetShortRentalUpperLimit() {
        return "/component/customer/setShortRentalUpperLimit";
    }

    @RequestMapping("/customer/set-risk-credit-amount-used")
    public String customerSetRiskCreditAmountUsed() {
        return "/component/customer/updateRiskCreditAmountUsed";
    }

    @RequestMapping("/customer-manage/edit-after-pass")
    public String customerManageEditAfterPass() {
        return "/customerManage/customerEditAfterPass";
    }

    @RequestMapping("/customer-business-manage/edit-after-pass")
    public String customerBusinessManageEditAfterPass() {
        return "/businessCustomerManage/businessCustomerEditAfterPass";
    }

    @RequestMapping(value = "customer-common-manage/detail", method = RequestMethod.GET)
    public String detailCustomer(String no) {
        CustomerQueryParam customerQueryParam = new CustomerQueryParam();
        customerQueryParam.setCustomerNo(no);
        ServiceResult<String, Customer> serviceResult = customerService.detailCustomer(customerQueryParam.getCustomerNo());
        if(serviceResult.getErrorCode().equals(ErrorCode.SUCCESS)) {
            if(serviceResult.getResult().getCustomerType().equals(CustomerType.CUSTOMER_TYPE_COMPANY)) {
                return "redirect:/customer-business-manage/detail?no="+no;
            } else {
                return "redirect:/customer-manage/detail?no="+no;
            }
        }
        return "redirect:/customer-business-manage/detail?no="+no;
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

    //采购收料通知
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

    @RequestMapping("/purchase-receive-manage-product-equipment/edit")
    public String purchaseReceiveManageEditEquipment() {
        return "/component/purchaseReceiveOrder/purchaseReceiveOrderProductEquipment";
    }

    @RequestMapping("/purchase-receive-manage-material-bulk/edit")
    public String purchaseReceiveManageEditMaterialBulk() {
        return "/component/purchaseReceiveOrder/purchaseReceiveOrderProductMaterialBulk";
    }

    @RequestMapping("/purchase-receive-manage-udpate-equipment-remark/modal")
    public String purchaseReceiveManageUpdateEquipmentRemarkModal() {
        return "/component/purchaseReceiveOrder/updateEquipmentRemarkModal";
    }

    @RequestMapping("/purchase-receive-manage-equiment/list")
    public String purchaseReceiveMangeEquimentList() {
        return "/component/purchaseReceiveOrder/equimentList";
    }


    //采购发货单
    @RequestMapping("/purchase-delivery-manage/list")
    public String purchaseDeliveryManageList() {
        return "/purchaseDeliveryManage/purchaseDeliveryList";
    }

    @RequestMapping("/purchase-delivery-manage/detail")
    public String purchaseDeliveryManageDetail() {
        return "/purchaseDeliveryManage/purchaseDeliveryDetail";
    }


    //供应商管理
    @RequestMapping("/supplier-manage/list")
    public String supplierManageList() {
        return "/supplierManage/supplierList";
    }

    @RequestMapping("/supplier-manage/detail")
    public String supplierManageDetail() {
        return "/supplierManage/supplierDetail";
    }

    @RequestMapping("/supplier-manage/add")
    public String supplierManageAdd() {
        return "/supplierManage/supplierAdd";
    }

    @RequestMapping("/supplier-manage/edit")
    public String supplierManageEdit() {
        return "/supplierManage/supplierEdit";
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

    //审核管理
    @RequestMapping("/audit-manage/list")
    public String auditManageList() {
        return "/auditManage/auditList";
    }

    @RequestMapping("/audit-manage/detail")
    public String auditManageDetail() {
        return "/auditManage/auditDetail";
    }

    @RequestMapping("/audit-manage/modal/pass")
    public String auditManagePassModal() {
        return "/component/audit/passModal";
    }

    @RequestMapping("/audit-manage/modal/reject")
    public String auditManageRejectModal() {
        return "/component/audit/rejectModal";
    }

    //调拨单
    @RequestMapping("/order-deployment-manage/list")
    public String deploymentOrderManageList() {
        return "/deploymentOrderManage/deploymentOrderList";
    }
    @RequestMapping("/order-deployment-manage/add")
    public String deploymentOrderManageAdd() {
        return "/deploymentOrderManage/deploymentOrderAdd";
    }
    @RequestMapping("/order-deployment-manage/edit")
    public String deploymentOrderManageEdit() {
        return "/deploymentOrderManage/deploymentOrderEdit";
    }
    @RequestMapping("/order-deployment-manage/detail")
    public String deploymentOrderManageDetail() {
        return "/deploymentOrderManage/deploymentOrderDetail";
    }
    @RequestMapping("/order-deployment-manage/stock-up-material-modal")
    public String deploymentOrderManageStockUpMaterialModal() {
        return "/component/deploymentOrder/stockUpMaterialModal";
    }

    //流转单-转入
    @RequestMapping("/order-transfer-in-manage/list")
    public String transferOrdermanageList() {
        return "/transferOrderManage/in/inList";
    }
    @RequestMapping("/order-transfer-in-manage/detail")
    public String transferOrdermanageDetail() {
        return "/transferOrderManage/in/inDetail";
    }
    @RequestMapping("/order-transfer-in-manage/add")
    public String transferOrdermanageAdd() {
        return "/transferOrderManage/in/inAdd";
    }
    @RequestMapping("/order-transfer-in-manage/edit")
    public String transferOrdermanageEdit() {
        return "/transferOrderManage/in/inEdit";
    }
    @RequestMapping("/order-transfer-manage/equiment-list")
    public String transferOrderInManageEquimentList() {
        return "/component/transferOrder/equimentList";
    }
    @RequestMapping("/order-transfer-manage/bulk-material-list")
    public String transferOrderInManageMaterialList() {
        return "/component/transferOrder/bulkMaterialList";
    }


    //流转单-转出
    @RequestMapping("/order-transfer-out-manage/list")
    public String transferOutOrdermanageList() {
        return "/transferOrderManage/out/outList";
    }
    @RequestMapping("/order-transfer-out-manage/detail")
    public String transferOutOrdermanageDetail() {
        return "/transferOrderManage/out/outDetail";
    }
    @RequestMapping("/order-transfer-out-manage/add")
    public String transferOutOrdermanageAdd() {
        return "/transferOrderManage/out/outAdd";
    }
    @RequestMapping("/order-transfer-out-manage/edit")
    public String transferOutOrdermanageEdit() {
        return "/transferOrderManage/out/outEdit";
    }
    @RequestMapping("/order-transfer-out-manage/stock-up-material-modal")
    public String transferOutOrdermanageStockUpMaterialModal() {
        return "/component/transferOrder/stockUpMaterialModal";
    }

    //同行管理
    @RequestMapping("/peer-manage/supplier-list")
    public String peerManageSupplierList() {
        return "/peerManage/peerSupplier/peerSupplierList";
    }
    @RequestMapping("/peer-manage/supplier-detail")
    public String peerManageSupplierDetail() {
        return "/peerManage/peerSupplier/peerSupplierDetail";
    }
    @RequestMapping("/peer-manage/supplier-add")
    public String peerManageSupplierAdd() {
        return "/peerManage/peerSupplier/peerSupplierAdd";
    }
    @RequestMapping("/peer-manage/supplier-edit")
    public String peerManageSupplierEdit() {
        return "/peerManage/peerSupplier/peerSupplierEdit";
    }
    @RequestMapping("/peer-manage/order-list")
    public String peerManageOrderList() {
        return "/peerManage/peerOrder/peerOrderList";
    }
    @RequestMapping("/peer-manage/order-detail")
    public String peerManageOrderDetail() {
        return "/peerManage/peerOrder/peerOrderDetail";
    }
    @RequestMapping("/peer-manage/order-add")
    public String peerManageOrderAdd() {
        return "/peerManage/peerOrder/peerOrderAdd";
    }
    @RequestMapping("/peer-manage/order-edit")
    public String peerManageOrderEdit() {
        return "/peerManage/peerOrder/peerOrderEdit";
    }
    @RequestMapping("/peer-supplier-modal/choose")
    public String choosePeerSuppliserModal() {
        return "/component/peer/choosePeerSupplierModal";
    }
    @RequestMapping("/peer-order-manage/equiment-list")
    public String peerOrderManageEquimentList() {
        return "/component/peer/equimentList";
    }
    @RequestMapping("/peer-order-manage/bulk-material-list")
    public String peerOrderManageBulkMaterialList() {
        return "/component/peer/bulkMaterialList";
    }

    //结算单
    @RequestMapping("/statement-order/list")
    public String statementOrderList() {
        return "/statementOrderManage/statementOrderList";
    }
    @RequestMapping("/statement-order/detail")
    public String statementOrderDetail() {
        return "/statementOrderManage/statementOrderDetail";
    }
    // 对账单
    @RequestMapping("/statement-monthly-order/list")
    public String monthStatementOrderList() {
        return "/statementOrderManage/monthlyStatementOrderList";
    }
    @RequestMapping("/statement-monthly-order/detail")
    public String monthStatementOrderDetail() {
        return "/statementOrderManage/monthlyStatementOrderDetail";
    }
    // 冲正单
    @RequestMapping("/correct-order/list")
    public String correctOrderList() {
        return "/statementOrderManage/correctOrderList";
    }
    @RequestMapping("/correct-order/detail")
    public String correctOrderDetail() {
        return "/statementOrderManage/correctOrderDetail";
    }
    @RequestMapping("/correct-order/add")
    public String addCorrectOrder() {
        return "/component/statementOrder/addCorrectOrder";
    }

    //统计
    @RequestMapping("/statistics/income-list")
    public String statisticsIncomeList() {
        return "/statisticsManage/incomeList";
    }
    @RequestMapping("/detail/unreceivable-list")
    public String detailManageUnReceivableList() {
        return "/statisticsManage/detailUnreceivableList";
    }
    @RequestMapping("/statistics/unreceivable-list")
    public String statisticsManageUnReceivableList() {
        return "/statisticsManage/statisticsUnreceivableList";
    }


    //充值记录
    @RequestMapping("/recharge-manage/list")
    public String rechargeManageList() {
        return "/rechargeManage/rechargeList";
    }


    //站内信息
    @RequestMapping("/site-message/list")
    public String siteMessageList() {
        return "/siteMessageManage/siteMessageList";
    }

    //站内信息收件箱
    @RequestMapping("/site-message/inbox-list")
    public String inboxList() {
        return "/siteMessageManage/inboxList";
    }
    //站内信息发件箱
    @RequestMapping("/site-message/outbox-list")
    public String outboxList() {
        return "/siteMessageManage/outboxList";
    }


    //换货单
    @RequestMapping("/change-order/list")
    public String changeOrderManageList() {
        return "/changeOrderManage/changeOrderList";
    }
    @RequestMapping("/change-order/detail")
    public String changeOrderManageDetail() {
        return "/changeOrderManage/changeOrderDetail";
    }
    @RequestMapping("/change-order/add")
    public String changeOrderManageAdd() {
        return "/changeOrderManage/changeOrderAdd";
    }
    @RequestMapping("/change-order/edit")
    public String changeOrderManageEdit() {
        return "/changeOrderManage/changeOrderEdit";
    }
    @RequestMapping("/change-order/equiment-list")
    public String changeOrderManageEquimentList() {
        return "/component/changeOrder/equimentList";
    }
    @RequestMapping("/change-order/bulk-material-list")
    public String changeOrderMaterialList() {
        return "/component/changeOrder/bulkMaterialList";
    }
    @RequestMapping("/change-order/material-in-storage")
    public String changeOrderMaterialInStorage() {
        return "/component/changeOrder/materialInStorage";
    }
    @RequestMapping("/change-order/complete-input")
    public String changeOrderCompleteInput() {
        return "/component/changeOrder/completeInput";
    }
    @RequestMapping("/change-order/equipment-remark-modal")
    public String changeOrderEquipmentRemarkModal() {
        return "/component/changeOrder/equipmentRemark";
    }
    @RequestMapping("/change-order/update-equipment-diff-price")
    public String changeOrderUpdateEquipmentDiffPrice() {
        return "/component/changeOrder/updateEquipmentPriceDiff";
    }
    @RequestMapping("/change-order/update-material-diff-price")
    public String changeOrderUpdateMaterialDiffPrice() {
        return "/component/changeOrder/updateMaterialPriceDiff";
    }

    //k3数据
    @RequestMapping("/k3-order/list")
    public String k3OrderManageList() {
        return "/k3Manage/k3OrderList";
    }
    @RequestMapping("/k3-order/detail")
    public String k3OrderManageDetail() {
        return "/k3Manage/k3OrderDetail";
    }
    @RequestMapping("/return-order-k3/list")
    public String k3ReturnOrderList() {
        return "/k3Manage/K3ReturnOrder";
    }
    @RequestMapping("/return-order-k3/detail")
    public String k3ReturnOrderDetail() {
        return "/k3Manage/K3ReturnOrderDetail";
    }
    @RequestMapping("/return-order-k3/add")
    public String k3ReturnOrderAdd() {
        return "/k3Manage/k3ReturnOrderAdd";
    }
    @RequestMapping("/return-order-k3/edit")
    public String k3ReturnOrderEdit() {
        return "/k3Manage/k3ReturnOrderEdit";
    }
    @RequestMapping("/k3-order/item-choose")
    public String k3OrderChooseItem() {
        return "/component/k3Manage/chooseOrderItemModal";
    }
    @RequestMapping("/change-order-k3/list")
    public String k3ChangeOrderList() {
        return "/k3Manage/k3ChangeOrder";
    }
    @RequestMapping("/change-order-k3/detail")
    public String k3ChangeOrderDetail() {
        return "/k3Manage/k3ChangeOrderDetail";
    }
    @RequestMapping("/change-order-k3/add")
    public String k3ChangeOrderAdd() {
        return "/k3Manage/k3ChangeOrderAdd";
    }
    @RequestMapping("/change-order-k3/edit")
    public String k3ChangeOrderEdit() {
        return "/k3Manage/k3ChangeOrderEdit";
    }
    @RequestMapping("/k3-data/list")
    public String k3DataManageList() {
        return "/k3Manage/sendTok3";
    }
    @RequestMapping("/send-data-to-k3/modal")
    public String sendDataToK3Modal() {
        return "/k3Manage/bathSendTok3Modal";
    }

    //分公司列表
    @RequestMapping("/company-manage/list")
    public String companyManageList() {
        return "/companyManage/companyList";
    }
    @RequestMapping("/add-short-receivable-amount/modal")
    public String addShortReceivableAmountModal() {
        return "/component/company/addShortReceivableAmountModal";
    }

    //组合商品
    @RequestMapping("/grouped-product/list")
    public String groupedProductList() {
        return "/groupedProductManage/groupedProductList";
    }
    @RequestMapping("/grouped-product/detail")
    public String groupedProductDetail() {
        return "/groupedProductManage/groupedProductDetail";
    }
    @RequestMapping("/grouped-product/add")
    public String groupedProductAdd() {
        return "/groupedProductManage/groupedProductAdd";
    }
    @RequestMapping("/grouped-product/edit")
    public String groupedProductEdit() {
        return "/groupedProductManage/groupedProductEdit";
    }


    //回访记录
    @RequestMapping("/customer-return-visit-record/list")
    public String customerReturnVisitRecordList() {
        return "/customerReturnVisitRecordManage/returnVisitRecordList";
    }
    @RequestMapping("/customer-return-visit-record/detail")
    public String customerReturnVisitRecordDetail() {
        return "/customerReturnVisitRecordManage/returnVisitRecordDetail";
    }
    @RequestMapping("/customer-return-visit-record/add")
    public String customerReturnVisitRecordAdd() {
        return "/customerReturnVisitRecordManage/returnVisitRecordAdd";
    }
    @RequestMapping("/customer-return-visit-record/edit")
    public String customerReturnVisitRecordEdit() {
        return "/customerReturnVisitRecordManage/returnVisitRecordEdit";
    }


    //优惠券
    @RequestMapping("/coupon-manage/group-list")
    public String couponGroupManage() {
        return "/couponManage/couponGroupList";
    }
    @RequestMapping("/coupon-manage/group-detail")
    public String couponGroupDetailManage() {
        return "/couponManage/couponGroupDetail";
    }
    @RequestMapping("/coupon-manage/card-list")
    public String couponManage() {
        return "/couponManage/couponCardList";
    }
    @RequestMapping("/coupon-manage/add-coupon")
    public String couponManageAdd(){return "/component/couponManage/couponAddList";}
    @RequestMapping("/coupon-manage/add-coupon-pici")
    public String couponManagePiCiAdd(){return "/component/couponManage/couponPiCiAddList";}


    /**
     * 资金流水附件信息列表
     * @return
     */
    @RequestMapping("/jurnal-amount-attachment/list")
    public String jurnalAmountAttachmentList() {
        return "/financialManage/jurnalAmountAttachmentList";
    }
    /**
     * 资金流水列表
     * @return
     */
    @RequestMapping("/jurnal-amount/list")
    public String jurnalAmountList() {
        return "/financialManage/jurnalAmountList";
    }
    @RequestMapping("/jurnal-amount/detail")
    public String jurnalAmountDetail() {
        return "/financialManage/jurnalAmountDetail";
    }

    @RequestMapping("/private-manage/request")
    public String privateManageRequeset() {
        return "/privateManage/request";
    }

    //上传附件
    @RequestMapping("/jurnal-attachment-list/file/upload")
    public String jurnalAttachmentList() {return "/component/jurnalAmount/upload";}

    //选择所有客户(公司、个人)
    @RequestMapping("/all-customer-modal/choose")
    public String jurnalAmountCustomerList() {
        return "/component/customer/chooseCustomerByAll";
    }

    //交易明细
    @RequestMapping("/jurnal-amount/tradeDetail")
    public String jurnalAmountTradeDetail() {
        return "/component/jurnalAmount/tradeDetail";
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
    public String manproductChoose() {
        return "/component/product/chooseModal";
    }

    //提交审核选择审核人及填写审核备注
    @RequestMapping("/submit-audit/modal")
    public String submitAuditModal() {
        return "/component/audit/submitModal";
    }

    //采购单选择审核人Modal
    @RequestMapping("/audit-user/choose")
    public String auditUserChoose() {
        return "/component/audit/chooseAuditUserModal";
    }

    //采购单选择审核人Modal
    @RequestMapping("/material/choose")
    public String materialChoose() {
        return "/component/material/chooseModal";
    }

    //选择物料型号
    @RequestMapping("/material-modal/choose")
    public String materialModalChoose() {
        return "/component/materialModal/chooseModal";
    }

    //选择个人客户
    @RequestMapping("/customer-modal/choose")
    public String customerModalChoose() {
        return "/component/customer/chooseCustomerModal";
    }

    //选择企业客户
    @RequestMapping("/business-customer-modal/choose")
    public String businessCustomerModalChoose() {
        return "/component/customer/chooseBusinessCustomerModal";
    }

    //选择用户
    @RequestMapping("/user-modal/choose")
    public String chooseUserModal() {
        return "/component/user/chooseUserModal";
    }

    //选择公司
    @RequestMapping("/company-modal/chooseCompany")
    public String chooseCompanyModal() {
        return "/component/company/chooseCompanyModal";
    }

    //选择客户可退换商品
    @RequestMapping("/customer-rent-product-modal/choose")
    public String chooseRentProductModal() {
        return "/component/customer/chooseRentProductModal";
    }

    //选择客户可退物料
    @RequestMapping("/customer-can-return-material-modal/choose")
    public String chooseCustomerCanReturnMaterialModal() {
        return "/component/customer/chooseCanReturnMaterial";
    }
    //选择客户可换物料
    @RequestMapping("/customer-can-change-material-modal/choose")
    public String chooseCustomerCanChangeMaterialModal() {
        return "/component/customer/chooseCanChangeMaterial";
    }

    //选择客户地址
    @RequestMapping("/customer-address-modal/choose")
    public String chooseCustomerAddressModal() {
        return "/component/customer/chooseCustomerAddressModal";
    }

    //输入地址
    @RequestMapping("/address-modal/input")
    public String inputAddressModal() {
        return "/component/address/inputAddressModal";
    }

    //输入地址
    @RequestMapping("/common-modal/remark")
    public String commonModalRemark() {
        return "/component/common/remarkModal";
    }


    //输入地址
    @RequestMapping("/view-work-flow/modal")
    public String viewWorkFlowModal() {
        return "/component/workFlow/viewWorkFlow";
    }


}
