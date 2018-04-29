package com.limingjian.liteplayer.business.mainmusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.base.BasePresenterImpl;
import com.limingjian.liteplayer.bean.Song;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SongsPresenter extends BasePresenterImpl<SongsContract.View> implements SongsContract.Presenter {

    @Override
    public void loadSongs() {
        Disposable disposable = getAllSongs(App.getAppContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showLoading();
                    }
                })
                .subscribe(new Consumer<List<Song>>() {
                    @Override
                    public void accept(List<Song> songs) throws Exception {
                        if (songs == null || songs.size() == 0) {
                            getView().showEmptyView();
                        } else {
                            getView().displaySongs(songs);
                            getView().hideLoading();
                        }
                    }
                });
        addDisposable(disposable);
    }

    public static Observable<List<Song>> getSongsForCursor(final Cursor cursor) {
        return Observable.create(new ObservableOnSubscribe<List<Song>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Song>> e) throws Exception {
                List<Song> arrayList = new ArrayList<>();
                if ((cursor != null) && (cursor.moveToFirst()))
                    do {
                        long id = cursor.getLong(0);
                        String title = cursor.getString(1);
                        String artist = cursor.getString(2);
                        String album = cursor.getString(3);
                        int duration = cursor.getInt(4);
                        int trackNumber = cursor.getInt(5);
                        long artistId = cursor.getInt(6);
                        long albumId = cursor.getLong(7);
                        String path = cursor.getString(8);

                        arrayList.add(new Song(id, albumId, artistId, title, artist, album, duration, trackNumber, path));
                    }
                    while (cursor.moveToNext());
                if (cursor != null){
                    cursor.close();
                }
                e.onNext(arrayList);
                e.onComplete();
            }
        });

    }

    public Observable<List<Song>> getAllSongs(Context context) {
        return getSongsForCursor(makeSongCursor(context, null, null,null));
    }

    private Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "is_music=1 AND title != ''";

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", MediaStore.Audio.Media.DATA},
                selectionStatement, paramArrayOfString, sortOrder);
    }
}
