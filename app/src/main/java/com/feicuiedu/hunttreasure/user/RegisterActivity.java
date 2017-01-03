package com.feicuiedu.hunttreasure.user;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.commons.RegexUtils;
import com.feicuiedu.hunttreasure.custom.AlertDialogFragment;
import com.feicuiedu.hunttreasure.treasure.HomeActivity;

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
    private String mUsername;
    private String mPassword;

    private ActivityUtils mActivityUtils;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 会触发onContentChanged方法
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

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
            mUsername = mEtUsername.getText().toString();
            mPassword = mEtPassword.getText().toString();
            String confirm = mEtConfirm.getText().toString();
            boolean canregister = !(TextUtils.isEmpty(mUsername)||
                    TextUtils.isEmpty(mPassword)||
                    TextUtils.isEmpty(confirm))
                    && mPassword.equals(confirm);
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
        // 注册的视图和业务处理
        if (RegexUtils.verifyUsername(mUsername)!=RegexUtils.VERIFY_SUCCESS){

            // 显示一个错误的对话框：自定义一个
            AlertDialogFragment.getInstances(
                    getString(R.string.username_error),
                    getString(R.string.username_rules))
                    .show(getSupportFragmentManager(),"usernameError");
            return;
        }

        if (RegexUtils.verifyPassword(mPassword)!=RegexUtils.VERIFY_SUCCESS){
            // 显示一个错误的对话框
            AlertDialogFragment.getInstances(
                    getString(R.string.password_error),
                    getString(R.string.password_rules))
                    .show(getSupportFragmentManager(),"passwordError");

            return;
        }
        // 进行注册的功能：模拟场景进行注册
        /**
         * 3个泛型：
         * 3. 1. 启动任务输入的参数类型：请求的地址、上传的数据等类型
         * 3. 2. 后台任务执行的进度：一般是Integer类型(int的包装类)
         * 3. 3. 后台返回的结果类型：比如String类型、Void等
         * 模拟注册，三个泛型都不需要的时候都可以设置成Void
         */
        new AsyncTask<Void, Integer, Void>() {

            // 可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // UI的处理：进度条的展示
                showProgress();
            }

            // 后台执行，比较耗时的操作都可以放在这里,后台线程，不可以做UI的更新
            @Override
            protected Void doInBackground(Void... params) {

                // 后台线程，做网络请求
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            // 相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // 拿到数据，做UI更新
                // 注册成功之后的处理
                hideProgress();
                showMessage("注册成功");
                navigationToHome();

            }
        }.execute();
    }

    // 跳转页面
    private void navigationToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();
    }

    // 显示信息
    private void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    // 隐藏进度
    private void hideProgress() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
    }

    // 显示进度
    private void showProgress() {
        mDialog = ProgressDialog.show(this, "注册", "亲，正在注册中，请稍后~");
    }
}
