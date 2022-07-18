package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.RecommendCategories;
import com.example.taobaounion.model.domain.RecommendContent;

import java.util.List;

public interface IRecommendCallback extends IBaseCallback {

    /**
     * 获取精选分类
     */
    void onRecommendCategoriesLoaded(List<RecommendCategories.DataDTO> result);

    /**
     * 获取某一分类内容
     */
    void onContentLoaded(RecommendContent result);

}
