package com.lxzl.erp.core.service.system.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.system.pojo.Menu;
import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/11/1.
 * Time: 14:44.
 */
public class ConvertMenu {

    public static Menu convert(SysMenuDO menuDO) {
        Menu menu = new Menu();
        if (menuDO.getId() != null) {
            menu.setMenuId(menuDO.getId());
        }
        if (menuDO.getMenuName() != null) {
            menu.setMenuName(menuDO.getMenuName());
        }
        if (menuDO.getParentMenuId() != null) {
            menu.setParentMenuId(menuDO.getParentMenuId());
        }
        if (menuDO.getMenuOrder() != null) {
            menu.setMenuOrder(menuDO.getMenuOrder());
        }
        if (menuDO.getIsFolder() != null) {
            menu.setIsFolder(menuDO.getIsFolder());
        }
        if (menuDO.getMenuUrl() != null) {
            menu.setMenuUrl(menuDO.getMenuUrl());
        }
        if (menuDO.getMenuIcon() != null) {
            menu.setMenuIcon(menuDO.getMenuIcon());
        }
        if (menuDO.getDataStatus() != null) {
            menu.setDataStatus(menuDO.getDataStatus());
        }
        if (menuDO.getRemark() != null) {
            menu.setRemark(menuDO.getRemark());
        }
        if (menuDO.getIsAvailable() != null) {
            menu.setIsAvailable(menuDO.getIsAvailable());
        }

        if (menuDO.getChildren() != null && menuDO.getChildren().size() > 0) {
            List<Menu> menuList = new ArrayList<>();
            for (SysMenuDO childrenMenu : menuDO.getChildren()) {
                menuList.add(convert(childrenMenu));
            }
            menu.setChildren(menuList);
        }
        if (menuDO.getButtonMaps() != null && menuDO.getButtonMaps().size() > 0) {
            Map<Integer, Menu> buttonMap = new HashMap<>();
            Set set = menuDO.getButtonMaps().keySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                Integer key = (Integer) iter.next();
                buttonMap.put(key, convert(menuDO.getButtonMaps().get(key)));
            }
//            menu.setButtonMaps(buttonMap);
        }

        return menu;

    }

    public static List<SysMenuDO> convertTree(List<SysMenuDO> menuDOList) {
        List<SysMenuDO> nodeList = new ArrayList<>();
        if (menuDOList != null) {
            for (SysMenuDO node1 : menuDOList) {
                if (node1.getParentMenuId().equals(CommonConstant.SUPER_MENU_ID)) {
                    nodeList.add(node1);
                }
                for (SysMenuDO t : menuDOList) {
                    if (t.getParentMenuId().equals(node1.getId())) {
                        if (node1.getChildren() == null) {
                            List<SysMenuDO> myChildren = new ArrayList<SysMenuDO>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }

                        if (node1.getButtonMaps() == null) {
                            if (t.getIsFolder() == 2) {
                                Map<Integer, SysMenuDO> map = new HashMap<>();
                                map.put(t.getId(), t);
                                node1.setButtonMaps(map);
                            }
                        } else {
                            node1.getButtonMaps().put(t.getId(), t);
                        }
                    }
                }
            }
        }

        return nodeList;
    }

    public static SysMenuDO reverseConvert(Menu menuDO) {
        SysMenuDO menu = new SysMenuDO();
        if (menuDO.getMenuId() != null) {
            menu.setId(menuDO.getMenuId());
        }
        if (menuDO.getMenuName() != null) {
            menu.setMenuName(menuDO.getMenuName());
        }
        if (menuDO.getParentMenuId() != null) {
            menu.setParentMenuId(menuDO.getParentMenuId());
        }
        if (menuDO.getMenuOrder() != null) {
            menu.setMenuOrder(menuDO.getMenuOrder());
        }
        if (menuDO.getIsFolder() != null) {
            menu.setIsFolder(menuDO.getIsFolder());
        }
        if (menuDO.getMenuUrl() != null) {
            menu.setMenuUrl(menuDO.getMenuUrl());
        }
        if (menuDO.getMenuIcon() != null) {
            menu.setMenuIcon(menuDO.getMenuIcon());
        }
        if (menuDO.getDataStatus() != null) {
            menu.setDataStatus(menuDO.getDataStatus());
        }
        if (menuDO.getRemark() != null) {
            menu.setRemark(menuDO.getRemark());
        }

        return menu;

    }

    public static Menu convert(SysMenuDO menuDO, Menu parentMenu) {
        Menu menu = new Menu();
        if (menuDO.getId() != null) {
            menu.setMenuId(menuDO.getId());
        }
        if (menuDO.getMenuName() != null) {
            menu.setMenuName(menuDO.getMenuName());
        }
        if (menuDO.getParentMenuId() != null) {
            menu.setParentMenuId(menuDO.getParentMenuId());
        }
        if (menuDO.getMenuOrder() != null) {
            menu.setMenuOrder(menuDO.getMenuOrder());
        }
        if (menuDO.getIsFolder() != null) {
            menu.setIsFolder(menuDO.getIsFolder());
        }
        if (menuDO.getMenuUrl() != null) {
            menu.setMenuUrl(menuDO.getMenuUrl());
        }
        if (menuDO.getMenuIcon() != null) {
            menu.setMenuIcon(menuDO.getMenuIcon());
        }
        if (menuDO.getDataStatus() != null) {
            menu.setDataStatus(menuDO.getDataStatus());
        }
        if (menuDO.getRemark() != null) {
            menu.setRemark(menuDO.getRemark());
        }
        if (menuDO.getIsAvailable() != null) {
            menu.setIsAvailable(menuDO.getIsAvailable());
        }

        if (menuDO.getChildren() != null && menuDO.getChildren().size() > 0) {
            List<Menu> menuList = new ArrayList<>();
            for (SysMenuDO childrenMenu : menuDO.getChildren()) {
                menuList.add(convert(childrenMenu, parentMenu));
            }
            menu.setChildren(menuList);
        }
        if (menuDO.getButtonMaps() != null && menuDO.getButtonMaps().size() > 0) {
            Map<Integer, Menu> buttonMap = new HashMap<>();
            Set set = menuDO.getButtonMaps().keySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                Integer key = (Integer) iter.next();
                buttonMap.put(key, convert(menuDO.getButtonMaps().get(key), parentMenu));
            }
//            menu.setButtonMaps(buttonMap);
        }

        return menu;

    }

}
