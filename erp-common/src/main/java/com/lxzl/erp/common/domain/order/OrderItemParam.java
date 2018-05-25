package com.lxzl.erp.common.domain.order;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\5\22 0022 10:48
 */
public class OrderItemParam {
    private Integer itemType;   //类型：1-商品项，2-配件项
    private Integer itemId;     //商品项/配件项ID
    private Integer itemCount;        //收货数量

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

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }
}
