package com.feicuiedu.hunttreasure.treasure.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.feicuiedu.hunttreasure.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gqq on 2017/1/4.
 */

// 宝藏页面：地图的展示和宝藏数据的展示
public class MapFragment extends Fragment {

    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView mIvToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        // 初始化百度地图
        initMapView();

        // 初始化定位相关
        initLocation();

    }

    // 初始化定位相关
    private void initLocation() {

        // 前置：激活定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 第一步，初始化LocationClient类:LocationClient类必须在主线程中声明，需要Context类型的参数。
        mLocationClient = new LocationClient(getContext().getApplicationContext());

        // 第二步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开GPS
        option.setCoorType("bd09ll");// 设置百度坐标类型，默认gcj02，会有偏差，bd9ll百度地图坐标类型，将无偏差的展示到地图上
        option.setIsNeedAddress(true);// 需要地址信息
        mLocationClient.setLocOption(option);

        // 第三步，实现BDLocationListener接口
        mLocationClient.registerLocationListener(mBDLocationListener);

        // 第四步，开始定位
        mLocationClient.start();

    }

    // 定位监听
    private BDLocationListener mBDLocationListener = new BDLocationListener() {

        // 获取到定位结果
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            // 如果没有拿到结果，重新请求
            if (bdLocation==null){
                mLocationClient.requestLocation();
                return;
            }

            // 定位结果的经纬度
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();

            String currentAddr = bdLocation.getAddrStr();

            Log.i("TAG","定位的位置："+currentAddr+"，经纬度："+latitude+","+longitude);
        }
    };

    // 初始化百度地图
    private void initMapView() {

        // 设置地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(19)// 3--21：默认的是12
                .overlook(0)// 俯仰的角度
                .rotate(0)// 旋转的角度
                .build();

        // 设置百度地图的设置信息
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)
                .compassEnabled(true)// 是否显示指南针
                .zoomGesturesEnabled(true)// 是否允许缩放手势
                .scaleControlEnabled(false)// 不显示比例尺
                .zoomControlsEnabled(false)// 不显示缩放的控件
                ;

        // 创建
        MapView mapView = new MapView(getContext(), options);

        // 在布局上添加地图控件：0，代表第一位
        mMapFrame.addView(mapView, 0);

        // 拿到地图的操作类(控制器：操作地图等都是使用这个)
        mBaiduMap = mapView.getMap();
    }

    // 卫星视图和普通视图的切换
    @OnClick(R.id.tv_satellite)
    public void switchMapType(){
        int mapType = mBaiduMap.getMapType();// 获取当前的地图类型
        // 切换类型
        mapType = (mapType==BaiduMap.MAP_TYPE_NORMAL)?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;
        // 卫星和普通的文字的显示
        String msg  = mapType==BaiduMap.MAP_TYPE_NORMAL?"卫星":"普通";
        mBaiduMap.setMapType(mapType);
        mTvSatellite.setText(msg);
    }

    // 指南针
    @OnClick(R.id.tv_compass)
    public void switchCompass(){
        // 指南针有没有显示:指南针是地图上的一个图标
        boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
        mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
    }

    // 地图的缩放
    @OnClick({R.id.iv_scaleDown,R.id.iv_scaleUp})
    public void scaleMap(View view){
        switch (view.getId()){
            case R.id.iv_scaleDown:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.iv_scaleUp:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
        }
    }
}
