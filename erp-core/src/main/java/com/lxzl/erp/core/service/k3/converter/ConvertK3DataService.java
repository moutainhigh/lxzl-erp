package com.lxzl.erp.core.service.k3.converter;

import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;

public interface ConvertK3DataService {
    Object getK3PostWebServiceData(Integer postK3OperatorType , Object data);
    void successNotify(K3SendRecordDO k3SendRecordDO);
    void failNotify(K3SendRecordDO k3SendRecordDO);
}
