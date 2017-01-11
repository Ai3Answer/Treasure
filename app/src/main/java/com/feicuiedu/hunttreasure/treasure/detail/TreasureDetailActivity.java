package com.feicuiedu.hunttreasure.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.treasure.Treasure;

public class TreasureDetailActivity extends AppCompatActivity {

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

        // 拿到传递过来的数据
        mTreasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);

    }
}
