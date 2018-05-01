package com.limingjian.liteplayer.business.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.limingjian.liteplayer.MainActivity;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.adapter.HistoryAdapter;
import com.limingjian.liteplayer.base.BaseFragment;
import com.limingjian.liteplayer.bean.MediaBean;
import com.limingjian.liteplayer.callback.OnHistoryDataChangedCallBack;
import com.limingjian.liteplayer.callback.OnSetToolBarTitleCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by lmj on 2018/4/29.
 */
public class HistoryFragment extends BaseFragment<HistoryContract.View, HistoryPresenter> implements HistoryContract.View,OnHistoryDataChangedCallBack {

    @BindView(R.id.recycler_view_history)
    RecyclerView mRecyclerViewHistory;
    Unbinder unbinder;

    private HistoryAdapter mHistoryAdapter;

    List<MediaBean> mMediaBeans = new ArrayList<>();

    private OnSetToolBarTitleCallBack mOnToolBarTitleChanged;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolBarTitleCallBack) {
            mOnToolBarTitleChanged = (OnSetToolBarTitleCallBack) context;
        }
    }

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mOnToolBarTitleChanged.setToolBarTitle("播放记录");
        getBindPresenter().loadHistory();

        mHistoryAdapter.setOnItemClickListener(getOnItemClickListener());

        MainActivity.setOnHistoryDataChangedCallBack(this);
    }

    private BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaBean mediaBean = mMediaBeans.get(position);
                if (mediaBean.isVideo()) {
                    JZVideoPlayerStandard.startFullscreen(getContext(),JZVideoPlayerStandard.class,mediaBean.getPath(),mediaBean.getName());
                }
            }
        };
    }

    @Override
    public HistoryPresenter bindPresenter() {
        return new HistoryPresenter();
    }

    @Override
    public HistoryContract.View bindView() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void displayHistory(List<MediaBean> mediaBeans) {
        this.mMediaBeans.clear();
        this.mMediaBeans.addAll(mediaBeans);

        mHistoryAdapter = new HistoryAdapter(R.layout.history_list_item, this.mMediaBeans);
        mRecyclerViewHistory.setAdapter(mHistoryAdapter);
        mHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void refreshHistory() {
        getBindPresenter().loadHistory();
    }
}
