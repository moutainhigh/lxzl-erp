package com.lxzl.erp.web.service;

import com.lxzl.erp.core.service.material.impl.support.BulkMaterialSupport;
import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-16 10:52
 */
public class MaterialTest  extends BaseUnTransactionalTest {

    @Autowired
    private BulkMaterialSupport bulkMaterialSupport;

    @Test
    public void testQuery(){
        BulkMaterialDO bulkMaterialDO = bulkMaterialSupport.queryFitBulkMaterialDO(23);
    }
}
