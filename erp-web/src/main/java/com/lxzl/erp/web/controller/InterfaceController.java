package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerAccountLogParam;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerQueryParam;
import com.lxzl.erp.common.domain.erpInterface.order.InterfaceOrderQueryParam;
import com.lxzl.erp.common.domain.erpInterface.statementOrder.InterfaceStatementOrderPayParam;
import com.lxzl.erp.common.domain.erpInterface.statementOrder.InterfaceStatementOrderQueryParam;
import com.lxzl.erp.common.domain.erpInterface.subCompany.InterfaceSubCompanyQueryParam;
import com.lxzl.erp.common.domain.erpInterface.weiXin.InterfaceWeixinChargeParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerPersonGroup;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNameGroup;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNoGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.businessSystemConfig.BusinessSystemConfigService;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.web.util.NetworkUtil;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-14 17:09
 */
@Controller
@ControllerLog
@RequestMapping("interface")
public class InterfaceController extends BaseController {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StatementService statementService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BusinessSystemConfigService businessSystemConfigService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody @Validated(QueryCustomerNoGroup.class) InterfaceOrderQueryParam interfaceOrderQueryParam, BindingResult validResult) {

        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceOrderQueryParam.getErpAppId(), interfaceOrderQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Order> serviceResult = orderService.queryOrderByNo(interfaceOrderQueryParam.getOrderNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }

        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryAllOrder", method = RequestMethod.POST)
    public Result queryAllOrder(@RequestBody @Validated InterfaceOrderQueryParam interfaceOrderQueryParam, BindingResult validResult) {

        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceOrderQueryParam.getErpAppId(), interfaceOrderQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Page<Order>> serviceResult = orderService.queryOrderByUserIdInterface(interfaceOrderQueryParam);
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }

        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryCustomerByName", method = RequestMethod.POST)
    public Result queryCustomerByName(@RequestBody @Validated(QueryCustomerNameGroup.class) InterfaceCustomerQueryParam interfaceCustomerQueryParam, BindingResult validResult) {

        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceCustomerQueryParam.getErpAppId(), interfaceCustomerQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Customer> serviceResult = customerService.queryCustomerByCustomerName(interfaceCustomerQueryParam.getCustomerName());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryStatementOrder", method = RequestMethod.POST)
    public Result queryStatementOrder(@RequestBody @Validated InterfaceStatementOrderQueryParam interfaceStatementOrderQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceStatementOrderQueryParam.getErpAppId(), interfaceStatementOrderQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrder(interfaceStatementOrderQueryParam);
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryStatementOrderDetail", method = RequestMethod.POST)
    public Result queryStatementOrderDetail(@RequestBody @Validated InterfaceStatementOrderQueryParam interfaceStatementOrderQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceStatementOrderQueryParam.getErpAppId(), interfaceStatementOrderQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetail(interfaceStatementOrderQueryParam.getStatementOrderNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "weixinPayStatementOrder", method = RequestMethod.POST)
    public Result weixinPayStatementOrder(@RequestBody @Validated InterfaceStatementOrderPayParam interfaceStatementOrderPayParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceStatementOrderPayParam.getErpAppId(), interfaceStatementOrderPayParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, String> serviceResult = statementService.weixinPayStatementOrder(interfaceStatementOrderPayParam.getStatementOrderNo(), interfaceStatementOrderPayParam.getOpenId(), NetworkUtil.getIpAddress(request));
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "wechatCharge", method = RequestMethod.POST)
    public Result wechatCharge(@RequestBody @Validated InterfaceWeixinChargeParam interfaceWeixinChargeParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceWeixinChargeParam.getErpAppId(), interfaceWeixinChargeParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, String> serviceResult = paymentService.wechatCharge(interfaceWeixinChargeParam.getCustomerNo(), interfaceWeixinChargeParam.getAmount(), interfaceWeixinChargeParam.getOpenId(), NetworkUtil.getIpAddress(request));
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "queryChargeRecordPage", method = RequestMethod.POST)
    public Result queryChargeRecordPage(@RequestBody @Validated(QueryCustomerNoGroup.class) InterfaceCustomerQueryParam interfaceCustomerQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceCustomerQueryParam.getErpAppId(), interfaceCustomerQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Page<ChargeRecord>> serviceResult = paymentService.queryChargeRecordPage(interfaceCustomerQueryParam.getCustomerNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "queryCustomerByNo", method = RequestMethod.POST)
    public Result queryCustomerByNo(@RequestBody @Validated(QueryCustomerNoGroup.class) InterfaceCustomerQueryParam interfaceCustomerQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceCustomerQueryParam.getErpAppId(), interfaceCustomerQueryParam.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Customer> serviceResult = customerService.queryCustomerByNo(interfaceCustomerQueryParam.getCustomerNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "weixinQueryCustomerAccountLogPage", method = RequestMethod.POST)
    public Result weixinQueryCustomerAccountLogPage(@RequestBody @Validated InterfaceCustomerAccountLogParam param, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(param.getErpAppId(), param.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Map<String, Object>> serviceResult = paymentService.weixinQueryCustomerAccountLogPage(param);
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "subCompanyPage", method = RequestMethod.POST)
    public Result pageSubCompany(@RequestBody @Validated InterfaceSubCompanyQueryParam param, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(param.getErpAppId(), param.getErpAppSecret());
        if (erpIdentity) {
            ServiceResult<String, Page<SubCompany>> serviceResult = companyService.subCompanyPage(param);
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "addPerson", method = RequestMethod.POST)
    public Result addPerson(@RequestBody @Validated(AddCustomerPersonGroup.class) InterfaceAddPersonParam param, BindingResult validResult) {
        if (!businessSystemConfigService.verifyErpIdentity(param.getErpAppId(), param.getErpAppSecret()))
            return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

        if (userSupport.getCurrentUser() == null) {
            User superUser = new User();
            superUser.setUserId(CommonConstant.SUPER_USER_ID);
            httpSession.setAttribute(CommonConstant.ERP_USER_SESSION_KEY, superUser);
        }
        ServiceResult<String, String> serviceResult = customerService.addPerson(param.getCustomer());
        httpSession.setAttribute(CommonConstant.ERP_USER_SESSION_KEY, null);
        if (serviceResult.getErrorCode().equals(ErrorCode.CUSTOMER_PERSON_IS_EXISTS))
            return resultGenerator.generate(ErrorCode.SUCCESS, serviceResult.getResult());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    private static class InterfaceAddPersonParam {
        @NotNull(message = ErrorCode.BUSINESS_APP_ID_NOT_NULL, groups = {AddCustomerPersonGroup.class})
        private String erpAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
        @NotNull(message = ErrorCode.BUSINESS_APP_SECRET_NOT_NULL, groups = {AddCustomerPersonGroup.class})
        private String erpAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
        @NotNull(message = ErrorCode.USER_NOT_NULL, groups = {AddCustomerPersonGroup.class})
        private Customer customer;

        public String getErpAppId() {
            return erpAppId;
        }

        public void setErpAppId(String erpAppId) {
            this.erpAppId = erpAppId;
        }

        public String getErpAppSecret() {
            return erpAppSecret;
        }

        public void setErpAppSecret(String erpAppSecret) {
            this.erpAppSecret = erpAppSecret;
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
    }
}
