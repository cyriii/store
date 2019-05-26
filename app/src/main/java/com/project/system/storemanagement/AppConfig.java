package com.project.system.storemanagement;

import android.text.TextUtils;

import com.project.system.storemanagement.bean.UserBean;

public class AppConfig {
    public static final String BASEURL = "http://39.105.159.120/";

    public static final String SUCCESS = "000000";

    public static String TOKEN = "";
    public static UserBean USERBAN;

    public static boolean isLogin() {
        return !TextUtils.isEmpty(TOKEN) ? true : false;
    }
}
