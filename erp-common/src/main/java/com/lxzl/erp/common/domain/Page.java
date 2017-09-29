package com.lxzl.erp.common.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
public class Page<T> {
    //总记录数
    protected int totalCount = 0;
    //每页条数
    protected int pageSize = 0;
    //总页数
    protected int pageCount = 0;
    //结果集
    protected List<T> itemList = null;

    //当前页数
    protected int currentPage = 1;

    public Page(){
        this.totalCount = 0;
        this.itemList = new ArrayList<T>();
    }

    public Page(List<T> itemList, int totalCount){
        this.itemList = itemList;
        this.totalCount = totalCount;
    }
    public Page(List<T> itemList, int totalCount, int currentPage ,int pageSize){
        this.itemList = itemList;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        int res = totalCount%pageSize;
        int inc = res==0?0:1;
        this.pageCount = totalCount/pageSize+inc;


    }
    public List<T> getItemList() {
        return itemList;
    }

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    //********************Other method*****************************
    public final static int getStartIndex(int totalCount, int currPage, int pageSize){
        if (totalCount > 0) {
            int count = countTotalPage(totalCount, pageSize);
            currPage = assignPageNo(currPage, count);
            return (currPage - 1) * pageSize;
        } else {
            return 0;
        }
    }
    public final static int countTotalPage(int totalCount, int pageSize){
        if (pageSize <= 0 || totalCount <= 0){
            return 0;
        }
        int count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }
    public final static int assignPageNo(int pageNo, int totalPage){
        if (totalPage <= 0){
            return 0;
        }
        if (pageNo > totalPage){
            pageNo = totalPage;
        }
        else if (pageNo <= 0){
            pageNo = 1;
        }
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
