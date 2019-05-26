package com.project.system.storemanagement.event;

import com.project.system.storemanagement.bean.GoodsBean;

public class ChangeSupplierEvent {
    private int position;
    private GoodsBean goodsBean;

    public ChangeSupplierEvent(int position, GoodsBean goodsBean) {
        this.position = position;
        this.goodsBean = goodsBean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public GoodsBean getGoodsBean() {
        return goodsBean;
    }

    public void setGoodsBean(GoodsBean goodsBean) {
        this.goodsBean = goodsBean;
    }
}
