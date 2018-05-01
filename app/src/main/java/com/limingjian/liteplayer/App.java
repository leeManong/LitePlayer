package com.limingjian.liteplayer;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

/**
 * Created by lmj on 2018/4/10.
 */
public class App extends Application {

    private static App mInstance;

    public static final String DB_NAME = "liteplayer.db";

    public static LiteOrm sLiteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        mInstance = this;
        initLiteOrm();
    }

    private void initLiteOrm() {
        DataBaseConfig config = new DataBaseConfig(this);
        config.debugged = BuildConfig.DEBUG;
        config.dbName = DB_NAME;
        config.dbVersion = 1;
        config.onUpdateListener = null;
        sLiteOrm = LiteOrm.newSingleInstance(config);
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public static App getInstance() {
        return mInstance;
    }
}
