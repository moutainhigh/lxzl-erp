package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.peer.PeerQueryParam;
import com.lxzl.erp.common.domain.peer.pojo.Peer;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/13
 * @Time : Created in 19:04
 */
public class PeerControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addPeer() throws Exception {
        Peer peer = new Peer();
        peer.setPeerName("王五");
        peer.setPeerCode("asndjkan11");
        peer.setProvince(34);
        peer.setCity(35);
        peer.setDistrict(36);
        TestResult result = getJsonTestResult("/peer/addPeer", peer);
    }

    @Test
    public void updatePeer() throws Exception {
        Peer peer = new Peer();
        peer.setPeerNo("LXPEER099900003");
        peer.setPeerName("hahah");
        peer.setPeerCode("1111");
        peer.setProvince(11);
        peer.setCity(10);
        peer.setDistrict(9);
        TestResult result = getJsonTestResult("/peer/updatePeer", peer);
    }

    @Test
    public void queryDetail() throws Exception {
        Peer peer = new Peer();
//        peer.setPeerId(2);
        peer.setPeerNo("LXPEER099900003");
        TestResult result = getJsonTestResult("/peer/queryDetail", peer);
    }

    @Test
    public void queryPage() throws Exception {
        PeerQueryParam peerQueryParam = new PeerQueryParam();
//        peerQueryParam.setPeerName("李四");
//        peerQueryParam.setPeerCode("11111");
//        peerQueryParam.setPeerNo("LXPEER031700002");
//        peerQueryParam.setProvinceId(12);
//        peerQueryParam.setCityId(10);
        peerQueryParam.setDistrictId(14);
        TestResult result = getJsonTestResult("/peer/queryPage", peerQueryParam);
    }

}