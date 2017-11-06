package com.lxzl.erp.core.service.system.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.system.pojo.Menu;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.service.system.MenuService;
import com.lxzl.erp.core.service.system.impl.support.ConvertMenu;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.dataaccess.dao.mysql.system.SysMenuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMapper;
import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/1.
 * Time: 14:28.
 */
@Service("MenuService")
public class MenuServiceImpl extends BaseServiceImpl implements MenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService roleService;

    @Autowired(required = false)
    private HttpSession session;

    @Override
    public Integer addMenu(Menu menu) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        SysMenuDO parentMenu = new SysMenuDO();
        SysMenuDO SysMenuDO = ConvertMenu.reverseConvert(menu);
        if (!CommonConstant.SUPER_MENU_ID.equals(menu.getParentMenuId())) {
            parentMenu = sysMenuMapper.findByMenuId(menu.getParentMenuId());
        }
        SysMenuDO.setLevel(parentMenu.getLevel() + 1);
        SysMenuDO.setDataStatus(CommonConstant.DATA_STATUS_DISABLE);
        if (loginUser != null) {
            SysMenuDO.setCreateUser(loginUser.getUserId().toString());
            SysMenuDO.setCreateTime(new Date());
            SysMenuDO.setUpdateUser(loginUser.getUserId().toString());
            SysMenuDO.setUpdateTime(new Date());
        }

        return sysMenuMapper.save(SysMenuDO);
    }

    @Override
    public Integer updateMenu(Menu menu) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        SysMenuDO parentMenu = new SysMenuDO();
        SysMenuDO SysMenuDO = ConvertMenu.reverseConvert(menu);
        if (!CommonConstant.SUPER_MENU_ID.equals(menu.getParentMenuId())) {
            parentMenu = sysMenuMapper.findByMenuId(menu.getParentMenuId());
        }

        SysMenuDO.setLevel(parentMenu.getLevel() + 1);
        SysMenuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        if (loginUser != null) {
            SysMenuDO.setUpdateUser(loginUser.getUserId().toString());
            SysMenuDO.setUpdateTime(new Date());
        }

        return sysMenuMapper.update(SysMenuDO);
    }

    private String verifyData(Menu menu, SysMenuDO parentMenu) {
        if (menu.getIsFolder() != 0 && StringUtil.isBlank(menu.getMenuUrl())) {
            return "功能类必须有URL";
        }
        if (menu.getIsFolder() == 2 && parentMenu.getIsFolder() != 1) {
            return "按钮必须放在功能下，不能放在功能夹或者按钮下";
        }

        return "success";
    }

    @Override
    public ServiceResult<String, List<Menu>> findRoleMenu() {
        ServiceResult<String, List<Menu>> result = new ServiceResult<>();
        User user = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        List<SysMenuDO> menuDOList = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();

        List<RoleDO> roleList = userRoleMapper.findRoleListByUserId(user.getUserId());
        List<Integer> roleIdList = new ArrayList<>();
        if (roleList != null && roleList.size() > 0) {
            if (!roleService.isSuperAdmin(user.getUserId())) {
                for(RoleDO roleDO : roleList){
                    roleIdList.add(roleDO.getId());
                }
                maps.put("roleSet", roleIdList);
            }
            menuDOList = sysMenuMapper.findRoleMenu(maps);
        }

        /*if (userResponse.getRoleList() != null && userResponse.getRoleList().size() > 0) {
            if (!roleService.isSuperAdmin(userResponse.getUserId())) {
                maps.put("roleSet", userResponse.getRoleList());
            }
            menuDOList = menuMysqlDAO.findRoleMenu(maps);
        }*/

        /*if(UserType.USER_TYPE_ADMIN.equals(userResponse.getUserType())){
            Map<String, Object> maps = new HashMap<>();
            menuDOList = menuMysqlDAO.findAllMenu(maps);
        }else{
            List<Integer> userRoleList = userRoleMysqlDAO.findRoleIdListByUserId(userResponse.getUserId());
            if (userRoleList != null && userRoleList.size() != 0) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("roleSet", userRoleList);
                menuDOList = menuMysqlDAO.findAllMenu(maps);
            }
        }*/





        /* 实现方式一 */
        /*for(SysMenuDO node1 : menuDOList){
            boolean mark = false;
            for(SysMenuDO node2 : menuDOList){

                if(node1.getParentMenuId()!=null && node1.getParentMenuId().equals(node2.getMenuId())){
                    mark = true;
                    if(node2.getChildren() == null)
                        node2.setChildren(new ArrayList<SysMenuDO>());
                    node2.getChildren().add(node1);
                    if (node2.getButtonMaps() == null) {
                        if (node1.getMenuId().startsWith("B")) {
                            Map<String, SysMenuDO> map = new HashMap<>();
                            map.put(node1.getMenuId(), node1);
                            node2.setButtonMaps(map);
                        }

                    } else {
                        node2.getButtonMaps().put(node1.getMenuId(), node1);
                    }
                    break;
                }
            }
            if(!mark){
                nodeList.add(node1);
            }
        }*/


        /* 实现方式二 */
        List<SysMenuDO> nodeList = ConvertMenu.convertTree(menuDOList);

        List<Menu> resultList = new ArrayList<>();
        for (SysMenuDO node1 : nodeList) {
            resultList.add(ConvertMenu.convert(node1));
        }

        result.setResult(resultList);
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

    @Override
    public ServiceResult<String, List<Menu>> findAllMenu() {
        ServiceResult<String, List<Menu>> result = new ServiceResult<>();
        User user = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Map<String, Object> maps = new HashMap<>();
        List<SysMenuDO> menuDOList = sysMenuMapper.findRoleMenu(maps);


        /* 实现方式二 */
        List<SysMenuDO> nodeList = ConvertMenu.convertTree(menuDOList);

        List<Menu> resultList = new ArrayList<>();
        for (SysMenuDO node1 : nodeList) {
            resultList.add(ConvertMenu.convert(node1));
        }

        result.setResult(resultList);
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

    @Override
    public ServiceResult<String, Menu> findByCode(Integer menuId) {
        ServiceResult<String, Menu> result = new ServiceResult<>();

        SysMenuDO sysMenuDO = sysMenuMapper.findByMenuId(menuId);
        List<SysMenuDO> children = sysMenuMapper.findByParentId(sysMenuDO.getId());

        sysMenuDO.setChildren(children);

        result.setResult(ConvertMenu.convert(sysMenuDO));
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

    @Override
    public ServiceResult<String, List<Menu>> getHomeMenu() {
        ServiceResult<String, List<Menu>> result = new ServiceResult<>();
        List<Menu> resultList = new ArrayList<>();

        Menu menu = new Menu();
        menu.setMenuId(200026);
        menu.setMenuName("业主列表");
        menu.setMenuUrl("/ownerList");
        resultList.add(menu);

        menu = new Menu();
        menu.setMenuId(200049);
        menu.setMenuName("我的收藏");
        menu.setMenuUrl("/userFavoriteList");
        resultList.add(menu);

        menu = new Menu();
        menu.setMenuId(200047);
        menu.setMenuName("组合商品");
        menu.setMenuUrl("/groupedProductList?packetType=1");
        resultList.add(menu);

        menu = new Menu();
        menu.setMenuId(200047);
        menu.setMenuName("样板间");
        menu.setMenuUrl("/groupedProductList?packetType=2");
        resultList.add(menu);

        result.setResult(resultList);
        result.setErrorCode(ErrorCode.SUCCESS);

        return result;
    }

}
