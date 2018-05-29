package com.lxzl.erp.core.service.filterData;


import com.lxzl.erp.core.service.filterData.impl.support.FilterDataSupport;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/27
 * @Time : Created in 19:42
 */
public class MatchModel {
    private Double percent;
    private String name;

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MatchModel(Double percent, String name) {
        this.percent = percent;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MatchModel)){
            return false;
        }
        double likePercent = getLikePercent((MatchModel)o,this);
        if(likePercent>this.percent){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = getPercent() != null ? getPercent().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    private double getLikePercent(MatchModel a , MatchModel b){
        //获取两个对象的name的相似百分比
        return FilterDataSupport.similarDegree(a,b);
    }


}
