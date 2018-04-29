package com.limingjian.liteplayer.business.mainmusic;

import com.limingjian.liteplayer.bean.Song;

import java.util.List;

public interface SongsContract {

    interface View{

        void showLoading();

        void hideLoading();

        void showEmptyView();

        void displaySongs(List<Song> songs);
    }

    interface Presenter{

        void loadSongs();
    }
}
