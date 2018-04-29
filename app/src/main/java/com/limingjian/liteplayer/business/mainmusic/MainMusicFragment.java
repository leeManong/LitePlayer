package com.limingjian.liteplayer.business.mainmusic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.callback.OnSetToolBarTitleCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainMusicFragment extends Fragment {

    @BindView(R.id.music_tabs)
    TabLayout mMusicTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;

    private OnSetToolBarTitleCallBack mOnSetToolBarTitleCallBack;

    public static MainMusicFragment newInstance() {
        Bundle args = new Bundle();
        MainMusicFragment fragment = new MainMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolBarTitleCallBack) {
            mOnSetToolBarTitleCallBack = (OnSetToolBarTitleCallBack) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOnSetToolBarTitleCallBack.setToolBarTitle("音频");
        if (mViewpager != null) {
            setupViewPager();
            mViewpager.setOffscreenPageLimit(2);
        }
        mMusicTabs.setupWithViewPager(mViewpager);
    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(SongsFragment.newInstance(),"歌曲");
        adapter.addFragment(SongsFragment.newInstance(),"歌曲");
        adapter.addFragment(SongsFragment.newInstance(),"歌曲");
        mViewpager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
