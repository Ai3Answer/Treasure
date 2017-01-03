package com.feicuiedu.hunttreasure.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.hunttreasure.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.et_Confirm)
    EditText mEtConfirm;
    @BindView(R.id.btn_Register)
    Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        // toolbar的展示和返回箭头的监听
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){

            // 激活左上角的返回图标(内部使用选项菜单处理)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 设置title
            getSupportActionBar().setTitle(R.string.register);
        }

        // EditText 的输入监听，监听文本的变化
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
        mEtConfirm.addTextChangedListener(textWatcher);
    }

    // 文本输入监听
    private TextWatcher textWatcher = new TextWatcher() {

        // 文本变化前
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        // 文本输入变化
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        // 文本输入之后，在这里面处理按钮的点击
        @Override
        public void afterTextChanged(Editable s) {
            // 处理文本输入之后的按钮事件
            String username = mEtUsername.getText().toString();
            String password = mEtPassword.getText().toString();
            String confirm = mEtConfirm.getText().toString();
            boolean canregister = !(TextUtils.isEmpty(username)||
                    TextUtils.isEmpty(password)||
                    TextUtils.isEmpty(confirm))
                    &&password.equals(confirm);
            mBtnRegister.setEnabled(canregister);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 处理ActionBar的返回箭头事件
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Register)
    public void onClick() {
    }
}
