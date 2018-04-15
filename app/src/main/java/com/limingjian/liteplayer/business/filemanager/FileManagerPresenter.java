package com.limingjian.liteplayer.business.filemanager;

import com.limingjian.liteplayer.base.BasePresenterImpl;
import com.limingjian.liteplayer.utils.GetFilesUtils;

import java.util.List;
import java.util.Map;

public class FileManagerPresenter extends BasePresenterImpl<FileManagerContract.View> implements FileManagerContract.Presenter {


    @Override
    public List<Map<String,Object>> loadFiles(String stringPath) {
        List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(stringPath);
        return list;
    }
}
