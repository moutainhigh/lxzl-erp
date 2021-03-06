package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.peer.PeerQueryParam;
import com.lxzl.erp.common.domain.peer.pojo.Peer;
import org.junit.Test;

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
        peer.setPeerCode("222");
        peer.setProvince(34);
        peer.setCity(35);
        peer.setDistrict(36);
//        peer.setBeneficiaryAccount("12345678aaaaaaaaa");
        TestResult result = getJsonTestResult("/peer/addPeer", peer);
    }

    @Test
    public void updatePeer() throws Exception {
        Peer peer = new Peer();
        peer.setPeerNo("LXPEER-0999-00006");
        peer.setPeerName("王五5");
        peer.setPeerCode("111");
//        peer.setBeneficiaryAccount("12345678aaaaaaaaa");
        peer.setProvince(11);
        peer.setCity(10);
        peer.setDistrict(9);

//        peer.setBeneficiaryAccount("4512 1254 7894 1226 522");
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
        peerQueryParam.setDistrictId(1);
        TestResult result = getJsonTestResult("/peer/queryPage", peerQueryParam);
    }

}