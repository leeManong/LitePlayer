package com.limingjian.liteplayer.base;

/**
 * Created by lmj on 2018/3/27.
 */

public interface IPresenter<V> {

    void attachView(V view);

    void detachView();

    V getView();

    boolean isViewAttached();
}
