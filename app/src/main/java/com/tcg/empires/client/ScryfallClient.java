package com.tcg.empires.client;

import com.tcg.empires.service.ScryfallService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScryfallClient {

    private static final String BASE_URL = "https://api.scryfall.com";
    private static Retrofit retrofit = null;

    public static ScryfallService getScryfallService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ScryfallService.class);
    }

}

