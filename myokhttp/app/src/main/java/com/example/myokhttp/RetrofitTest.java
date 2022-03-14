package com.example.myokhttp;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitTest {
    @POST("post")
    @FormUrlEncoded
    Call<ResponseBody> post(@Field("userName") String username, @Field("passWord")String password);
    @POST("user/login")
    @FormUrlEncoded
    Call<JsonRootBean> get(@Field("username") String username,@Field("password") String password);
    @POST("user/login")
    @FormUrlEncoded
    Flowable<JsonRootBean> get2(@Field("username") String username, @Field("password") String password);
    @GET("lg/collect/list/{pageNum}/json")
    Flowable<ResponseBody> get3(@Path("pageNum") int pageNum);
    @POST("post")
    @Multipart
    Call<ResponseBody> upload(@Part MultipartBody.Part file);
}
