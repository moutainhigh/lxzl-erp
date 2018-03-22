package com.lxzl.erp.core.service.bank.impl;

import com.lxzl.erp.common.constant.BankType;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.exclt.impl.bank.*;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.bank.BankSlipMapper;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDO;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pengbinjie
 * @Description：
 * @Date: Created in 19:49 2018/3/20
 * @Modified By:
 */
@Service
public class BankSlipServiceImpl implements BankSlipService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    ImportTrafficBank importTrafficBank;

    @Autowired
    ImportChinaBank importChinaBank;

    @Autowired
    ImportNanJingBank importNanJingBank;

    @Autowired
    ImportAgricultureBank importAgricultureBank;

    @Autowired
    ImportICBCBank importICBCBank;

    @Autowired
    ImportCCBBank importCCBBank;

    @Autowired
    ImportPingAnBank importPingAnBank;

    @Autowired
    ImportShanghaiPudongDevelopmentBank importShanghaiPudongDevelopmentBank;

    @Autowired
    ImportAlipay importAlipay;

    @Autowired
    ImportCMBCBank importCMBCBank;
    @Autowired
    private BankSlipMapper bankSlipMapper;

    @Autowired
    private UserSupport userSupport;

    @Override
    public ServiceResult<String, Page<BankSlip>> pageBankSlip(BankSlipQueryParam bankSlipQueryParam) {
        ServiceResult<String, Page<BankSlip>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        Integer departmentType = 0;
        if (userSupport.isFinancePerson()) {
            //财务人员类型设置为1
            departmentType = 1;
        } else if (userSupport.isBusinessAffairsPerson() || userSupport.isBusinessPerson()) {
            //商务和业务员类型设置为2
            departmentType = 2;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("bankSlipQueryParam", bankSlipQueryParam);
        maps.put("departmentType", departmentType);
        Integer totalCount = bankSlipMapper.findBankSlipCountByParams(maps);
        List<BankSlipDO> bankSlipDOList = bankSlipMapper.findBankSlipByParams(maps);

        List<BankSlip> bankSlipList = ConverterUtil.convertList(bankSlipDOList, BankSlip.class);
        Page<BankSlip> page = new Page<>(bankSlipList, totalCount, bankSlipQueryParam.getPageNo(), bankSlipQueryParam.getPageSize());

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(page);
        return result;
    }

    @Override
    public ServiceResult<String, String> saveBankSlip(BankSlip bankSlip) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Integer bankType = bankSlip.getBankType();
        if (!BankType.BOC_BANK.equals(bankType) &&
                !BankType.TRAFFIC_BANK.equals(bankType) &&
                !BankType.NAN_JING_BANK.equals(bankType) &&
                !BankType.AGRICULTURE_BANK.equals(bankType) &&
                !BankType.ICBC_BANK.equals(bankType) &&
                !BankType.CCB_BANK.equals(bankType) &&
                !BankType.PING_AN_BANK.equals(bankType) &&
                !BankType.CMBC_BANK.equals(bankType) &&
                !BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType) &&
                !BankType.ALIPAY.equals(bankType)) {
            serviceResult.setErrorCode(ErrorCode.BANK_TYPE_IS_FAIL);
            return serviceResult;
        }
        String excelUrl = bankSlip.getExcelUrl();
        URL url = new URL(excelUrl);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(3 * 60 * 1000);
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
        } catch (Exception e) {
            logger.error("-----------------连接fastdfs失败------------------------", e);
            serviceResult.setErrorCode(ErrorCode.EXCEL_UPLOAD_ERROR);
            return serviceResult;
        }

        if (inputStream == null) {
            serviceResult.setErrorCode(ErrorCode.EXCEL_SHEET_IS_NULL);
            return serviceResult;
        }


        if (BankType.BOC_BANK.equals(bankType)) {
            serviceResult = importChinaBank.saveChinaBank(bankSlip, inputStream);
        } else if (BankType.TRAFFIC_BANK.equals(bankType)) {
            serviceResult = importTrafficBank.saveTrafficBank(bankSlip, inputStream);
        } else if (BankType.NAN_JING_BANK.equals(bankType)) {
            serviceResult = importNanJingBank.saveNanJingBank(bankSlip, inputStream);
        } else if (BankType.AGRICULTURE_BANK.equals(bankType)) {
            serviceResult = importAgricultureBank.saveAgricultureBank(bankSlip, inputStream);
        } else if (BankType.ICBC_BANK.equals(bankType)) {
            serviceResult = importICBCBank.saveICBCBank(bankSlip, inputStream);
        } else if (BankType.CCB_BANK.equals(bankType)) {
            serviceResult = importCCBBank.saveCCBBank(bankSlip, inputStream);
        } else if (BankType.PING_AN_BANK.equals(bankType)) {
            serviceResult = importPingAnBank.savePingAnBank(bankSlip, inputStream);
        } else if (BankType.CMBC_BANK.equals(bankType)) {
            serviceResult = importCMBCBank.saveCMBCBank(bankSlip, inputStream);
        } else if (BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType)) {
            serviceResult = importShanghaiPudongDevelopmentBank.saveShanghaiPudongDevelopmentBank(bankSlip, inputStream);
        } else if (BankType.ALIPAY.equals(bankType)) {
            serviceResult = importAlipay.saveAlipay(bankSlip, inputStream);
        }
        return serviceResult;
    }
}
