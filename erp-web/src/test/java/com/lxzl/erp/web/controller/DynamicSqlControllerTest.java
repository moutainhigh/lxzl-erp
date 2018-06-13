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
        String sql = "select * from erp_user eu left join erp_user_role eur on eur.user_id = eu.id";
        String sql2 = "update erp_joint_product set data_status = 2 where id = 33";
        String sql3 = "select eu.id as 唯一标识, eu.real_name as 姓名, eur.id as 唯一标识 from erp_user eu left join erp_user_role eur on eur.user_id = eu.id";
        String sql4 = "select * from (select eu.id as 唯一标识, eu.real_name as 姓名 from erp_user eu left join erp_user_role eur on eur.user_id = eu.id) haha";
        String sql5 = "select id, create_time from erp_user";
        String sql6 = "select * from erp_statement_order_detail";

        DynamicSql dynamicSql = new DynamicSql();
        dynamicSql.setSql(sql6);
        getJsonTestResult("/dynamicSql/select", dynamicSql);
    }
}