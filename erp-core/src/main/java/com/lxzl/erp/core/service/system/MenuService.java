package com.lxzl.erp.core.service.system;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Menu;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/11/1.
 * Time: 14:26.
 */
public interface MenuService extends BaseService {

    ServiceResult<String, List<Menu>> findRoleMenu();

    ServiceResult<String, List<Menu>> findAllMenu();

    ServiceResult<String, List<Menu>> getHomeMenu();

    ServiceResult<String, Menu> findByCode(Integer menuId);

    Integer addMenu(Menu menu);

    Integer updateMenu(Menu menu);

}
