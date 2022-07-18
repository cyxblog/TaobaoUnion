package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类id获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    /**
     * 加载更多
     * @param categoryId
     */
    void loadMore(int categoryId);

    /**
     * 重新加载
     * @param categoryId
     */
    void reload(int categoryId);
}
