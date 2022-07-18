package com.example.taobaounion.model;

import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.RecommendCategories;
import com.example.taobaounion.model.domain.RecommendContent;
import com.example.taobaounion.model.domain.TicketParams;
import com.example.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);


    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<RecommendCategories> getRecommendCategories();

    @GET
    Call<RecommendContent> getRecommendContent(@Url String url);
}
