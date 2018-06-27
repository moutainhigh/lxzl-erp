package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.dynamicSql.pojo.DynamicSql;
import org.junit.Test;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/26
 */
public class DynamicSqlControllerTest extends ERPUnTransactionalTest {

    @Test
    public void testSelect() throws Exception {
//        String sql = "select * from erp_user eu left join erp_user_role eur on eur.user_id = eu.id";
//        String sql2 = "update erp_joint_product set data_status = 2 where id = 33";
//        String sql3 = "select eu.id as 唯一标识, eu.real_name as 姓名, eur.id as 唯一标识 from erp_user eu left join erp_user_role eur on eur.user_id = eu.id";
//        String sql4 = "select * from (select eu.id as 唯一标识, eu.real_name as 姓名 from erp_user eu left join erp_user_role eur on eur.user_id = eu.id) haha";
//        String sql5 = "select id, create_time from erp_user";
//        String sql6 = "select * from erp_statement_order_detail";
        String sql6 = "select * from erp_customer";


        DynamicSqlSelectParam dynamicSqlSelectParam = new DynamicSqlSelectParam();
        dynamicSqlSelectParam.setSql(sql6);
        TestResult result = getJsonTestResult("/dynamicSql/select", dynamicSqlSelectParam);
    }

    @Test
    public void testSave() throws Exception {
//        String sql = "select * from erp_user eu left join erp_user_role eur on eur.user_id = eu.id";
//        String sql2 = "update erp_joint_product set data_status = 2 where id = 33";
//        String sql3 = "select eu.id as 唯一标识, eu.real_name as 姓名, eur.id as 唯一标识 from erp_user eu left join erp_user_role eur on eur.user_id = eu.id";
        String sql4 = "select * from (select eu.id as 唯一标识, eu.real_name as 姓名 from erp_user eu left join erp_user_role eur on eur.user_id = eu.id) haha";
//        String sql5 = "select id, create_time from erp_user";
//        String sql6 = "select * from erp_statement_order_detail";
//        String sql6 = "select * from erp_customer";

        DynamicSql dynamicSql = new DynamicSql();
        dynamicSql.setSqlTitle("查询员工名称");
        dynamicSql.setSqlContent(sql4);
        dynamicSql.setRemark("测试保存数据");

        TestResult result = getJsonTestResult("/dynamicSql/create", dynamicSql);
    }

    @Test
    public void delete() throws Exception {
        DynamicSql dynamicSql = new DynamicSql();
        dynamicSql.setDynamicSqlId(4);

        TestResult result = getJsonTestResult("/dynamicSql/delete", dynamicSql);
    }

    @Test
    public void detail() throws Exception {
        DynamicSql dynamicSql = new DynamicSql();
        dynamicSql.setDynamicSqlId(2);

        TestResult result = getJsonTestResult("/dynamicSql/detail", dynamicSql);
    }

    @Test
    public void page() throws Exception {
        DynamicSqlQueryParam dynamicSqlQueryParam = new DynamicSqlQueryParam();
        dynamicSqlQueryParam.setSqlTitle("名称");

        TestResult result = getJsonTestResult("/dynamicSql/page", dynamicSqlQueryParam);
    }


}