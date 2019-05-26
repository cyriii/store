package com.project.system.storemanagement.http;

import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.bean.GoodsBean;
import com.project.system.storemanagement.bean.GoodsDataBean;
import com.project.system.storemanagement.bean.UserBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 用户注册
     *
     * @param map
     * @return
     */
    @POST("register")
    Observable<BaseBean> register(@Body Map<String, String> map);

    /**
     * 用户登录
     *
     * @return
     */
    @POST("login")
    Observable<BaseBean<UserBean>> login(@Query("username") String username, @Query("password") String password);

    /**
     * 修改密码
     *
     * @return
     */
    @POST("user/changepwd")
    Observable<BaseBean> changepwd(@Body Map<String, String> map);

    /**
     * 用户信息修改
     *
     * @param map
     * @return
     */
    @PUT("user")
    Observable<BaseBean> user(@Body Map<String, String> map);

    /**
     * 当前登录用户信息
     *
     * @return
     */
    @GET("user")
    Observable<BaseBean<UserBean>> getUser();

    /**
     * 商品信息新增
     *
     * @return
     */
    @POST("good")
    Observable<BaseBean> good(@Body Map<String, String> map);

    /**
     * 商品信息修改
     *
     * @return
     */
    @PUT("good")
    Observable<BaseBean> changeGoods(@Body Map<String, String> map);


    /**
     * 商品信息删除
     *
     * @return
     */
    @DELETE("{url}")
    Observable<BaseBean> delete(@Path(value = "url", encoded = true) String url);

    /**
     * 商品列表
     *
     * @return
     */
    @POST("goods")
    Observable<BaseBean<GoodsDataBean>> goods(@Body Map<String, Object> map);

    /**
     * 供货商新增
     *
     * @return
     */
    @POST("supplier")
    Observable<BaseBean> supplier(@Body Map<String, String> map);

    /**
     * 供货商修改信息修改
     *
     * @return
     */
    @PUT("supplier")
    Observable<BaseBean> changeSupplier(@Body Map<String, String> map);

    /**
     * 商品列表
     *
     * @return
     */
    @POST("suppliers")
    Observable<BaseBean<GoodsDataBean>> suppliers(@Body Map<String, Object> map);


    /**
     * 客户新增
     *
     * @return
     */
    @POST("customer")
    Observable<BaseBean> customer(@Body Map<String, String> map);

    /**
     * 客户信息修改
     *
     * @return
     */
    @PUT("customer")
    Observable<BaseBean> changeCustomer(@Body Map<String, String> map);

    /**
     * 客户列表
     *
     * @return
     */
    @POST("customers")
    Observable<BaseBean<GoodsDataBean>> customers(@Body Map<String, Object> map);


    /**
     * 入库新增
     *
     * @return
     */
    @POST("store/in")
    Observable<BaseBean> storeIn(@Body Map<String, String> map);

    /**
     * 入库信息修改
     *
     * @return
     */
    @PUT("store/in")
    Observable<BaseBean> changeSoreIn(@Body Map<String, String> map);

    /**
     * 入库列表
     *
     * @return
     */
    @POST("store/ins")
    Observable<BaseBean<GoodsDataBean>> storeIns(@Body Map<String, Object> map);

    /**
     * 出库新增
     *
     * @return
     */
    @POST("store/out")
    Observable<BaseBean> storeOut(@Body Map<String, String> map);

    /**
     * 出库信息修改
     *
     * @return
     */
    @PUT("store/out")
    Observable<BaseBean> changeSoreOut(@Body Map<String, String> map);

    /**
     * 出库列表
     *
     * @return
     */
    @POST("store/outs")
    Observable<BaseBean<GoodsDataBean>> storeOuts(@Body Map<String, Object> map);

    /**
     * 库存信息列表
     *
     * @return
     */
    @POST("stores")
    Observable<BaseBean<GoodsDataBean>> stores(@Body Map<String, Object> map);


    /**
     * 出入库信息删除
     *
     * @return
     */
    @GET("{url}")
    Observable<BaseBean<GoodsBean>> query(@Path(value = "url", encoded = true) String url);

}
