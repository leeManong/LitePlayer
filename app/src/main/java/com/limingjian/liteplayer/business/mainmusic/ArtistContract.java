package com.limingjian.liteplayer.business.mainmusic;

import com.limingjian.liteplayer.bean.Artist;

import java.util.List;

public interface ArtistContract {

    interface View{

        void showLoading();

        void hideLoading();

        void showEmptyView();

        void displayArtists(List<Artist> artistList);
    }

    interface Presenter{

        void loadArtists();
    }
}
