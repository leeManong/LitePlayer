package com.limingjian.liteplayer.business.mainmusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.base.BasePresenterImpl;
import com.limingjian.liteplayer.bean.Artist;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ArtistPresenter extends BasePresenterImpl<ArtistContract.View> implements ArtistContract.Presenter {

    @Override
    public void loadArtists() {
        Disposable disposable = getAllArtists(App.getAppContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showLoading();
                    }
                })
                .subscribe(new Consumer<List<Artist>>() {
                    @Override
                    public void accept(List<Artist> artists) throws Exception {
                        if (artists == null || artists.size() == 0) {
                            getView().showEmptyView();
                        } else {
                            getView().displayArtists(artists);
                            getView().hideLoading();
                        }
                    }
                });
        addDisposable(disposable);
    }

    private static Observable<List<Artist>> getArtistsForCursor(final Cursor cursor) {
        return Observable.create(new ObservableOnSubscribe<List<Artist>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Artist>> e) throws Exception {
                List<Artist> arrayList = new ArrayList<Artist>();
                if ((cursor != null) && (cursor.moveToFirst()))
                    do {
                        arrayList.add(new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
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


    public static Observable<List<Artist>> getAllArtists(Context context) {
        return getArtistsForCursor(makeArtistCursor(context, null, null));
    }


    private static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        final String artistSortOrder = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, selection, paramArrayOfString, artistSortOrder);
        return cursor;
    }
}
