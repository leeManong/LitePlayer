package com.limingjian.liteplayer.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by lmj on 2018/3/27.
 */

public abstract class BaseFragment<V, P extends BasePresenterImpl<V>> extends Fragment {

    private P mPresenter;

    private V mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = bindPresenter();
        mView = bindView();
        if (mPresenter != null) {
            mPresenter.attachView(mView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.dispose();
        }
    }

    public abstract P bindPresenter();

    public abstract V bindView();

    protected P getBindPresenter() {
        return mPresenter;
    }

    protected V getBindView() {
        return mView;
    }
}
