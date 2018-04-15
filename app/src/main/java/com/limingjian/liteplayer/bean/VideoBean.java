package com.limingjian.liteplayer.bean;

import android.graphics.Bitmap;

/**
 * Created by lmj on 2018/4/11.
 */
public class VideoBean{

    private String name;
    private long duration;
    private long lastWatcherTime;
    private long size;
    private String data;//绝对地址
    private String height;
    private String width;
    private String thumbPath;
    private String directoryName;
    private Bitmap mBitmap;

    public VideoBean(String name, long duration, long size, String data, String height, String width,String thump) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
        this.height = height;
        this.width = width;
        thumbPath = thump;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public long getLastWatcherTime() {
        return lastWatcherTime;
    }

    public void setLastWatcherTime(long lastWatcherTime) {
        this.lastWatcherTime = lastWatcherTime;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                '}';
    }
}
