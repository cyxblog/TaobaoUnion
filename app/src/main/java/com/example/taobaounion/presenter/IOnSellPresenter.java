package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.IOnSellCallback;

public interface IOnSellPresenter extends IBasePresenter<IOnSellCallback> {

    /**
     * 获取特惠商品
     */
    void getOnSellContent();

    /**
     * 重新加载特惠商品
     */
    void reload();

    /**
     * 加载更多
     */
    void loadMore();
}
