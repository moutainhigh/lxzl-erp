package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.peerDeploymentOrder.PeerDeploymentOrderService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: kai
 * @Description：
 * @Date: Created in 15:39 2018/1/13
 * @Modified By:
 */

@RequestMapping("peerDeploymentOrder")
@Controller
@ControllerLog
public class PeerDeploymentOrderController extends BaseController {

    @Autowired
    private PeerDeploymentOrderService peerDeploymentOrderService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(@RequestBody PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.createPeerDeploymentOrder(peerDeploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

//    /**
//     * 取消同行调拨单
//     */
//    @RequestMapping(value = "cancel", method = RequestMethod.POST)
//    public Result cancel(@RequestBody PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
//        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.cancel(peerDeploymentOrder);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }

}
