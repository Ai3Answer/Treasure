package com.feicuiedu.hunttreasure.treasure.hide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.treasure.TreasureRepo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HideTreasureActivity extends AppCompatActivity implements HideTreasureView{

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_LOCATION = "key_location";
    private static final String KEY_LATLNG = "key_latlng";
    private static final String KEY_ALTIYUDE = "key_altitude";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    private ProgressDialog mDialog;

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
    }

    // 对外提供一个跳转的方法
    public static void open(Context context, String title, String address, LatLng latLng, double altitude) {
        Intent intent = new Intent(context, HideTreasureActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_LOCATION, address);
        intent.putExtra(KEY_LATLNG, latLng);
        intent.putExtra(KEY_ALTIYUDE, altitude);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mActivityUtils = new ActivityUtils(this);

        ButterKnife.bind(this);

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    // 处理toolbar的返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 点击的时候上传宝藏到服务器
    @OnClick(R.id.hide_send)
    public void onClick() {

        // 网络请求的数据上传
        HideTreasure hideTreasure = new HideTreasure();

        new HideTreasurePresenter(this).hideTreasure(hideTreasure);

    }

    //---------------视图接口里面的方法--------------
    @Override
    public void showProgress() {
        mDialog = ProgressDialog.show(this, "宝藏上传", "宝藏正在上传中，不要着急~");
    }

    @Override
    public void hideProgress() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void navigationToHome() {
        finish();

        // 清除缓存：为了回到之前页面重新请求数据，而不是再从缓存中取
        TreasureRepo.getInstance().clear();
    }
}
