package com.limingjian.liteplayer.business.history;

import com.limingjian.liteplayer.bean.MediaBean;

import java.util.List;

/**
 * Created by lmj on 2018/4/29.
 */
public interface HistoryContract {

    interface View{

        void showLoading();

        void hideLoading();

        void displayHistory(List<MediaBean>mediaBeans);
    }

    interface Presenter{

        void loadHistory();
    }

}
