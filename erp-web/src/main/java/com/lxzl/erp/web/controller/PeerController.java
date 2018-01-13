package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peer.PeerQueryParam;
import com.lxzl.erp.common.domain.peer.pojo.Peer;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.peer.PeerService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/13
 * @Time : Created in 14:24
 */

@Controller
@ControllerLog
@RequestMapping("/peer")
public class PeerController {

    /**
    * 添加同行信息
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/13 14:30
    * @param : validResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "addPeer",method = RequestMethod.POST)
    public Result addPeer(@RequestBody @Validated(AddGroup.class) Peer peer, BindingResult validResult){
        ServiceResult<String,Integer> result = peerService.addPeer(peer);
        return resultGenerator.generate(result.getErrorCode(),result.getResult());
    }
    /**
    * 跟新同行信息
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/13 14:47
    * @param : peer
    * @param : validResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "updatePeer",method = RequestMethod.POST)
    public Result updatePeer(@RequestBody @Validated(UpdateGroup.class) Peer peer, BindingResult validResult){
        ServiceResult<String,Integer> result = peerService.updatePeer(peer);
        return resultGenerator.generate(result.getErrorCode(),result.getResult());
    }
    /**
    * 查询详情
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/13 14:50
    * @param : peer
    * @param : validResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "queryDetail",method = RequestMethod.POST)
    public Result queryDetail(@RequestBody @Validated(IdGroup.class) Peer peer, BindingResult validResult){
        ServiceResult<String, Peer> result = peerService.queryDetail(peer.getPeerId());
        return resultGenerator.generate(result.getErrorCode(),result.getResult());
    }
    /**
    * 查询分页
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/13 14:50
    * @param : peer
    * @param : validResult
    * @Return : com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "queryPage",method = RequestMethod.POST)
    public Result queryPage(@RequestBody PeerQueryParam peerQueryParam, BindingResult validResult){
        ServiceResult<String, Page<Peer>> result = peerService.queryPage(peerQueryParam);
        return resultGenerator.generate(result.getErrorCode(),result.getResult());
    }

    @Autowired
    private PeerService peerService;
    @Autowired
    private ResultGenerator resultGenerator;

}
