package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.dataaccess.dao.mysql.system.SysMenuMapper;
import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-27 20:08
 */
public class SysMenuTest extends ERPUnTransactionalTest {

    @Test
    public void test() {
        List<SysMenuDO> list = new ArrayList<>();
        list.add(sysMenuMapper.findByMenuId(200315));
        list.add(sysMenuMapper.findByMenuId(200316));

        for (SysMenuDO sysMenuDO : list) {
            System.out.println("INSERT INTO `erp_sys_menu` (`id`,`menu_name`,`parent_menu_id`,`menu_order`,`is_folder`,`menu_url`,`level`,`data_status`,`remark`) VALUES(" + sysMenuDO.getId() + ",'" + sysMenuDO.getMenuName() + "'," + sysMenuDO.getParentMenuId()
                    + "," + sysMenuDO.getMenuOrder() + "," + sysMenuDO.getIsFolder() + ",'" + sysMenuDO.getMenuUrl() + "'," + sysMenuDO.getLevel() + ",1,'" + sysMenuDO.getRemark() + "') ;");
        }
    }

    @Autowired
    private SysMenuMapper sysMenuMapper;

}
