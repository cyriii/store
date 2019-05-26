package com.project.system.storemanagement.http;

import android.util.Log;

import com.project.system.storemanagement.utils.ZipHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2017/12/21.
 */

public class RequestIntercept implements Interceptor {

    private GlobalHttpHandler mHandler;


    public RequestIntercept(GlobalHttpHandler mHandler) {
        this.mHandler = mHandler;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request();
//        Request.Builder requestBuilder = request.newBuilder();

        Buffer requestbuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestbuffer);
        } else {
            LogUtils.e("request.body() == null");
        }

//        String method = request.method();
//        if ("POST".equals(method)) {
//            StringBuilder sb = new StringBuilder();
//            if (request.body() instanceof FormBody) {
//                FormBody body = (FormBody) request.body();
//                for (int i = 0; i < body.size(); i++) {
//                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
//                }
//                sb.delete(sb.length() - 1, sb.length());
//                LogUtils.e("post请求", "| RequestParams:{" + sb.toString() + "}");
//            }
//        }

        if (mHandler != null)//在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不做操作则返回参数中的request
            request = mHandler.onHttpRequestBefore(chain, request);

        LogUtils.e("intercept:url-->>" + request.url());
        LogUtils.e("intercept:method-->>" + request.method());
        LogUtils.e("intercept:headers-->>" + request.headers().toString());
        LogUtils.e("intercept:body-->>" + requestbuffer);
        LogUtils.e("intercept:tag-->>" + request.tag());

        long t1 = System.nanoTime();
        Response originalResponse = chain.proceed(request);
        long t2 = System.nanoTime();

        LogUtils.e("网络请求时间：", String.valueOf(((t2 - t1) / 1e6d) / 1000) + "秒");
        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        //获取content的压缩类型
        String encoding = originalResponse.headers().get("Content-Encoding");

        Buffer clone = buffer.clone();
        String bodyString;

        //解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            clone.writeTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            bodyString = ZipHelper.decompressForGzip(bytes);//解压
            outputStream.close();
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            clone.writeTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            bodyString = ZipHelper.decompressToStringForZlib(bytes);//解压
            outputStream.close();
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }

        LogUtils.e("请求结果: " + bodyString);

        // TODO: 2019/3/15
//        if (TextUtils.equals(BuildConfig.BUILD_TYPE, "debug") || TextUtils.equals(BuildConfig.BUILD_TYPE, "inner_test")) {
//            //检测数据请求的合法性
//            new StrawberryCheckHttp().checkResponseJson(BuildConfig.BASEURL, request.url(), request.headers(), requestbuffer.toString(), bodyString);
//
//        }
        if (mHandler != null)//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);

        return originalResponse;
    }

    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }
        return null;
    }
}
