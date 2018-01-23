package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderCommitParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialBulkQueryGroup;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderProductEquipmentQueryGroup;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderMaterialBulk;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrderProductEquipment;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.peerDeploymentOrder.PeerDeploymentOrderService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    /**
     * 创建同行调拨单
     * @param peerDeploymentOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(@RequestBody @Validated(AddGroup.class)PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.createPeerDeploymentOrder(peerDeploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 修改同行调拨单
     * @param peerDeploymentOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class)PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.updatePeerDeploymentOrder(peerDeploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 提交同行调拨单
     * @param peerDeploymentOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "commitPeerDeploymentOrderInto", method = RequestMethod.POST)
    public Result commitPeerDeploymentOrderInto(@RequestBody @Validated(ExtendGroup.class) PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.commitPeerDeploymentOrderInto(peerDeploymentOrderCommitParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 确认收货同行调配单
     * @param peerDeploymentOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "confirmPeerDeploymentOrderInto", method = RequestMethod.POST)
    public Result confirmPeerDeploymentOrderInto(@RequestBody PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.confirmPeerDeploymentOrderInto(peerDeploymentOrderCommitParam.getPeerDeploymentOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 取消确认收货
     * @param peerDeploymentOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody @Validated(IdGroup.class)PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.cancelPeerDeploymentOrder(peerDeploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 提交同行调拨单归还审核
     */
    @RequestMapping(value = "commitPeerDeploymentOrderReturn", method = RequestMethod.POST)
    public Result commitPeerDeploymentOrderReturn(@RequestBody @Validated(ExtendGroup.class) PeerDeploymentOrderCommitParam peerDeploymentOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.commitPeerDeploymentOrderReturn(peerDeploymentOrderCommitParam.getPeerDeploymentOrderNo(),peerDeploymentOrderCommitParam.getVerifyUserId(),peerDeploymentOrderCommitParam.getVerifyMatters(),peerDeploymentOrderCommitParam.getRemark());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 确认退回
     */
    @RequestMapping(value = "endPeerDeploymentOrderOut", method = RequestMethod.POST)
    public Result endPeerDeploymentOrderOut(@RequestBody @Validated(IdGroup.class) PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = peerDeploymentOrderService.endPeerDeploymentOrderOut(peerDeploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 同行调拨单分页显示
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody PeerDeploymentOrderQueryParam peerDeploymentOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<PeerDeploymentOrder>> serviceResult = peerDeploymentOrderService.page(peerDeploymentOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 同行调拨单详情显示
     */
    @RequestMapping(value = "detailPeerDeploymentOrder", method = RequestMethod.POST)
    public Result detailPeerDeploymentOrder(@RequestBody @Validated(IdGroup.class) PeerDeploymentOrder peerDeploymentOrder, BindingResult validResult) {
        ServiceResult<String, PeerDeploymentOrder> serviceResult = peerDeploymentOrderService.detailPeerDeploymentOrder(peerDeploymentOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    /**
     * 同行调拨单商品详情列表
     */
    @RequestMapping(value = "detailPeerDeploymentOrderProductEquipment", method = RequestMethod.POST)
    public Result detailPeerDeploymentOrderProductEquipment(@RequestBody @Validated(IdGroup.class) PeerDeploymentOrderProductEquipmentQueryGroup peerDeploymentOrderProductEquipmentQueryGroup, BindingResult validResult) {
        ServiceResult<String, Page<PeerDeploymentOrderProductEquipment>> serviceResult = peerDeploymentOrderService.detailPeerDeploymentOrderProductEquipment(peerDeploymentOrderProductEquipmentQueryGroup);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 同行调拨单散料详情列表
     */
    @RequestMapping(value = "detailPeerDeploymentOrderMaterialBulk", method = RequestMethod.POST)
    public Result detailPeerDeploymentOrderMaterialBulk(@RequestBody @Validated(IdGroup.class) PeerDeploymentOrderMaterialBulkQueryGroup peerDeploymentOrderMaterialBulkQueryGroup, BindingResult validResult) {
        ServiceResult<String, Page<PeerDeploymentOrderMaterialBulk>> serviceResult = peerDeploymentOrderService.detailPeerDeploymentOrderMaterialBulk(peerDeploymentOrderMaterialBulkQueryGroup);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
