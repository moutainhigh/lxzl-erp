package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.lxzl.erp.TestResult;
/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-03 12:00
 */
public class CompanyServiceTest extends BaseUnTransactionalTest {

    @Autowired
    private CompanyService companyService;

    @Test
    public void test() {
        DepartmentQueryParam param = new DepartmentQueryParam();
        ServiceResult<String, List<Department>> result = companyService.getDepartmentList(param);
    }
}
