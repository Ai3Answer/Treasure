package com.feicuiedu.hunttreasure.treasure.hide;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.R;

public class HideTreasureActivity extends AppCompatActivity {

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_LOCATION = "key_location";
    private static final String KEY_LATLNG = "key_latlng";
    private static final String KEY_ALTIYUDE = "key_altitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
    }

    // 对外提供一个跳转的方法
    public static void open(Context context, String title, String address, LatLng latLng,double altitude){
        Intent intent = new Intent(context,HideTreasureActivity.class);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_LOCATION,address);
        intent.putExtra(KEY_LATLNG,latLng);
        intent.putExtra(KEY_ALTIYUDE,altitude);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();



    }
}
