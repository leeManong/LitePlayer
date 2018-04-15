package com.limingjian.liteplayer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.bean.VideoDirectory;

import java.util.List;

/**
 * Created by lmj on 2018/4/11.
 */
public class VideoDirectoryAdapter extends BaseQuickAdapter<VideoDirectory,BaseViewHolder> {


    public VideoDirectoryAdapter(int layoutResId, List<VideoDirectory> videoDirectories) {
        super(layoutResId, videoDirectories);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDirectory item) {
        helper.setText(R.id.tv_directory_name, item.getName());
        helper.setText(R.id.tv_num_video, item.getVideoBeans().size() + " 视频");
        if (item.isLastWatched()) {
            helper.setTextColor(R.id.tv_directory_name, App.getAppContext().getResources().getColor(R.color.defaultColor));
            helper.setTextColor(R.id.tv_num_video,App.getAppContext().getResources().getColor(R.color.defaultColor));
        }
        if (item.isNew()) {
            helper.setVisible(R.id.iv_icon_new, true);
        }

    }





}
