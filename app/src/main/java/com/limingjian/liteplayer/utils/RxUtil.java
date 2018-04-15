package com.limingjian.liteplayer.utils;



import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * Created by lmj on 2018/4/11.
 */
public class RxUtil {

    /**
     * rxjava递归查询内存中的视频文件
     * @param f
     * @return
     */
    public static Observable<File> listFiles(final File f){
        if(f.isDirectory()){
            return Observable.just(f.listFiles()).flatMap(new Function<File[], ObservableSource<File>>() {
                @Override
                public ObservableSource<File> apply(File[] files) throws Exception {
                    return listFiles(f);
                }
            });
        } else {
            /**filter操作符过滤视频文件,是视频文件就通知观察者**/
            return Observable.just(f).filter(new Predicate<File>() {
                @Override
                public boolean test(File file) throws Exception {
                    return f.exists() && f.canRead() && FileUtils.isVideo(f);
                }
            });
        }
    }


}
