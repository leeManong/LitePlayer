package com.limingjian.liteplayer.utils;

import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.bean.MediaBean;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmj on 2018/4/29.
 */
public class MediaHistoryManager {

    //从数据库中删除历史记录
    public static int clearMediaHistory() {
        return App.sLiteOrm.deleteAll(MediaBean.class);
    }

    //从数据库中获取历史记录
    public static List<MediaBean> getMediaHistory() {
        return App.sLiteOrm.query(MediaBean.class);
    }

    //插入一条历史记录数据
    public static void insertMedia(MediaBean newMediaBean) {
        ArrayList<MediaBean> mediaBean = App.sLiteOrm.query(new QueryBuilder<MediaBean>(MediaBean.class)
        .whereEquals("name" ,newMediaBean.getName()));
        if (mediaBean == null || mediaBean.size() == 0) {
            App.sLiteOrm.insert(newMediaBean);
        }
    }
}
