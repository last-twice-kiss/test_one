package com.example.myokhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit build;
    RetrofitTest retrofitTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button get1 = findViewById(R.id.get1);
        Button get2 = findViewById(R.id.get2);
        Button post1 = findViewById(R.id.post1);
        Button post2 = findViewById(R.id.post2);

        OkHttpClient client = new OkHttpClient();

        build = new Retrofit.Builder().baseUrl("https://www.httpbin.org/").build();
        retrofitTest = build.create(RetrofitTest.class);
        get1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        Request build = new Request.Builder().url("https://www.httpbin.org/get?a=1&b=2").build();
                        Call call = client.newCall(build);
                        try {
                            Response execute = call.execute();
                            Log.d("TAG", "onClick1: "+execute.body().string());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        get2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Request build = new Request.Builder().url("https://www.httpbin.org/get?a=1&b=2").build();
                        Call call = client.newCall(build);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            }
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()){
                                    Log.d("TAG", "onClick2: "+response.body().string());
                                }
                            }
                        });

            }
        });
        post1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        FormBody build1 = new FormBody.Builder().add("a", "1").add("b", "2").build();
                        RequestBody requestBody = RequestBody.create("[{\"id\":1,\"name\":\"zhao\"},{\"id\":2,\"name\":\"ding\"}]", MediaType.parse("application/json"));
                        Request build = new Request.Builder()
                                .url("https://www.httpbin.org/post")
                                .post(requestBody)
                                .build();
                        Call call = client.newCall(build);
                        try {
                            Response execute = call.execute();
                            Log.d("TAG", "onClick3: "+execute.body().string());
//                            parseJson(execute.body().string());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        post2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormBody build1 = new FormBody.Builder().add("a", "1").add("b", "2").build();

                Request build = new Request.Builder().url("https://www.httpbin.org/post").post(build1).build();
                Call call = client.newCall(build);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            Log.d("TAG", "onClick4: "+response.body().string());
                        }

                    }
                });

            }
        });
    }
    public void retrofitGet(View view) throws IOException {
        Retrofit build1 = new Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitTest retrofitTest1 = build1.create(RetrofitTest.class);
        retrofit2.Call<JsonRootBean> zhaojunyao = retrofitTest1.get("zhaojunyao", "123456");
        zhaojunyao.enqueue(new retrofit2.Callback<JsonRootBean>() {
            @Override
            public void onResponse(retrofit2.Call<JsonRootBean> call, retrofit2.Response<JsonRootBean> response) {
                Log.d("TAG", "onClick5: "+response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<JsonRootBean> call, Throwable t) {

            }
        });
        }
    private HashMap<String,List<Cookie>> cookies=new HashMap<>();
    public void retrofitlist(View view) {
        Retrofit build2 = new Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/")
                .callFactory(new OkHttpClient
                        .Builder()
                        .cookieJar(new CookieJar() {
                            @Override
                            public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
                                cookies.put(httpUrl.host(),list);
                            }

                            @NonNull
                            @Override
                            public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
                                List<Cookie> cookies=MainActivity.this.cookies.get(httpUrl.host());
                                return cookies==null?new ArrayList<>():cookies;
                            }
                        }).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        RetrofitTest retrofitTest2 = build2.create(RetrofitTest.class);


         retrofitTest2.get2("zhaojunyao", "123456")
                 .flatMap(new Function<JsonRootBean, Publisher<ResponseBody>>() {
                     @Override
                     public Publisher<ResponseBody> apply(JsonRootBean jsonRootBean) throws Throwable {
                         return retrofitTest2.get3(0);
                     }
                 })
                 .observeOn(Schedulers.io())
         .subscribeOn(AndroidSchedulers.mainThread())
         .subscribe(new Consumer<ResponseBody>() {
             @Override
             public void accept(ResponseBody responseBody) throws Throwable {
                 Log.d("TAG", "accept: "+responseBody.string());
             }
         });


    }
    public void retrofitPost(View view) {
        retrofit2.Call<ResponseBody> zhao = retrofitTest.post("zhao", "12345");
        zhao.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.d("TAG", "onClick6: "+response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void retrofitfile(View view) {
        Retrofit build3 = new Retrofit.Builder().baseUrl("https://www.httpbin.org/").build();
        RetrofitTest retrofitTest3 = build3.create(RetrofitTest.class);
        File file1 = new File("C:\\Users\\Lenovo\\Desktop\\101.txt");
        MultipartBody.Part file11 = MultipartBody.Part.createFormData("file1",
                "101.txt",
                RequestBody.create(file1, MediaType.parse("text/plain")));
        retrofit2.Call<ResponseBody> upload = retrofitTest3.upload(file11);
        upload.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.d("TAG", "retrofitfile: "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });


    }
}