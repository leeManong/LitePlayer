package com.limingjian.liteplayer.business.videodirectory;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.base.BasePresenterImpl;
import com.limingjian.liteplayer.bean.VideoBean;
import com.limingjian.liteplayer.bean.VideoDirectory;
import com.limingjian.liteplayer.utils.RxCursorIterable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lmj on 2018/4/11.
 */
public class VideoPresenter extends BasePresenterImpl<VideoContract.View> implements VideoContract.Presenter {

    private static final String TAG = "VideoPresenter";

    private File rootFile = Environment.getExternalStorageDirectory();

    private List<VideoBean> mVideoBeans = new ArrayList<>();

    private HashMap<String, VideoDirectory> mVideoDirectories = new HashMap<>();

    @Override
    public void loadVideo() {

        mVideoBeans.clear();
        Cursor cursor = getCursor();
        final Disposable disposable = Observable.fromIterable(RxCursorIterable.from(cursor))
                .doAfterNext(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor cursor) throws Exception {
                        if (cursor.getPosition() == cursor.getCount() - 1) {
                            cursor.close();
                        }
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showLoading();
                    }
                })
                .subscribe(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor cursor) throws Exception {
                        String name = cursor.getString(0);
                        long duration = cursor.getLong(1);
                        long size = cursor.getLong(2);
                        String data = cursor.getString(3);
                        String height = cursor.getString(4);
                        String width = cursor.getString(5);
                        String thump = cursor.getString(6);
                        mVideoBeans.add(new VideoBean(name, duration, size, data, height, width, thump));

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: " + throwable.getMessage());
                        getView().showLoadingError();
                        getView().hideLoading();
                    }
                });
        addDisposable(disposable);
        getView().hideLoading();
        getView().displayVideos(mVideoBeans);

    }

    @Override
    public void loadDirectory() {
        for (VideoBean videoBean : mVideoBeans) {
            String key = getVideoDirectory(videoBean.getData());
            if (!mVideoDirectories.containsKey(key)) {
                List<VideoBean> videoBeans = new ArrayList<>();
                videoBeans.add(videoBean);
                VideoDirectory videoDirectory = new VideoDirectory();
                videoDirectory.setVideoBeans(videoBeans);
                videoDirectory.setName(key);
                mVideoDirectories.put(key, videoDirectory);
            } else {
                VideoDirectory videoDirectory = mVideoDirectories.get(key);
                videoDirectory.getVideoBeans().add(videoBean);
            }
        }
        getView().displayDirectory(mVideoDirectories);
    }


    private String getVideoDirectory(String path) {
        String temp = path.substring(0, path.lastIndexOf("/"));
        return temp.substring(temp.lastIndexOf("/") + 1);
    }

    private Cursor getCursor() {
        ContentResolver contentResolver = App.getAppContext().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] obj = {MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                MediaStore.Video.Media.DURATION,//时长
                MediaStore.Video.Media.SIZE,//大小
                MediaStore.Video.Media.DATA,//视频的绝对地址
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Thumbnails.DATA
        };
        return contentResolver.query(uri, obj, null, null, null);
    }

}
