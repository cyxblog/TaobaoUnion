package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.OnSellContent;

public interface IOnSellCallback extends IBaseCallback {

    /**
     * 加载特惠商品成功
     */
    void onContentLoaded(OnSellContent result);

    /**
     * 更多特惠商品加载成功
     * @param result
     */
    void onMoreLoaded(OnSellContent result);

    /**
     * 更多商品加载失败
     */
    void onMoreLoadError();

    /**
     * 没有更多商品
     */
    void onMoreLoadEmpty();
}
