package com.project.system.storemanagement;

import android.app.Application;

import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.GlobalHttpHandler;
import com.project.system.storemanagement.utils.FileUtil;
import com.project.system.storemanagement.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.project.system.storemanagement.AppConfig.TOKEN;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        initClient();
    }

    /**
     * 创建网络请求
     */
    private void initClient() {
        Client.builder()
                .baseUrl(AppConfig.BASEURL)
                .cacheFile(FileUtil.getCacheFile(this))
                .globalHttpHandler(getHandler())
                .interceptors(getInterceptors())
                .build();
    }

    private Interceptor[] getInterceptors() {
        return null;
    }

    private GlobalHttpHandler getHandler() {
        return new GlobalHttpHandler() {
            @Override
            public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //获取请求request
                Request build = null;
                try {
                    build = request.newBuilder()
                            .addHeader("Content-Type", "application/json;charset=utf-8")
                            .addHeader("token", TOKEN)
                            .url(URLDecoder.decode(request.url().url().toString(), "utf-8"))
                            .build();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return build;
            }
        };
    }
}
