package com.limingjian.liteplayer.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.bean.VideoBean;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by lmj on 2018/4/11.
 */
public class VideoAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {

    public VideoAdapter(int layoutResId, List<VideoBean> videoBeans) {
        super(layoutResId, videoBeans);
    }

    @Override
    protected void convert(final BaseViewHolder helper, VideoBean item) {
        helper.setText(R.id.tv_video_name, item.getName());
        helper.setText(R.id.tv_video_size, item.getWidth() + "x" + item.getHeight());

        if (item.getLastWatcherTime() == 0) {
            helper.setText(R.id.tv_video_duration, TimeUtils.millis2String(item.getDuration(), new SimpleDateFormat("HH:mm:ss", Locale.CHINA)));
        } else {
            helper.setText(R.id.tv_video_duration,
                    TimeUtils.millis2String(item.getLastWatcherTime(), new SimpleDateFormat("HH:mm:ss", Locale.CHINA)) + "//"
                            + TimeUtils.millis2String(item.getDuration(), new SimpleDateFormat("HH:mm:ss", Locale.CHINA)));
        }

        final ImageView imageView = helper.getView(R.id.iv_video_image);

        Glide.with(App.getAppContext()).asBitmap().load(item.getThumbPath()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap bitmap = resource;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int xWidth = 110 * height / 62;
                int xHeight = xWidth * 9 / 16;
                int clipHeight = width / 3;
                if (height > width + width / 2) {
                    clipHeight = width * 9 / 16 + clipHeight;
                    bitmap = ImageUtils.clip(bitmap, 0, width / 3, width, clipHeight);
                    bitmap = ImageUtils.scale(bitmap, xWidth, xHeight);
                } else {

                    bitmap = ImageUtils.scale(bitmap, xWidth, xHeight);

                }

                Glide.with(App.getAppContext()).load(bitmap)
                        .apply(bitmapTransform(new RoundedCornersTransformation(25, 0))).into(imageView);

            }
        });
    }

}
