package com.limingjian.liteplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.FragmentUtils;
import com.limingjian.liteplayer.business.filemanager.FileManagerFragment;
import com.limingjian.liteplayer.business.videodirectory.VideoDirectoryFragment;
import com.limingjian.liteplayer.callback.OnBackPressedCallBack;
import com.limingjian.liteplayer.callback.OnSetToolBarTitleCallBack;
import com.limingjian.liteplayer.utils.ActivityUtils;
import com.limingjian.liteplayer.utils.GetFilesUtils;
import com.limingjian.liteplayer.widget.AutoClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;

public class MainActivity extends AppCompatActivity implements OnSetToolBarTitleCallBack {

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

    private VideoDirectoryFragment mVideoDirectoryFragment;

    private FileManagerFragment mFileManagerFragment;

    private static OnBackPressedCallBack mOnBackPressedCallBack;

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
        mNavMain.setCheckedItem(R.id.nav_video);
        mNavMain.setNavigationItemSelectedListener(getNavigationItemSelectedListener());

        if (mVideoDirectoryFragment == null) {
            mVideoDirectoryFragment = new VideoDirectoryFragment();
        }
        FragmentUtils.add(getSupportFragmentManager(), mVideoDirectoryFragment, R.id.fl_main_container);

    }

    private NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_video:
                        goVideo();
                        return true;
                    case R.id.nav_directory:
                        return goDirectory();
                }
                return false;
            }
        };
    }

    private boolean goDirectory() {
        closeDrawer(mDlMainActivity);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (mFileManagerFragment == null) {
            mFileManagerFragment = FileManagerFragment.newInstance(GetFilesUtils.getInstance().getBasePath(), "内部存储");
            for (int i = 0; i < count; i++) {
                getSupportFragmentManager().popBackStack();
            }
        }

        //FragmentUtils.add(getSupportFragmentManager(), mFileManagerFragment, R.id.fl_main_container, false);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mFileManagerFragment,R.id.fl_main_container,false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
        }
    }

    private void goVideo() {
        closeDrawer(mDlMainActivity);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        //清空回退栈
        for (int i = 0; i < count; i++) {
            getSupportFragmentManager().popBackStack();
        }

        /*if (mVideoDirectoryFragment != null && mVideoDirectoryFragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(mVideoDirectoryFragment);
            transaction.commit();
            mVideoDirectoryFragment = null;
        }*/

        if (mFileManagerFragment != null && mFileManagerFragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(mFileManagerFragment);
            transaction.commit();
            mFileManagerFragment = null;
        }
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

    @Override
    public void setToolBarTitle(String title) {
        mTbMain.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedCallBack != null) {
            mOnBackPressedCallBack.activityOnBackPressed();
        }
        if (JZVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    public static void setOnBackPressedCallBack(OnBackPressedCallBack onBackPressedCallBack) {
        mOnBackPressedCallBack = onBackPressedCallBack;
    }
}
