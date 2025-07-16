package com.tcg.taptrackmtg.client;


import com.tcg.taptrackmtg.service.JustTCGService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JustTCGClient {
    private static final String BASE_URL = "https://api.justtcg.com/v1";
    private static Retrofit retrofit = null;

    public static JustTCGService getJustTCGService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(JustTCGService.class);
    }
}
