package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.message.MessageQueryParam;
import com.lxzl.erp.common.domain.message.pojo.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageControllerTest extends ERPUnTransactionalTest{
    @Test
    public void sendMessage() throws Exception {
        Message message =new Message();
        message.setMessageText("nihao");
        message.setTitle("123");
        List<Integer> receiverUserIdList = new ArrayList<>();
//        receiverUserIdList.add(500004);
        receiverUserIdList.add(500005);
 //       receiverUserIdList.add(500006);

        message.setReceiverUserIdList(receiverUserIdList);
        TestResult result = getJsonTestResult("/message/sendMessage",message);
    }

    /**
     *
     * @throws Exception
     */

    @Test
    public void pageSendMessage() throws Exception{

        MessageQueryParam messageQueryParam = new MessageQueryParam();
        messageQueryParam.setPageNo(1);
        messageQueryParam.setPageSize(10);
        TestResult result = getJsonTestResult("/message/pageSendMessage",messageQueryParam);
    }


    @Test
    public void queryMessage() throws Exception {
        Message message =new Message();
        message.setMessageId(5);

        TestResult result = getJsonTestResult("/message/queryMessage",message);
    }

    @Test
    public void pageReceiveMessage() throws Exception{

        MessageQueryParam messageQueryParam = new MessageQueryParam();
        messageQueryParam.setPageNo(1);
        messageQueryParam.setPageSize(10);

        TestResult result = getJsonTestResult("/message/pageReceiveMessage",messageQueryParam);
    }

    @Test
    public void superSendMessage() throws Exception {
        Message message =new Message();
        message.setMessageText("系统内容");
        message.setTitle("系统");
        List<Integer> receiverUserIdList = new ArrayList<>();
        receiverUserIdList.add(500002);
        receiverUserIdList.add(500003);

        message.setReceiverUserIdList(receiverUserIdList);
        TestResult result = getJsonTestResult("/message/sendMessage",message);
    }

    @Test
    public void noReadCount() throws Exception{


        TestResult result = getJsonTestResult("/message/noReadCount",null);
    }

}