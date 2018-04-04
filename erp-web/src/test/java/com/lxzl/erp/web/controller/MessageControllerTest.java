package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.message.MessageBatchReadParam;
import com.lxzl.erp.common.domain.message.MessageQueryParam;
import com.lxzl.erp.common.domain.message.pojo.Message;
import com.lxzl.erp.common.domain.payment.ManualDeductParam;
import com.lxzl.erp.common.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
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
 //       receiverUserIdList.add(500005);
        receiverUserIdList.add(500371);
 //       receiverUserIdList.add(500006);

        message.setReceiverUserIdList(receiverUserIdList);
        TestResult testResult = getJsonTestResult("/message/sendMessage",message);
    }

    @Test
    public void TestSendMessage() throws Exception {

        String str = "{\"title\":\"She threw out her hands to him, palms up, in the age-old gesture of appeal and her heart, again,was in her face.\",\"messageText\":\"“Scarlett, I was never one to patiently pick up broken fragments and glue them together and tell myself that the mended whole was as good as new. What is broken is broken—and I’d rather remember it as it was at its best than mend it and see the broken places as long as I lived. Perhaps,if I were younger—” he sighed. “But I’m too old to believe in such sentimentalities as clean slates and starting all over. I’m too old to shoulder the burden of constant lies that go with living in polite disillusionment. I couldn’t live with you and lie to you and I certainly couldn’t lie to myself. I can’t even lie to you now. I wish I could care what you do or where you go, but I can’t.”\",\"receiverUserIdList\":[\"500001\"]}";
        Message message = JSONUtil.convertJSONToBean(str, Message.class);

        TestResult testResult = getJsonTestResult("/message/sendMessage",message);
    }


    /**
     *
     * @throws Exception
     */

    @Test
    public void pageSendMessage() throws Exception{

        MessageQueryParam messageQueryParam = new MessageQueryParam();
        messageQueryParam.setPageNo(2);
        messageQueryParam.setPageSize(5);
//        messageQueryParam.setIsRead(1);

        TestResult testResult = getJsonTestResult("/message/pageSendMessage",messageQueryParam);
    }


    @Test
    public void queryMessage() throws Exception {
        Message message =new Message();
        message.setMessageId(256);//817 839 840 841 845

        TestResult testResult = getJsonTestResult("/message/queryMessage",message);
    }

    @Test
    public void pageReceiveMessage() throws Exception{

        MessageQueryParam messageQueryParam = new MessageQueryParam();

        messageQueryParam.setPageNo(2);
        messageQueryParam.setPageSize(10);
//        messageQueryParam.setIsRead(0);

        TestResult testResult = getJsonTestResult("/message/pageReceiveMessage",messageQueryParam);
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
        TestResult testResult = getJsonTestResult("/message/sendMessage",message);
    }

    @Test
    public void noReadCount() throws Exception{


        TestResult testResult = getJsonTestResult("/message/noReadCount",null);
    }
    @Test
    public void batchRead() throws Exception{

        MessageBatchReadParam param=new MessageBatchReadParam();
        Message mes=new Message();
        mes.setMessageId(896);
        List<Message> messList = new ArrayList<Message>();
        messList.add(mes);
        mes=new Message();
        mes.setMessageId(900);
        messList.add(mes);
        mes=new Message();
        mes.setMessageId(901);
        messList.add(mes);
        param.setMessageList(messList);

        TestResult testResult = getJsonTestResult("/message/batchRead",param);
    }

}