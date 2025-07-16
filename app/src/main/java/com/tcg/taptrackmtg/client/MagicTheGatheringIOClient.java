package com.tcg.taptrackmtg.client;

import com.tcg.taptrackmtg.service.MagicTheGatheringIOService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MagicTheGatheringIOClient {

    private static final String BASE_URL = "https://api.magicthegathering.io/v1/";
    private static Retrofit retrofit = null;

    public static MagicTheGatheringIOService getMagicTheGatheringIOService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MagicTheGatheringIOService.class);
    }

}
