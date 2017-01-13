package com.feicuiedu.hunttreasure.user.account;

import android.util.Log;

import com.feicuiedu.hunttreasure.net.NetClient;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gqq on 2017/1/13.
 */

// 头像处理的业务类
public class AccountPresenter {

    public void uploadPhoto(File file){

        // 构建上传的图片的“部分”
        MultipartBody.Part part = MultipartBody.Part.createFormData("file","photo.png", RequestBody.create(null,file));

        // 上传的请求
        Call<UploadResult> uploadResultCall = NetClient.getInstances().getTreasureApi().upload(part);
        uploadResultCall.enqueue(mResultCallback);
    }

    private Callback<UploadResult> mResultCallback = new Callback<UploadResult>() {

        // 请求成功
        @Override
        public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
            if (response.isSuccessful()){
                UploadResult body = response.body();
                if (body==null){
                    // 提示
                    return;
                }

                // 提示
                if (body.getCount()!=1){
                    return;
                }

                String photoUrl = body.getUrl();
                // 更新信息！！重新在个人信息上加载、保存到用户信息里面等
                Log.i("TAG","photo:"+photoUrl);
            }
        }

        // 请求失败
        @Override
        public void onFailure(Call<UploadResult> call, Throwable t) {
            // 提示

        }
    };
}
