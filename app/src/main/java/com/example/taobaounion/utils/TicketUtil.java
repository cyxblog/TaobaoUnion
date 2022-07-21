package com.example.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.taobaounion.TicketActivity;
import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.presenter.ITicketPresenter;

public class TicketUtil{

    public static void toTicketPage(Context context, IBaseInfo baseInfo){
        //处理数据
        String title = baseInfo.getTitle();
        //领券页面
        String url = baseInfo.getUrl();
        String cover = baseInfo.getCover();
        // 拿到TicketPresenter对象
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
