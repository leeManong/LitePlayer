package com.limingjian.liteplayer.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.bean.FileBean;
import com.limingjian.liteplayer.utils.GetFilesUtils;

import java.util.List;

public class FileAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {

    public FileAdapter(int layoutResId, List<FileBean> fileBeans) {
        super(layoutResId, fileBeans);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        helper.setText(R.id.tv_file_name, item.getName());
        String numFile;
        if (item.isDir()) {
            if (item.getNumMedia() == 0 && item.getNumSonFolder() == 0) {
                numFile = "空文件夹";
            } else if (item.getNumMedia() == 0 && item.getNumSonFolder() != 0) {
                numFile = item.getNumSonFolder() + "个子文件夹";
            } else if (item.getNumMedia() != 0 && item.getNumSonFolder() == 0) {
                numFile = item.getNumMedia() + "个媒体文件";
            } else {
                numFile = item.getNumSonFolder() + "个子文件夹，" + item.getNumMedia() + "个媒体文件";
            }
            helper.setText(R.id.tv_num_son_file, numFile);
        } else {
            helper.setText(R.id.tv_num_son_file, GetFilesUtils.getFileSize(item.getPath()));
        }

        ImageView imageView = helper.getView(R.id.iv_file_image);
        String type = item.getType();
        switch (type) {
            case "VIDEO":
                //Glide.with(App.getAppContext()).load(R.mipmap.ic_videos).into(imageView);
                imageView.setImageResource(R.mipmap.ic_videos);
                break;
            case "AUDIO":
                //Glide.with(App.getAppContext()).load(R.mipmap.ic_audios).into(imageView);
                imageView.setImageResource(R.mipmap.ic_audios);
                break;
            case "OTHER":
                //Glide.with(App.getAppContext()).load(R.mipmap.ic_unknow).into(imageView);
                imageView.setImageResource(R.mipmap.ic_unknow);
                break;
            default:
                //Glide.with(App.getAppContext()).load(R.mipmap.ic_video_directory).into(imageView);
                imageView.setImageResource(R.mipmap.ic_video_directory);
        }



    }
}
