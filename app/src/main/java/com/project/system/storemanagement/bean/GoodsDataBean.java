package com.project.system.storemanagement.bean;

import java.util.List;

public class GoodsDataBean {


    /**
     * records : [{"id":"e5a38f2b1daa4568a856e07201b9292a","name":"123","unit":"123","remark":"123123","userId":"a34e66238d9c46f58cea72a0e4b07233","createDate":"2019-05-26T04:14:47.000+0000"}]
     * total : 1
     * size : 10
     * current : 1
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<GoodsBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<GoodsBean> getRecords() {
        return records;
    }

    public void setRecords(List<GoodsBean> records) {
        this.records = records;
    }
}
