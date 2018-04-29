package com.limingjian.liteplayer.business.filemanager;

import java.util.List;
import java.util.Map;

public interface FileManagerContract {

    interface View{

        void showLoading();

        void hideLoading();

        void displayFiles(List<Map<String, Object>> list);
    }

    interface Presenter{

        void loadFiles(String stringPath);
    }
}
