package com.jasper.myandroidtest._library;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.jasper.myandroidtest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * <strong>Retrofit</strong><br/>
 * 一个网络请求框架。<br/>
 * 项目地址：https://github.com/square/retrofit<br/>
 * 详细介绍：http://square.github.io/retrofit/<br/>
 *
 * 下面的使用的API接口来自github，网址：https://developer.github.com/v3/
 */
public class RetrofitActivity extends Activity {
    @Bind(R.id.et_log) EditText etLog;

    private GithubService githubService;
    private GithubService githubErrorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        githubService = retrofit.create(GithubService.class);

        Retrofit retrofitError = new Retrofit.Builder()
                .baseUrl("http://zhongxianyao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        githubErrorService = retrofitError.create(GithubService.class);
    }

    @OnClick(R.id.btn_get_data)
    void getData() {
        Call<GithubUser> githubUserCall = githubService.getUser("JasperZXY");
        githubUserCall.enqueue(new Callback<GithubUser>() {
            @Override
            public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
                GithubUser user = response.body();
                etLog.append(String.format("\nid:%d, avatarUrl:%s, email:%s",
                        user.id, user.avatar_url, user.email));
            }

            @Override
            public void onFailure(Call<GithubUser> call, Throwable t) {
                etLog.append("\n异常:" + t.getLocalizedMessage());
            }
        });
    }

    @OnClick(R.id.btn_http_error)
    void httpError() {
        Call<String> call = githubErrorService.test("JasperZXY");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                etLog.append("\n居然成功了，response:" + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                etLog.append("\n异常:" + t.getLocalizedMessage());
            }
        });
    }

    @OnClick(R.id.btn_et_clear)
    void etClear() {
        etLog.setText(R.string.hello_world);
    }

    interface GithubService {
        @GET("/users/{name}")
        Call<GithubUser> getUser(@Path("name")String name);

        @GET("test")
        Call<String> test(@Query("param")String param);

        //post的写法，这里没有相应的例子
        @POST("users/new")
        Call<String> createUser(@Body GithubUser user);
    }

    static class GithubUser {
        public int id;
        public String avatar_url;
        public String email;
    }
}
