package com.lxzl.erp.common.domain.order;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\5\25 0025 14:59
 */
public class ChangeOrderItemParam {
    private Integer itemType;   //类型：1-商品项，2-配件项
    private Integer itemId;     //商品项/配件项ID
    private Integer returnCount;        //退货数量

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }
}
