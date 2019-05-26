package com.project.system.storemanagement.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class SearchBean {
    @JSONField(name = "name")
    private String name;

    public SearchBean(String name) {
        this.name = name;
    }
}
