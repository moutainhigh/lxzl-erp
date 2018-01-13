package com.lxzl.erp.core.service.peer.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.peer.PeerQueryParam;
import com.lxzl.erp.common.domain.peer.pojo.Peer;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.peer.PeerService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.peer.PeerMapper;
import com.lxzl.erp.dataaccess.domain.peer.PeerDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mongo.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/13
 * @Time : Created in 14:29
 */
@Service
public class PeerServiceImpl implements PeerService {
    /**
     * 添加同行信息
     *
     * @param : peer
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:45
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addPeer(Peer peer) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        //检验 供应商名称 和 供应商自定义编码
        ServiceResult<String, Integer> result = addQueryParam(peer);
        if (result != null) {
            return result;
        }
        Date now = new Date();
        PeerDO peerDO = ConverterUtil.convert(peer, PeerDO.class);
        peerDO.setPeerNo(generateNoSupport.generatePeerNo(peer.getCity()));
        peerDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        peerDO.setCreateTime(now);
        peerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        peerDO.setUpdateTime(now);
        peerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        peerMapper.save(peerDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(peerDO.getId());
        return serviceResult;
    }

    /**
     * 跟新同行信息
     *
     * @param : peer
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:47
     * @Return : com.lxzl.se.common.domain.Result
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updatePeer(Peer peer) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        PeerDO peerDO = peerMapper.findById(peer.getPeerId());
        if(peerDO == null){
            serviceResult.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return serviceResult;
        }
        //供应商编码是否存在
        if (StringUtil.isNotEmpty(peer.getPeerNo())) {
            Map<String, Object> map = new HashMap<>();
            PeerQueryParam peerNameQueryParam = new PeerQueryParam();
            peerNameQueryParam.setPeerNo(peer.getPeerNo());
            map.put("queryParam", peerNameQueryParam);
            map.put("start",0);
            map.put("pageSize",Integer.MAX_VALUE);
            List<PeerDO> peerDOList = peerMapper.listPage(map);
            if (peerDOList.get(0).getPeerNo() != peer.getPeerNo()) {
                serviceResult.setErrorCode(ErrorCode.PEER_NO_EXISTS);
                return serviceResult;
            }
        }

        //检验 供应商名称 和 供应商自定义编码
        ServiceResult<String, Integer> result = updateQueryParam(peer);
        if (result != null) {
            return result;
        }
        peerDO = ConverterUtil.convert(peer, PeerDO.class);
        Date now = new Date();
        peerDO.setCreateTime(now);
        peerDO.setCreateUser(userSupport.getCurrentUserId().toString());
        peerDO.setUpdateTime(now);
        peerDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        peerMapper.update(peerDO);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(peerDO.getId());
        return serviceResult;
    }

    /**
     * 查询详情
     *
     * @param : peerId
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:50
     * @Return : com.lxzl.se.common.domain.Result
     */
    @Override
    public ServiceResult<String, Peer> queryDetail(Integer peerId) {

        ServiceResult<String, Peer> serviceResult = new ServiceResult<>();
        PeerDO peerDO = peerMapper.findDetailByPeerId(peerId);
        if (peerDO == null) {
            serviceResult.setErrorCode(ErrorCode.PEER_NOT_EXISTS);
            return serviceResult;
        }
        Peer peer = ConverterUtil.convert(peerDO, Peer.class);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(peer);
        return serviceResult;
    }

    /**
     * 查询分页
     *
     * @param : peerQueryParam
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 14:50
     * @Return : com.lxzl.se.common.domain.Result
     */
    @Override
    public ServiceResult<String, Page<Peer>> queryPage(PeerQueryParam peerQueryParam) {
        ServiceResult<String, Page<Peer>> serviceResult = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(peerQueryParam.getPageNo(), peerQueryParam.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageQuery.getStart());
        map.put("pageSize", pageQuery.getPageSize());
        map.put("queryParam", peerQueryParam);
        Integer count = peerMapper.listCount(map);
        List<PeerDO> peerDOList = peerMapper.listPage(map);
        List<Peer> peerList = ConverterUtil.convertList(peerDOList, Peer.class);
        Page<Peer> peerPage = new Page<>(peerList, count, peerQueryParam.getPageNo(), peerQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(peerPage);
        return serviceResult;
    }

    /**
     * 跟新校验查询
     *
     * @param : peer
     * @param : peerDO
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 16:59
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    public ServiceResult<String, Integer> updateQueryParam(Peer peer) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        PeerQueryParam peerNameQueryParam = new PeerQueryParam();
        Map<String, Object> map = new HashMap<>();

        //同行名称是否重复
        peerNameQueryParam.setPeerName(peer.getPeerName());
        map.put("queryParam", peerNameQueryParam);
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        List<PeerDO> queryByPeerNameDOList = peerMapper.listPage(map);
        if (CollectionUtil.isNotEmpty(queryByPeerNameDOList)) {
            if (queryByPeerNameDOList.get(0).getId() != peer.getPeerId()) {
                serviceResult.setErrorCode(ErrorCode.PEER_NAME_EXISTS);
                return serviceResult;
            }
        }

        //同行自定义编码是否重复
        PeerQueryParam peerCodeQueryParam = new PeerQueryParam();
        peerCodeQueryParam.setPeerCode(peer.getPeerCode());
        map.put("queryParam", peerCodeQueryParam);
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        List<PeerDO> queryByPeerCodeList = peerMapper.listPage(map);
        if (CollectionUtil.isNotEmpty(queryByPeerCodeList)) {
            if (queryByPeerCodeList.get(0).getId() != peer.getPeerId()) {
                serviceResult.setErrorCode(ErrorCode.PEER_CODE_EXISTS);
                return serviceResult;
            }
        }
        return null;
    }

    /**
     * 添加校验查询
     *
     * @param : peer
     * @Author : XiaoLuYu
     * @Date : Created in 2018/1/13 16:59
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Integer>
     */
    public ServiceResult<String, Integer> addQueryParam(Peer peer) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();

        PeerQueryParam peerNameQueryParam = new PeerQueryParam();
        Map<String, Object> map = new HashMap<>();

        //同行名称是否重复
        peerNameQueryParam.setPeerName(peer.getPeerName());
        map.put("queryParam", peerNameQueryParam);
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        List<PeerDO> queryByPeerNameDOList = peerMapper.listPage(map);
        if (CollectionUtil.isNotEmpty(queryByPeerNameDOList)) {
            serviceResult.setErrorCode(ErrorCode.PEER_NAME_EXISTS);
            return serviceResult;
        }

        //同行自定义编码是否重复
        PeerQueryParam peerCodeQueryParam = new PeerQueryParam();
        peerCodeQueryParam.setPeerCode(peer.getPeerCode());
        map.put("queryParam", peerCodeQueryParam);
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        List<PeerDO> queryByPeerCodeList = peerMapper.listPage(map);
        if (CollectionUtil.isNotEmpty(queryByPeerCodeList)) {
            serviceResult.setErrorCode(ErrorCode.PEER_CODE_EXISTS);
            return serviceResult;
        }

        return null;
    }

    @Autowired
    PeerMapper peerMapper;
    @Autowired
    GenerateNoSupport generateNoSupport;
    @Autowired
    UserSupport userSupport;
}
