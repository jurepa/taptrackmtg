package com.tcg.empires.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tcg.empires.client.ScryfallClient;
import com.tcg.empires.model.ScryfallAutocompleteResponse;
import com.tcg.empires.model.ScryfallCardDetailList;
import com.tcg.empires.model.ScryfallDetailCard;
import com.tcg.empires.service.ScryfallService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDetailViewModel extends ViewModel {

    private MutableLiveData<ScryfallAutocompleteResponse> autocomplete = new MutableLiveData<>();

    private MutableLiveData<ScryfallDetailCard> cards = new MutableLiveData<>();

    private MutableLiveData<ScryfallCardDetailList> prints = new MutableLiveData<>();

    public LiveData<ScryfallDetailCard> getCardDetail() {
        return cards;
    }

    public MutableLiveData<ScryfallCardDetailList> getAllPrintsFromOracleId() {
        return prints;
    }

    public LiveData<ScryfallAutocompleteResponse> getAutocomplete() {
        return autocomplete;
    }

    public void searchCards(String query){
        ScryfallService scryfallService = ScryfallClient.getScryfallService();
        Call<ScryfallAutocompleteResponse> call = scryfallService.getDropdownOptions(query);

        call.enqueue(new Callback<ScryfallAutocompleteResponse>() {
            @Override
            public void onResponse(Call<ScryfallAutocompleteResponse> call, Response<ScryfallAutocompleteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    autocomplete.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ScryfallAutocompleteResponse> call, Throwable t) {
                autocomplete.postValue(null); // o null, según prefieras
            }
        });
    }

    public void getAllPrintsFromOracleId(String oracleId){
        ScryfallService scryfallService = ScryfallClient.getScryfallService();
        oracleId = "oracleId:".concat(oracleId);
        Call<ScryfallCardDetailList> call = scryfallService.getAllPrintsOfCardFromOracleId("released", oracleId, "prints");

        call.enqueue(new Callback<ScryfallCardDetailList>() {
            @Override
            public void onResponse(Call<ScryfallCardDetailList> call, Response<ScryfallCardDetailList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    prints.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ScryfallCardDetailList> call, Throwable t) {
                prints.postValue(null); // o null, según prefieras
            }
        });
    }

    public void searchByExactName(String exactName){
        ScryfallService scryfallService = ScryfallClient.getScryfallService();
        Call<ScryfallDetailCard> call = scryfallService.getCardByExactName(exactName);

        call.enqueue(new Callback<ScryfallDetailCard>() {
            @Override
            public void onResponse(Call<ScryfallDetailCard> call, Response<ScryfallDetailCard> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cards.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ScryfallDetailCard> call, Throwable t) {
                cards.postValue(null); // o null, según prefieras
            }
        });
    }

}