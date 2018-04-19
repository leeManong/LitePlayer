package com.limingjian.liteplayer.business.filemanager;

import android.content.Context;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.limingjian.liteplayer.App;
import com.limingjian.liteplayer.MainActivity;
import com.limingjian.liteplayer.R;
import com.limingjian.liteplayer.adapter.FileAdapter;
import com.limingjian.liteplayer.base.BaseFragment;
import com.limingjian.liteplayer.bean.FileBean;
import com.limingjian.liteplayer.callback.OnBackPressedCallBack;
import com.limingjian.liteplayer.callback.OnSetToolBarTitleCallBack;
import com.limingjian.liteplayer.utils.ActivityUtils;
import com.limingjian.liteplayer.utils.FileUtils;
import com.limingjian.liteplayer.utils.GetFilesUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayerStandard;

public class FileManagerFragment extends BaseFragment<FileManagerContract.View, FileManagerPresenter> implements FileManagerContract.View {

    public static final String TAG = "FileManagerFragment";

    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.video_swipe_refresh_layout)
    SwipeRefreshLayout mVideoSwipeRefreshLayout;
    @BindView(R.id.fab_play)
    FloatingActionButton mFabPlay;
    @BindView(R.id.video_list_layout)
    CoordinatorLayout mVideoListLayout;
    Unbinder unbinder;

    private List<Map<String, Object>> aList;

    private List<FileBean> mFileBeans;

    private FileAdapter mFileAdapter;

    private OnSetToolBarTitleCallBack mOnToolBarTitleChanged;

    private String parentName;
    private String currPath;


    public static FileManagerFragment newInstance(String path,String name) {
        FileManagerFragment fragment = new FileManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("name",name);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolBarTitleCallBack) {
            mOnToolBarTitleChanged = (OnSetToolBarTitleCallBack) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_manager, container, false);
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
        mFileBeans = new ArrayList<>();
        aList = new ArrayList<>();

        if (getArguments() != null) {
            currPath = getArguments().getString("path");
            parentName = getArguments().getString("name");
        }
        try {
            loadFolderList(currPath);
            Log.i(TAG, "initData: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        analyzeFolderList();

    }

    private void analyzeFolderList() {
        for (Map<String, Object> map : aList) {
            FileBean fileBean = new FileBean();
            fileBean.setPath((String) map.get("fPath"));
            fileBean.setNumSonFolder(map.get("fnumFile") == null ? 0 : (int) map.get("fnumFile"));
            fileBean.setType((String) map.get("ftype"));
            fileBean.setNumMedia(map.get("fnumMedia") == null ? 0 : (int) map.get("fnumMedia"));
            fileBean.setName((String) map.get("fName"));
            fileBean.setDir((boolean) map.get("fIsDir"));
            mFileBeans.add(fileBean);
        }
    }

    private void initView() {
        mFileAdapter = new FileAdapter(R.layout.file_manager_list_item, mFileBeans);
        mRvVideoList.setAdapter(mFileAdapter);
        mRvVideoList.setLayoutManager(new LinearLayoutManager(getContext()));

        mFileAdapter.setOnItemClickListener(getOnItemClickListener());
        mOnToolBarTitleChanged.setToolBarTitle(parentName);

    }

    private BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileBean fileBean = mFileBeans.get(position);
                String path = fileBean.getPath();
                if (getActivity() != null && fileBean.isDir()) {
                    mOnToolBarTitleChanged.setToolBarTitle(fileBean.getName());
                    ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(), FileManagerFragment.newInstance(path,fileBean.getName()), R.id.fl_main_container, true);
                }
                if (fileBean.getType().equals("VIDEO")) {
                    JZVideoPlayerStandard.startFullscreen(getContext(),JZVideoPlayerStandard.class,path,fileBean.getName());
                }
            }
        };
    }

    private void loadFolderList(String file) throws IOException {
        List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(file);
        if (list != null) {
            Collections.sort(list, GetFilesUtils.getInstance().defaultOrder());
            aList.clear();
            mFileBeans.clear();
            for (Map<String, Object> map : list) {
                String fileType = (String) map.get(GetFilesUtils.FILE_INFO_TYPE);
                Map<String, Object> gMap = new HashMap<>();
                //FileBean fileBean = new FileBean();
                //fileBean.setPath(file);
                if (map.get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(true)) {
                    gMap.put("fIsDir", true);
                    gMap.put("ftype", "Folder");
                    //fileBean.setType("Folder");
                    //fileBean.setNumMedia((int)map.get(GetFilesUtils.FILE_INFO_NUM_MEDIA));
                    //fileBean.setNumSonFolder(Integer.parseInt((String)map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS)));
                    //gMap.put("fInfo", map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS)+"个文件夹和"+
                    // map.get(GetFilesUtils.FILE_INFO_NUM_MEDIA)+"个媒体文件");
                    gMap.put("fnumFile", map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS));
                    gMap.put("fnumMedia", map.get(GetFilesUtils.FILE_INFO_NUM_MEDIA));
                    //mFileBeans.add(fileBean);
                } else {
                    //FileBean fileBean1 = new FileBean();
                    gMap.put("fIsDir", false);
                    if (FileUtils.isVideo(fileType)) {
                        // gMap.put("fImg", R.drawable.filetype_text);
                        gMap.put("ftype", "VIDEO");
                        //fileBean1.setType("VIDEO");
                    } else if (FileUtils.isAudio(fileType)) {
                        gMap.put("ftype", "AUDIO");
                        //fileBean1.setType("AUDIO");
                        // gMap.put("fImg", R.drawable.filetype_unknow);
                    } else {
                        gMap.put("ftype", "OTHER");
                        //fileBean1.setType("OTHER");
                    }
                    //gMap.put("fInfo","文件大小:"+GetFilesUtils.getInstance().getFileSize(map.get(GetFilesUtils.FILE_INFO_PATH).toString()));
                    //mFileBeans.add(fileBean1);
                }
                gMap.put("fName", map.get(GetFilesUtils.FILE_INFO_NAME));
                gMap.put(GetFilesUtils.FILE_INFO_PATH, map.get(GetFilesUtils.FILE_INFO_PATH));
                aList.add(gMap);
            }
        } else {
            aList.clear();
        }
        //sAdapter.notifyDataSetChanged();
        //oldernowTv.setText(file);
    }

    private String getParentName(String path) {
        String temp = path.substring(0, path.lastIndexOf("/"));
        return temp.substring(temp.lastIndexOf("/") + 1);
    }


    @Override
    public FileManagerPresenter bindPresenter() {
        return null;
    }

    @Override
    public FileManagerContract.View bindView() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void displayFiles() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (currPath.equals("0")) {
            mOnToolBarTitleChanged.setToolBarTitle("内部存储");
        } else {
            mOnToolBarTitleChanged.setToolBarTitle(getParentName(currPath));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();

    }
}
