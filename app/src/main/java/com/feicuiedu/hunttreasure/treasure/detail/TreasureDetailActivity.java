package com.feicuiedu.hunttreasure.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.custom.TreasureView;
import com.feicuiedu.hunttreasure.treasure.Treasure;

import butterknife.BindView;
import butterknife.ButterKnife;

// 宝藏的详情页
public class TreasureDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mTreasureView;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetail;

    private static final String KEY_TREASURE = "key_treasure";
    private Treasure mTreasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
    }

    /**
     * 对外提供一个跳转到本页面的方法：
     * 1. 规范一下传递的数据：需要什么数据就必须传入
     * 2. Key简练
     */
    public static void open(Context context, Treasure treasure){
        Intent intent = new Intent(context,TreasureDetailActivity.class);
        intent.putExtra(KEY_TREASURE,treasure);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ButterKnife.bind(this);

        // 拿到传递过来的数据
        mTreasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(mTreasure.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 地图和宝藏的展示
        initMapView();

        // 宝藏卡片的视图展示
        mTreasureView.bindTreasure(mTreasure);
    }

    // 地图和宝藏的展示
    private void initMapView() {

        LatLng latLng = new LatLng(mTreasure.getLatitude(),mTreasure.getLongitude());

        // 地图的状态
        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)
                .overlook(-20)
                .zoom(18)
                .rotate(0)
                .build();

        // 设置地图什么操作都不能做，只是展示
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)
                .compassEnabled(false)
                .scrollGesturesEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false)
                .scaleControlEnabled(false)
                .rotateGesturesEnabled(false);

        MapView mapView = new MapView(this,options);

        // 填充到布局上
        mFrameLayout.addView(mapView);

        // 拿到地图的操作类
        BaiduMap map = mapView.getMap();

        // 添加地图的覆盖物
        BitmapDescriptor dot_expand = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MarkerOptions marker = new MarkerOptions()
                .position(latLng)
                .icon(dot_expand)
                .anchor(0.5f,0.5f);
        map.addOverlay(marker);

    }

    // 处理toolbar上面的返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
