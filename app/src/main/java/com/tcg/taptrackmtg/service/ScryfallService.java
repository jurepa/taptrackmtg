package com.tcg.taptrackmtg.service;

import com.tcg.taptrackmtg.model.ScryfallAutocompleteResponse;
import com.tcg.taptrackmtg.model.ScryfallCardDetailList;
import com.tcg.taptrackmtg.model.ScryfallDetailCard;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ScryfallService {
    @GET("/cards/autocomplete")
    @Headers({
            "Accept: application/json",
            "User-Agent: MTGExampleApp/1.0"
    })
    Call<ScryfallAutocompleteResponse> getDropdownOptions(@Query("q") String query);

    @GET("/cards/named")
    @Headers({
            "Accept: application/json",
            "User-Agent: MTGExampleApp/1.0"
    })
    Call<ScryfallDetailCard> getCardByExactName(@Query("exact") String exact);

    @GET("/cards/search")
    @Headers({
            "Accept: application/json",
            "User-Agent: MTGExampleApp/1.0"
    })
    Call<ScryfallCardDetailList> getAllPrintsOfCardFromOracleId(@Query("order") String order, @Query("q") String oracleId, @Query("unique") String unique);

    @GET("/cards/search")
    @Headers({
            "Accept: application/json",
            "User-Agent: MTGExampleApp/1.0"
    })
    Call<ScryfallCardDetailList> searchCardsSync(@Query("order") String order, @Query("q") String oracleId, @Query("unique") String unique);

    @GET("/cards/{id}")
    @Headers({
            "Accept: application/json",
            "User-Agent: MTGExampleApp/1.0"
    })
    Call<ScryfallDetailCard> searchCardsSyncById(@Path("id") String cardId);
}
