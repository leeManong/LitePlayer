package com.limingjian.liteplayer.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by lmj on 2018/3/27.
 */

public class BasePresenterImpl<V> implements IPresenter<V> {

    private Reference<V> mReference;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(V view) {
        mReference = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            synchronized (this) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
            }
        }
        mCompositeDisposable.add(disposable);
    }

    protected void dispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public V getView() {
        return mReference == null ? null : mReference.get();
    }

    @Override
    public boolean isViewAttached() {
        return mReference != null && mReference.get() != null;
    }
}
