package com.tcg.taptrackmtg.service;

import com.tcg.taptrackmtg.model.CardList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MagicTheGatheringIOService {
    @GET("cards")
    Call<CardList> getCards(@Query("name")String name);
}
