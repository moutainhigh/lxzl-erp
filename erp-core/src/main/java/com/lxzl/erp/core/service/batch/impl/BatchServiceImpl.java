package com.lxzl.erp.core.service.batch.impl;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.batch.BatchService;
import com.lxzl.erp.core.service.dingding.DingDingSupport.DingDingSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/7/12
 */
@Service
public class BatchServiceImpl implements BatchService {

    private static final Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);

    @Override
    public String batchCreateK3ReturnOrderStatement(List<String> returnOrderNoList) {
        StringBuffer sb = new StringBuffer();
        Set<String> returnOrderNoSet = new HashSet<>();
        for (String returnOrderNo : returnOrderNoList) {
            returnOrderNoSet.add(returnOrderNo);
        }
        if (CollectionUtil.isNotEmpty(returnOrderNoSet)) {
            for (String returnOrderNo : returnOrderNoSet) {
                try {
                    if (StringUtil.isEmpty(returnOrderNo)) {
                        continue;
                    }
                    ServiceResult<String, BigDecimal> result = statementService.createK3ReturnOrderStatement(returnOrderNo);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        String json = JSON.toJSONString(resultGenerator.generate(result.getErrorCode()));
                        sb.append("创建K3退货结算单【失败】：退货单号[" + returnOrderNo + "]" + json + "\n");
                    } else {
                        sb.append("成功重算订单：退货单号[" + returnOrderNo + "]\n");
                    }
                } catch (Exception e) {
                    logger.error("创建K3退货结算单【系统错误】：退货单号[" + returnOrderNo + "]", e);
                    sb.append("创建K3退货结算单【系统错误】：退货单号[" + returnOrderNo + "]" + e.getMessage() + "\n");
                }
            }
        }
        String message = sb.toString();
        if (StringUtil.isNotEmpty(message)) {
            logger.info(message);
            dingDingSupport.dingDingSendMessage(message);
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, String> batchReturnDeposit(List<String> orderNos) {
        ServiceResult<String, String> result = new ServiceResult<>();

        Set<String> orderNoSet = new HashSet<>();
        for (String orderNo : orderNos) {
            orderNoSet.add(orderNo);
        }

        StringBuilder sb = new StringBuilder();

        if (CollectionUtil.isNotEmpty(orderNoSet)) {
            for (String orderNo : orderNoSet) {
                if (StringUtil.isEmpty(orderNo)) {
                    continue;
                }
                sb.append(dingDingSupport.getEnvironmentString());
                try {
                    String message = statementService.returnDeposit(orderNo);
                    sb.append(message);
                } catch (Exception e) {
                    sb.append("订单退押金【系统错误】：订单号【" + orderNo + "】" + e.getMessage() + "\n");
                }

            }
        }

        logger.info(sb.toString());
        dingDingSupport.dingDingSendMessage(sb.toString());

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    @Autowired
    private StatementService statementService;
    @Autowired
    private DingDingSupport dingDingSupport;
    @Autowired
    private ResultGenerator resultGenerator;
}
