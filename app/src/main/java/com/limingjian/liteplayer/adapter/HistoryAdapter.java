package com.limingjian.liteplayer.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.bean.MediaBean;

import java.util.List;

/**
 * Created by lmj on 2018/4/29.
 */
public class HistoryAdapter extends BaseQuickAdapter<MediaBean,BaseViewHolder> {

    public HistoryAdapter(int resId, List<MediaBean> mediaBeans) {
        super(resId, mediaBeans);
    }
    @Override
    protected void convert(BaseViewHolder helper, MediaBean item) {
        if (item.isVideo()) {
            Glide.with(App.getAppContext()).load(R.mipmap.ic_videos).into((ImageView) helper.getView(R.id.image));
        } else {
            Glide.with(App.getAppContext()).load(R.mipmap.ic_audios).into((ImageView) helper.getView(R.id.image));
        }

        helper.setText(R.id.tv_media_name, item.getName());
    }
}
