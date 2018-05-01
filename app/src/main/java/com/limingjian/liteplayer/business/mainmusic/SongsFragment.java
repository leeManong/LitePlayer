package com.limingjian.liteplayer.business.mainmusic;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.GlideApp;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.adapter.SongListAdapter;
import com.limingjian.liteplayer.base.BaseFragment;
import com.limingjian.liteplayer.bean.Song;
import com.limingjian.liteplayer.callback.OnSetToolBarTitleCallBack;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SongsFragment extends BaseFragment<SongsContract.View, SongsPresenter> implements SongsContract.View {

    public static final String TAG = "SongsFragment";
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.tv_song_name)
    TextView mTvSongName;
    @BindView(R.id.song_artist)
    TextView mSongArtist;
    @BindView(R.id.view_empty)
    RelativeLayout mViewEmpty;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.avl)
    AVLoadingIndicatorView mAvl;
    Unbinder unbinder;
    @BindView(R.id.btn_start_pause)
    ImageButton mBtnStartPause;
    @BindView(R.id.btn_next)
    ImageButton mBtnNext;


    private MediaPlayer mMediaPlayer;

    private SongListAdapter mSongListAdapter;
    private List<Song> mSongList = new ArrayList<>();

    private boolean isPaused;

    private OnSetToolBarTitleCallBack mOnSetToolBarTitleCallBack;

    private int currPosition = -1;

    public static SongsFragment newInstance() {
        SongsFragment fragment = new SongsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolBarTitleCallBack) {
            mOnSetToolBarTitleCallBack = (OnSetToolBarTitleCallBack) context;
        }
    }

    @Override
    public SongsPresenter bindPresenter() {
        return new SongsPresenter();
    }

    @Override
    public SongsContract.View bindView() {
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mOnSetToolBarTitleCallBack.setToolBarTitle("音频");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSongListAdapter = new SongListAdapter(R.layout.song_list_item, mSongList);
        mRecyclerView.setAdapter(mSongListAdapter);
        mSongListAdapter.setOnItemClickListener(getOnItemClickListener());

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(getOnPreparedListener());

        mTvSongName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTvSongName.setSingleLine(true);
        mTvSongName.setSelected(true);
        mTvSongName.setFocusable(true);
        mTvSongName.setFocusableInTouchMode(true);

    }

    private void initData() {
        getBindPresenter().loadSongs();
    }

    private MediaPlayer.OnPreparedListener getOnPreparedListener() {

        return new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void showLoading() {
        mAvl.show();
    }

    @Override
    public void hideLoading() {
        mAvl.hide();
    }

    @Override
    public void showEmptyView() {
        mViewEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void displaySongs(List<Song> songs) {
        mSongList.clear();
        mSongList.addAll(songs);
        mSongListAdapter.notifyDataSetChanged();

    }

    private BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                playMusic(position);
                currPosition = position;
            }
        };
    }

    private void playMusic(int position) {
        String path = mSongList.get(position).path;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
            mBtnStartPause.setBackgroundResource(R.mipmap.ic_pause);

            Song song = mSongList.get(position);
            String uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), song.albumId).toString();
            GlideApp.with(App.getAppContext()).load(uri)
                    .placeholder(R.mipmap.icon_album_default)
                    .into(mImage);
            mTvSongName.setText(song.title);
            mSongArtist.setText(song.artistName);

        } catch (IOException e) {
            Log.e(TAG, "onItemClick: " + e.getMessage());
        }
    }

    @OnClick({R.id.btn_start_pause, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_pause:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mBtnStartPause.setBackgroundResource(R.mipmap.ic_start);
                } else {
                    mMediaPlayer.start();
                    mBtnStartPause.setBackgroundResource(R.mipmap.ic_pause);
                }
                break;
            case R.id.btn_next:
                if (currPosition == mSongList.size()-1) {
                    currPosition = 0;
                    playMusic(currPosition);
                } else {
                    currPosition++;
                    playMusic(currPosition);
                }
                break;
            default:
        }
    }

}
