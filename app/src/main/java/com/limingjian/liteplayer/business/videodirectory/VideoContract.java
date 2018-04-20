package com.limingjian.liteplayer.business.videodirectory;

import android.graphics.Bitmap;

import com.limingjian.liteplayer.bean.VideoBean;
import com.limingjian.liteplayer.bean.VideoDirectory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lmj on 2018/4/11.
 */
public interface VideoContract {

    interface View{

        void showLoading();

        void hideLoading();

        void showLoadingError();

        void displayVideos(List<VideoBean> videoBeans);

        void displayDirectory(HashMap<String,VideoDirectory> videoDirectoryHashMap);


    }

    interface Presenter{

        void loadVideo();

        void loadDirectory();

    }
}
