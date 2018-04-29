package com.limingjian.liteplayer.adapter;

import android.content.ContentUris;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.GlideApp;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.bean.Song;

import java.util.List;

public class SongListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    public SongListAdapter(int layoutResId, List<Song> songs) {
        super(layoutResId, songs);
    }

    @Override
    protected void convert(BaseViewHolder helper, Song item) {

        String uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), item.albumId).toString();
        GlideApp.with(App.getAppContext()).load(uri)
                .placeholder(R.mipmap.icon_album_default)
                .into((ImageView) helper.getView(R.id.image));

        helper.setText(R.id.text_item_title, item.title);
        helper.setText(R.id.text_item_subtitle, item.artistName);
        helper.setText(R.id.text_item_subtitle_2, item.albumName);

        helper.getView(R.id.popup_menu).setOnClickListener(getOnClickListener());
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(App.getAppContext(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_song_goto_album:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup_song);
                popupMenu.show();
            }
        };
    }
}
