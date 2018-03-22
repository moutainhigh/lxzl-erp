package com.lxzl.erp.core.service.exclt.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.pojo.BankSlip;
import com.lxzl.erp.core.service.exclt.ImportBankSlipService;
import com.lxzl.erp.core.service.exclt.impl.bank.*;
import com.lxzl.erp.core.service.order.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/3/19
 * @Time : Created in 15:51
 */
@Service
public class ImportBankSlipServiceImpl implements ImportBankSlipService {

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


    @Override
    public ServiceResult<String, String> saveBankSlip(BankSlip bankSlip) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        Integer bankType = bankSlip.getBankType();
        if(!BankType.BOC_BANK.equals(bankType) &&
                !BankType.TRAFFIC_BANK.equals(bankType) &&
                !BankType.NAN_JING_BANK.equals(bankType) &&
                !BankType.AGRICULTURE_BANK.equals(bankType) &&
                !BankType.ICBC_BANK.equals(bankType) &&
                !BankType.CCB_BANK.equals(bankType) &&
                !BankType.PING_AN_BANK.equals(bankType) &&
                !BankType.CMBC_BANK.equals(bankType) &&
                !BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType) &&
                !BankType.ALIPAY.equals(bankType) ){
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
        }else if(BankType.TRAFFIC_BANK.equals(bankType)){
            serviceResult = importTrafficBank.saveTrafficBank(bankSlip, inputStream);
        }else if(BankType.NAN_JING_BANK.equals(bankType)){
            serviceResult = importNanJingBank.saveNanJingBank(bankSlip, inputStream);
        }else if(BankType.AGRICULTURE_BANK.equals(bankType)){
            serviceResult = importAgricultureBank.saveAgricultureBank(bankSlip, inputStream);
        }else if(BankType.ICBC_BANK.equals(bankType)){
            serviceResult = importICBCBank.saveICBCBank(bankSlip, inputStream);
        }else if(BankType.CCB_BANK.equals(bankType)){
            serviceResult = importCCBBank.saveCCBBank(bankSlip, inputStream);
        }else if(BankType.PING_AN_BANK.equals(bankType)){
            serviceResult = importPingAnBank.savePingAnBank(bankSlip, inputStream);
        }else if(BankType.CMBC_BANK.equals(bankType)){
            serviceResult = importCMBCBank.saveCMBCBank(bankSlip, inputStream);
        }else if(BankType.SHANGHAI_PUDONG_DEVELOPMENT_BANK.equals(bankType)){
            serviceResult = importShanghaiPudongDevelopmentBank.saveShanghaiPudongDevelopmentBank(bankSlip, inputStream);
        }else if(BankType.ALIPAY.equals(bankType)){
            serviceResult = importAlipay.saveAlipay(bankSlip, inputStream);
        }
        return serviceResult;
    }
}
