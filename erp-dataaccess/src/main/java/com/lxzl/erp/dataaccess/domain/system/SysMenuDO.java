package com.lxzl.erp.dataaccess.domain.system;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/10/31.
 * Time: 9:58.
 */
public class SysMenuDO extends BaseDO {

    private Integer id;
    private String menuName;
    private Integer parentMenuId;
    private Integer menuOrder;
    private Integer isFolder;
    private String menuUrl;
    private String menuIcon;
    private Integer dataStatus;
    private Integer level;
    private String remark;
    private Integer isAvailable;
    private List<SysMenuDO> children;
    private Map<Integer, SysMenuDO> buttonMaps;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Integer parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Integer getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Integer isFolder) {
        this.isFolder = isFolder;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SysMenuDO> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuDO> children) {
        this.children = children;
    }

    public Map<Integer, SysMenuDO> getButtonMaps() {
        return buttonMaps;
    }

    public void setButtonMaps(Map<Integer, SysMenuDO> buttonMaps) {
        this.buttonMaps = buttonMaps;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }
}
