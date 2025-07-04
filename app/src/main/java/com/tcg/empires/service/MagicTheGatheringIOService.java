package com.tcg.empires.service;

import com.tcg.empires.model.Card;
import com.tcg.empires.model.CardList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MagicTheGatheringIOService {
    @GET("cards")
    Call<CardList> getCards(@Query("name")String name);
}
