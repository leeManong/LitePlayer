package com.limingjian.liteplayer.business.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.adapter.VideoAdapter;
import com.limingjian.liteplayer.adapter.VideoDirectoryAdapter;
import com.limingjian.liteplayer.base.BaseFragment;
import com.limingjian.liteplayer.bean.VideoBean;
import com.limingjian.liteplayer.bean.VideoDirectory;
import com.limingjian.liteplayer.business.videodirectory.VideoContract;
import com.limingjian.liteplayer.business.videodirectory.VideoPresenter;
import com.limingjian.liteplayer.callback.OnSetToolBarTitleCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lmj on 2018/4/11.
 */
public class VideoFragment extends BaseFragment<VideoContract.View, VideoPresenter> implements VideoContract.View {

    public static final String TAG = "VideoDirectoryFragment";
    public static final String NAME = "name";
    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.video_swipe_refresh_layout)
    SwipeRefreshLayout mVideoSwipeRefreshLayout;
    @BindView(R.id.fab_play)
    FloatingActionButton mFabPlay;
    @BindView(R.id.video_list_layout)
    CoordinatorLayout mVideoListLayout;
    Unbinder unbinder;
    private List<VideoBean> mVideoBeans = new ArrayList<>();

    private VideoAdapter mVideoAdapter;

    private HashMap<String,VideoDirectory> mVideoDirectoryHashMap = new HashMap<>();

    private String mDirectoryName;

    private OnSetToolBarTitleCallBack mOnToolBarTitleChanged;

    public static VideoFragment newInstance(String directoryName) {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NAME, directoryName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolBarTitleCallBack) {
            mOnToolBarTitleChanged = (OnSetToolBarTitleCallBack) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        if (getArguments() != null) {
            mDirectoryName = getArguments().getString(NAME);
            mOnToolBarTitleChanged.setToolBarTitle(mDirectoryName);
        }

        getBindPresenter().loadVideo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getBindPresenter().loadDirectory();

    }

    private void initView() {


        mVideoSwipeRefreshLayout.setOnRefreshListener(getOnRefreshListener());

        mVideoAdapter = new VideoAdapter(R.layout.video_list_item, mVideoBeans);
        mRvVideoList.setAdapter(mVideoAdapter);
        mRvVideoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mVideoAdapter.setOnItemClickListener(getOnItemClickListener());
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVideoBeans.clear();
                mVideoDirectoryHashMap.clear();
                initData();
                mVideoAdapter.setNewData(mVideoBeans);
            }
        };
    }

    private BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public VideoPresenter bindPresenter() {
        return new VideoPresenter();
    }

    @Override
    public VideoContract.View bindView() {
        return this;
    }

    @Override
    public void showLoading() {
        mVideoSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mVideoSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingError() {
        ToastUtils.showShort(R.string.load_error_hint);
    }

    @Override
    public void displayVideos(List<VideoBean> videoBeans) {
    }

    @Override
    public void displayDirectory(HashMap<String, VideoDirectory> videoDirectoryHashMap) {
        mVideoDirectoryHashMap = videoDirectoryHashMap;
        mVideoBeans = mVideoDirectoryHashMap.get(mDirectoryName).getVideoBeans();
    }


    @Override
    public void onPause() {
        super.onPause();
        mOnToolBarTitleChanged.setToolBarTitle(getString(R.string.video));
    }

}