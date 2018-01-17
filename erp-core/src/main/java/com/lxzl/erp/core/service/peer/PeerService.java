package com.lxzl.erp.core.service.peer;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peer.PeerQueryParam;
import com.lxzl.erp.common.domain.peer.pojo.Peer;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/13
 * @Time : Created in 14:29
 */
public interface PeerService {
    /**
    * 添加同行信息
    * @Author : XiaoLuYu
    * @Date : Created in 2018/1/13 14:45
    * @param :
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
    */
    ServiceResult<String,Integer> addPeer(Peer peer);
    /**
     * 跟新同行信息
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:47
     * @param : peer
     * @param : validResult
     * @Return : com.lxzl.se.common.domain.Result
     */
    ServiceResult<String,Integer> updatePeer(Peer peer);
    /**
     * 查询详情
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:50
     * @param : peerNo
    * @param : validResult
     * @Return : com.lxzl.se.common.domain.Result
     */
    ServiceResult<String, Peer> queryDetail(String peerNo);
    /**
     * 查询分页
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:50
     * @param : peer
     * @param : validResult
     * @Return : com.lxzl.se.common.domain.Result
     */
    ServiceResult<String,Page<Peer>> queryPage(PeerQueryParam peerQueryParam);
}
