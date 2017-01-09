package com.feicuiedu.hunttreasure.net;

import com.feicuiedu.hunttreasure.user.User;
import com.feicuiedu.hunttreasure.user.login.LoginResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gqq on 2017/1/9.
 */

// 请求构建的接口
public interface TreasureApi {

    // 登录的请求
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

}
