package com.limingjian.liteplayer.business.videodirectory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.adapter.VideoDirectoryAdapter;
import com.limingjian.liteplayer.base.BaseFragment;
import com.limingjian.liteplayer.bean.VideoBean;
import com.limingjian.liteplayer.bean.VideoDirectory;
import com.limingjian.liteplayer.business.video.VideoFragment;
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
public class VideoDirectoryFragment extends BaseFragment<VideoContract.View, VideoPresenter> implements VideoContract.View {

    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.video_swipe_refresh_layout)
    SwipeRefreshLayout mVideoSwipeRefreshLayout;
    @BindView(R.id.fab_play)
    FloatingActionButton mFabPlay;
    @BindView(R.id.video_list_layout)
    CoordinatorLayout mVideoListLayout;
    Unbinder unbinder;

    public static final String TAG = "VideoDirectoryFragment";

    private List<VideoBean> mVideoBeans = new ArrayList<>();

    private HashMap<String,VideoDirectory> mVideoDirectoryHashMap = new HashMap<>();

    private List<VideoDirectory> mVideoDirectories = new ArrayList<>();

    private VideoDirectoryAdapter mVideoDirectoryAdapter;

    private OnSetToolBarTitleCallBack mOnToolBarTitleChanged;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolBarTitleCallBack) {
            mOnToolBarTitleChanged = (OnSetToolBarTitleCallBack) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        mOnToolBarTitleChanged.setToolBarTitle(getString(R.string.video));
        mVideoSwipeRefreshLayout.setOnRefreshListener(getOnRefreshListener());

        mVideoDirectoryAdapter = new VideoDirectoryAdapter(R.layout.video_directory_list_item, mVideoDirectories);
        mRvVideoList.setAdapter(mVideoDirectoryAdapter);
        mRvVideoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mVideoDirectoryAdapter.setOnItemClickListener(getOnItemClickListener());


    }

    private BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoDirectory videoDirectory = mVideoDirectories.get(position);
                if (getFragmentManager() != null) {
                    com.limingjian.liteplayer.utils.ActivityUtils.addFragmentToActivity(getFragmentManager(),
                            VideoFragment.newInstance(videoDirectory.getName()),R.id.fl_main_container,true);
                }
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVideoDirectoryHashMap.clear();
                mVideoDirectories.clear();
                getBindPresenter().loadVideo();
                getBindPresenter().loadDirectory();
                mVideoDirectories = castToList();
                mVideoDirectoryAdapter.setNewData(mVideoDirectories);
            }
        };
    }

    private void initData() {
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

        mVideoDirectories = castToList();

    }

    private List<VideoDirectory> castToList() {
        List<VideoDirectory> videoDirectories = new ArrayList<>();
        for (Map.Entry<String, VideoDirectory> entry : mVideoDirectoryHashMap.entrySet()) {
            videoDirectories.add(entry.getValue());
        }
        return videoDirectories;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
        mVideoBeans = videoBeans;
    }

    @Override
    public void displayDirectory(HashMap<String,VideoDirectory> videoDirectoryHashMap) {
        mVideoDirectoryHashMap = videoDirectoryHashMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        mOnToolBarTitleChanged.setToolBarTitle(getString(R.string.video));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
