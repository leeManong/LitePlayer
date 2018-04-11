package com.limingjian.liteplayer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.limingjian.liteplayer.widget.AutoClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.main_edit_search)
    AutoClearEditText mMainEditSearch;
    @BindView(R.id.tb_main)
    Toolbar mTbMain;
    @BindView(R.id.fl_main_container)
    FrameLayout mFlMainContainer;
    @BindView(R.id.nav_main)
    NavigationView mNavMain;
    @BindView(R.id.dl_main_activity)
    DrawerLayout mDlMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        setSupportActionBar(mTbMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false); //设置返回键可用
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mNavMain.setItemIconTintList(null);//显示图标本来的颜色
        mTbMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(mDlMainActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(Gravity.START);
    }
    private void closeDrawer(DrawerLayout drawerLayout) {
        drawerLayout.closeDrawer(Gravity.START);
    }
}
