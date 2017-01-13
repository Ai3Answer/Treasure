package com.feicuiedu.hunttreasure.net;

import android.text.style.UpdateAppearance;

import com.feicuiedu.hunttreasure.treasure.Area;
import com.feicuiedu.hunttreasure.treasure.Treasure;
import com.feicuiedu.hunttreasure.treasure.detail.TreasureDetail;
import com.feicuiedu.hunttreasure.treasure.detail.TreasureDetailResult;
import com.feicuiedu.hunttreasure.treasure.hide.HideTreasure;
import com.feicuiedu.hunttreasure.treasure.hide.HideTreasureResult;
import com.feicuiedu.hunttreasure.user.User;
import com.feicuiedu.hunttreasure.user.account.Update;
import com.feicuiedu.hunttreasure.user.account.UpdateResult;
import com.feicuiedu.hunttreasure.user.account.UploadResult;
import com.feicuiedu.hunttreasure.user.login.LoginResult;
import com.feicuiedu.hunttreasure.user.register.RegisterResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by gqq on 2017/1/9.
 */

// 请求构建的接口
public interface TreasureApi {

    // 登录的请求
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

    // 注册的请求
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    // 获取区域内的宝藏数据请求
    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasureInArea(@Body Area area);

    // 宝藏详情的请求
    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetail(@Body TreasureDetail treasureDetail);

    // 埋藏宝藏的请求
    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);

    /**
     * 关于头像上传的：文件
     * @param part
     * @return
     */
    // 两种方式
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upload(@Part("file\";filename=\"image.png\"") RequestBody body);

    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upload(@Part MultipartBody.Part part);

    // 更新头像
    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> update(@Body Update update);

}
