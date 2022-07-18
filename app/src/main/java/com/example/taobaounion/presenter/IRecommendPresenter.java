package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.model.domain.RecommendCategories;
import com.example.taobaounion.view.IRecommendCallback;

public interface IRecommendPresenter extends IBasePresenter<IRecommendCallback> {

    /**
     * 获取精选分类
     */
    void getRecommendCategories();

    /**
     * 根据分类获取商品
     * @param item
     */
    void getContentByCategory(RecommendCategories.DataDTO item);

    /**
     * 重新加载内容
     */
    void reloadContent();

}
