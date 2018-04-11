package com.limingjian.liteplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lmj on 2018/3/29.
 */

public abstract class BaseActivity<V, P extends BasePresenterImpl<V>> extends AppCompatActivity {

    private P mPresenter;

    private V mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mView = createView();
        if (mPresenter != null) {
            mPresenter.attachView(mView);
        }

    }

    public abstract P createPresenter();

    public abstract V createView();

    protected P getPresenter(){
        return mPresenter;
    }

    protected V getView() {
        return mView;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter.dispose();
        }
        super.onDestroy();

    }
}
