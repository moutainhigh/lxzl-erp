package com.lxzl.erp.web.test;

import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageContent;
import com.lxzl.erp.common.domain.dingding.DingdingSendTextMessageRequest;
import com.lxzl.erp.common.util.DingdingPropertiesUtil;
import com.lxzl.erp.core.service.dingding.DingdingService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-25 20:15
 */
public class DingdingTest extends BaseUnTransactionalTest {

    @Autowired
    private DingdingService dingdingService;

    @Test
    public void testSendMessage() {
        DingdingSendTextMessageRequest request = new DingdingSendTextMessageRequest();
        request.setMsgtype("text");
        DingdingSendTextMessageContent content = new DingdingSendTextMessageContent();
        content.setContent("商品[一台测试机]库存已经低于预警库存，请通知采购部门采购。【凌雄租赁】");
        request.setText(content);
        String response = dingdingService.sendUserGroupMessage(DingdingPropertiesUtil.DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER, request);
    }
}
