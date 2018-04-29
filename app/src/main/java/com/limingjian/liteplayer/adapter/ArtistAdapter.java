package com.limingjian.liteplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.bean.Artist;

import java.util.List;

public class ArtistAdapter extends BaseQuickAdapter<Artist,BaseViewHolder> {

    public ArtistAdapter(int resId, List<Artist> artists) {
        super(resId, artists);
    }

    @Override
    protected void convert(BaseViewHolder helper, Artist item) {

    }


}
