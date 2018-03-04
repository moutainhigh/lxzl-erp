package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delivery.pojo.DeliveryOrder;
import com.lxzl.erp.common.domain.k3.pojo.callback.K3DeliveryOrder;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.k3.K3CallbackService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-19 13:36
 */
@Controller
@ControllerLog
@RequestMapping("k3Callback")
public class K3CallbackController extends BaseController {


    @RequestMapping(value = "receiveDeliveryInfo", method = RequestMethod.POST)
    public Result receiveDeliveryInfo(@RequestBody DeliveryOrder deliveryOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3CallbackService.callbackDelivery(deliveryOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private K3CallbackService k3CallbackService;

}
