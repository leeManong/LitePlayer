package com.limingjian.liteplayer.business.history;

import com.limingjian.liteplayer.base.BasePresenterImpl;
import com.limingjian.liteplayer.bean.MediaBean;
import com.limingjian.liteplayer.utils.MediaHistoryManager;

import java.util.List;

/**
 * Created by lmj on 2018/4/29.
 */
public class HistoryPresenter extends BasePresenterImpl<HistoryContract.View> implements HistoryContract.Presenter {


    @Override
    public void loadHistory() {
        List<MediaBean> mediaBeans = MediaHistoryManager.getMediaHistory();
        getView().displayHistory(mediaBeans);
    }
}
