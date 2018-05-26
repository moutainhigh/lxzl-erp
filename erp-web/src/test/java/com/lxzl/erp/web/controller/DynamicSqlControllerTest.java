package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSql;
import org.junit.Test;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
public class DynamicSqlControllerTest extends ERPTransactionalTest {

    @Test
    public void testSelect() throws Exception {
        String sql = "select * from erp_joint_product";
        String sql2 = "update erp_joint_product set data_status = 2 where id = 33";

        DynamicSql dynamicSql = new DynamicSql();
        dynamicSql.setSql(sql);
        getJsonTestResult("/dynamicSql/select", dynamicSql);
    }
}