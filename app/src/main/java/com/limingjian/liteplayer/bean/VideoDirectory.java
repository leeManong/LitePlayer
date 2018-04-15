package com.limingjian.liteplayer.bean;

import java.util.List;

/**
 * Created by lmj on 2018/4/11.
 */
public class VideoDirectory{

    private String name;

    private List<VideoBean> mVideoBeans;

    private boolean isLastWatched;

    private boolean isNew;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VideoBean> getVideoBeans() {
        return mVideoBeans;
    }

    public void setVideoBeans(List<VideoBean> videoBeans) {
        mVideoBeans = videoBeans;
    }


    public boolean isLastWatched() {
        return isLastWatched;
    }

    public void setLastWatched(boolean lastWatched) {
        isLastWatched = lastWatched;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return "VideoDirectory{" +
                "mVideoBeans=" + mVideoBeans +
                ", isLastWatched=" + isLastWatched +
                ", isNew=" + isNew +
                '}';
    }
}
