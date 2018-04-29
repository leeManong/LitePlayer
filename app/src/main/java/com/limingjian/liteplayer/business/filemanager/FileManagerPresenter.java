package com.limingjian.liteplayer.business.filemanager;

import com.limingjian.liteplayer.base.BasePresenterImpl;
import com.limingjian.liteplayer.utils.GetFilesUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FileManagerPresenter extends BasePresenterImpl<FileManagerContract.View> implements FileManagerContract.Presenter {


    @Override
    public void loadFiles(final String stringPath) {
        //List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(stringPath);

        /*Disposable disposable = Observable.create(new ObservableOnSubscribe<List<Map<String, Object>>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Map<String, Object>>> e) throws Exception {
                List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(stringPath);
                e.onNext(list);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Map<String, Object>>>() {
                    @Override
                    public void accept(List<Map<String, Object>> maps) throws Exception {

                    }
                });
        addDisposable(disposable);*/


        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(stringPath);
                getView().displayFiles(list);
            }
        });

    }
}
