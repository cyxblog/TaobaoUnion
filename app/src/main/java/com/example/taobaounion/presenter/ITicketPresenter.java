package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ITicketCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketCallback> {

    /**
     * 生成淘口令
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title, String url, String cover);
}
