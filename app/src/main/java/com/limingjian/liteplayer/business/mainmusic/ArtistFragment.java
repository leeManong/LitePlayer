package com.limingjian.liteplayer.business.mainmusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.base.BaseFragment;
import com.limingjian.liteplayer.bean.Artist;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ArtistFragment extends BaseFragment<ArtistContract.View, ArtistPresenter> implements ArtistContract.View {

    @BindView(R.id.view_empty)
    RelativeLayout mViewEmpty;
    @BindView(R.id.avl)
    AVLoadingIndicatorView mAvl;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public ArtistPresenter bindPresenter() {
        return new ArtistPresenter();
    }

    @Override
    public ArtistContract.View bindView() {
        return this;
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
    public void displayArtists(List<Artist> artistList) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
