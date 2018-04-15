package com.limingjian.liteplayer.business.filemanager;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileManagerContract {

    interface View{

        void showLoading();

        void hideLoading();

        void displayFiles();
    }

    interface Presenter{

        List<Map<String,Object>> loadFiles(String stringPath);
    }
}
