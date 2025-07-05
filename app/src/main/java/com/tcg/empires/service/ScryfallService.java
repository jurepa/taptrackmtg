package com.tcg.empires.service;

import com.tcg.empires.model.ScryfallAutocompleteResponse;
import com.tcg.empires.model.ScryfallCardDetailList;
import com.tcg.empires.model.ScryfallDetailCard;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

}
