package com.feicuiedu.hunttreasure.user.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.custom.IconSelectWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private IconSelectWindow mSelectWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.iv_userIcon)
    public void onClick() {
        // 点击头像会弹出一个选择的PopupWindow
        if (mSelectWindow==null) {
            mSelectWindow = new IconSelectWindow(this,listener);
        }
        if (mSelectWindow.isShowing()){
            mSelectWindow.dismiss();
            return;
        }
        mSelectWindow.show();
    }

    private IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {
        @Override
        public void toGallery() {
            Toast.makeText(AccountActivity.this, "到相册", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void toCamera() {
            Toast.makeText(AccountActivity.this, "到相机", Toast.LENGTH_SHORT).show();
        }
    };
}
