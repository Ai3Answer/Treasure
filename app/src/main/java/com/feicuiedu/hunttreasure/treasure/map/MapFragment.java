package com.feicuiedu.hunttreasure.treasure.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gqq on 2017/1/4.
 */

// 宝藏页面：地图的展示和宝藏数据的展示
public class MapFragment extends Fragment {

    private static final int ACCESS_LOCATION = 100;
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
    private LatLng mCurrentLocation;
    private LatLng mCurrentStatus;
    private Marker mCurrentMarker;
    private MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);

//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_DENIED){
//            // 需要动态获取权限的
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_LOCATION);
//        }else {
//            // 不需要去动态获取权限
//        }

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

            // 如果没有拿到结果，重新请求：部分机型会失败
            if (bdLocation == null) {
                mLocationClient.requestLocation();
                return;
            }

            // 定位结果的经纬度
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();

            // 定位的经纬度的类
            mCurrentLocation = new LatLng(latitude, longitude);
            String currentAddr = bdLocation.getAddrStr();

            Log.i("TAG", "定位的位置：" + currentAddr + "，经纬度：" + latitude + "," + longitude);

            // 设置定位图层展示的数据
            MyLocationData data = new MyLocationData.Builder()

                    // 定位数据展示的经纬度
                    .latitude(latitude)
                    .longitude(longitude)
                    .accuracy(100f)// 定位精度的大小
                    .build();

            // 定位数据展示到地图上
            mBaiduMap.setMyLocationData(data);

            // 移动到定位的地方，在地图上展示定位的信息：位置

            moveToLocation();


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
        mMapView = new MapView(getContext(), options);

        // 在布局上添加地图控件：0，代表第一位
        mMapFrame.addView(mMapView, 0);

        // 拿到地图的操作类(控制器：操作地图等都是使用这个)
        mBaiduMap = mMapView.getMap();

        // 设置地图状态的监听
        mBaiduMap.setOnMapStatusChangeListener(mStatusChangeListener);

        // 设置地图上标注物的点击监听
        mBaiduMap.setOnMarkerClickListener(mMarkerClickListener);
    }

    // 标注物的点击监听
    private BaiduMap.OnMarkerClickListener mMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (mCurrentMarker != null) {
                if (mCurrentMarker != marker) {
                    mCurrentMarker.setVisible(true);
                }
                mCurrentMarker.setVisible(true);
            }
            mCurrentMarker = marker;

            // 点击Marker展示InfoWindow，当前的覆盖物不可见
            mCurrentMarker.setVisible(false);

            // 创建一个InfoWindow
            InfoWindow infoWindow = new InfoWindow(dot_expand, marker.getPosition(), 0, new InfoWindow.OnInfoWindowClickListener() {

                // InfoWindow的监听
                @Override
                public void onInfoWindowClick() {
                    if (mCurrentMarker != null) {
                        mCurrentMarker.setVisible(true);
                    }
                    // 隐藏InfoWindow
                    mBaiduMap.hideInfoWindow();
                }
            });
            // 地图上显示一个InfoWindow
            mBaiduMap.showInfoWindow(infoWindow);

            return false;
        }
    };

    // 地图状态的监听
    private BaiduMap.OnMapStatusChangeListener mStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

        // 变化前
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        // 变化中
        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        // 变化结束后
        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {

            LatLng target = mapStatus.target;

            // 确实地图的状态发生变化了
            if (target != MapFragment.this.mCurrentStatus) {

                // TODO: 2017/1/5 会有数据的请求

                // 学习添加覆盖物的功能
                addMarker(target);

                MapFragment.this.mCurrentStatus = target;
            }
        }
    };

    // 卫星视图和普通视图的切换
    @OnClick(R.id.tv_satellite)
    public void switchMapType() {
        int mapType = mBaiduMap.getMapType();// 获取当前的地图类型
        // 切换类型
        mapType = (mapType == BaiduMap.MAP_TYPE_NORMAL) ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        // 卫星和普通的文字的显示
        String msg = mapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通";
        mBaiduMap.setMapType(mapType);
        mTvSatellite.setText(msg);
    }

    // 指南针
    @OnClick(R.id.tv_compass)
    public void switchCompass() {
        // 指南针有没有显示:指南针是地图上的一个图标
        boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
        mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
    }

    // 地图的缩放
    @OnClick({R.id.iv_scaleDown, R.id.iv_scaleUp})
    public void scaleMap(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleDown:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.iv_scaleUp:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
        }
    }

    // 定位的按钮：移动到定位的地方
    @OnClick(R.id.tv_located)
    public void moveToLocation() {

        // 地图状态的设置：设置到定位的地方
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mCurrentLocation)// 定位的位置
                .rotate(0)
                .overlook(0)
                .zoom(19)
                .build();
        // 更新状态
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        // 更新展示的地图的状态
        mBaiduMap.animateMapStatus(update);
    }


    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
    private BitmapDescriptor dot_expand = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);

    // 添加覆盖物
    private void addMarker(LatLng latLng) {

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);// 覆盖物的位置
        options.icon(dot);// 覆盖物的图标
        options.anchor(0.5f, 0.5f);// 锚点位置：居中

        // 添加覆盖物
        mBaiduMap.addOverlay(options);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case ACCESS_LOCATION:
//                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    // 获取到，做相应的处理
//                    mLocationClient.requestLocation();
//                }else {
//
//                }
//                break;
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
