package com.lxzl.erp.common.domain.system.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/10/31.
 * Time: 9:58.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu implements Serializable {

    @JsonProperty("menuId")
    private Integer menuId;

    @JsonProperty("menuName")
    private String menuName;

    @JsonProperty("parentMenuId")
    private Integer parentMenuId;

    @JsonProperty("menuOrder")
    private Integer menuOrder;

    @JsonProperty("isFolder")
    private Integer isFolder;

    @JsonProperty("menuUrl")
    private String menuUrl;

    @JsonProperty("menuIcon")
    private String menuIcon;

    @JsonProperty("dataStatus")
    private Integer dataStatus;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("isAvailable")
    private Integer isAvailable;

    private List<Menu> children;

//    private Map<Integer, ManagementMenu> buttonMaps;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

//    public Map<Integer, ManagementMenu> getButtonMaps() {
//        return buttonMaps;
//    }
//
//    public void setButtonMaps(Map<Integer, ManagementMenu> buttonMaps) {
//        this.buttonMaps = buttonMaps;
//    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }
}
