package com.feicuiedu.hunttreasure.treasure;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.feicuiedu.hunttreasure.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // toolbar的处理
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // drawerLayout设置监听
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();// 同步状态
        mDrawerLayout.addDrawerListener(toggle);

        // 设置Navigation每一条的选中监听
        mNavigationView.setNavigationItemSelectedListener(this);

        ImageView ivIcon = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.iv_usericon);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 更换头像
            }
        });
    }

    // Navigation每一条的选中监听
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_hide:
                break;
            case R.id.menu_logout:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 处理back返回键
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
