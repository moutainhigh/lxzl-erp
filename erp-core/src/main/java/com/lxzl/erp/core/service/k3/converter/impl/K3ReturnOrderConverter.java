package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSEOutStock;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSEOutStockEntry;
import com.lxzl.erp.core.service.k3.K3Support;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service
public class K3ReturnOrderConverter implements ConvertK3DataService {

    @Autowired
    private K3Support k3Support;
    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType, Object data) throws Exception {
        K3ReturnOrder k3ReturnOrder = (K3ReturnOrder) data;
        FormSEOutStock formSEOutStock = new FormSEOutStock();
        formSEOutStock.setAddress(getString(k3ReturnOrder.getReturnAddress()));
        Calendar backDate = Calendar.getInstance();
        backDate.setTime(k3ReturnOrder.getReturnTime());
        formSEOutStock.setBackDate(backDate);//退还日期
        String returnModeString = null;
        if (ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR == k3ReturnOrder.getReturnMode()) {
            returnModeString = "01";
        } else if (ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_MAIL == k3ReturnOrder.getReturnMode()) {
            returnModeString = "03";
        }
        formSEOutStock.setBackMode(returnModeString);//退货方式01上门收货，02客户自还，03客户快递，04转让设备
        formSEOutStock.setBackType("02");//退货类型 01发货 02退货 03 退换货
        formSEOutStock.setBillNO(k3ReturnOrder.getReturnOrderNo());//单据编号
        formSEOutStock.setBiller(getString(k3ReturnOrder.getCreateUserRealName()));//创建人
        if (k3ReturnOrder.getDeliverySubCompanyId() != null && k3ReturnOrder.getDeliverySubCompanyId() != 0) {
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(k3ReturnOrder.getDeliverySubCompanyId());
            if (subCompanyDO != null) {
                formSEOutStock.setBackCompanyNO(k3Support.getK3CityCode(subCompanyDO.getSubCompanyCode()));
            }
        }
        formSEOutStock.setContacts(getString(k3ReturnOrder.getReturnContacts()));
        formSEOutStock.setCustName(getString(k3ReturnOrder.getK3CustomerName()));
        formSEOutStock.setCustNumber(getString(k3ReturnOrder.getK3CustomerNo()));
        Calendar createTime = Calendar.getInstance();
        createTime.setTime(k3ReturnOrder.getCreateTime());
        formSEOutStock.setDate(createTime);//日期
        formSEOutStock.setNote(getString(k3ReturnOrder.getRemark()));
        formSEOutStock.setPhone(getString(k3ReturnOrder.getReturnPhone()));
        List<K3ReturnOrderDetail> k3ReturnOrderDetailList = k3ReturnOrder.getK3ReturnOrderDetailList();
        if (CollectionUtil.isNotEmpty(k3ReturnOrderDetailList)) {
            FormSEOutStockEntry[] formSEOutStockEntries = new FormSEOutStockEntry[k3ReturnOrder.getK3ReturnOrderDetailList().size()];
            for (int i = 0; i < k3ReturnOrderDetailList.size(); i++) {
                K3ReturnOrderDetail k3ReturnOrderDetail = k3ReturnOrderDetailList.get(i);
                FormSEOutStockEntry formSEOutStockEntry = new FormSEOutStockEntry();
                formSEOutStockEntry.setProductNumber(k3ReturnOrderDetail.getProductNo());
                formSEOutStockEntry.setNote(getString(k3ReturnOrderDetail.getRemark()));
                formSEOutStockEntry.setOrderNO(getString(k3ReturnOrderDetail.getOrderNo()));
                formSEOutStockEntry.setQty(new BigDecimal(k3ReturnOrderDetail.getProductCount()));
                formSEOutStockEntry.setOrderEntry(k3ReturnOrderDetail.getOrderEntry() == null ? 0 : Integer.parseInt(k3ReturnOrderDetail.getOrderEntry()));
                formSEOutStockEntries[i] = formSEOutStockEntry;
            }
            formSEOutStock.setEntryList(formSEOutStockEntries);
        }

        return formSEOutStock;
    }

    @Override
    public void successNotify(K3SendRecordDO k3SendRecordDO) {

    }

    @Override
    public void failNotify(K3SendRecordDO k3SendRecordDO) {

    }


    private String getString(String str) {
        return str == null ? "" : str;
    }
}
