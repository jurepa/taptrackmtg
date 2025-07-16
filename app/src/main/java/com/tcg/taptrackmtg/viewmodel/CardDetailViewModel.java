package com.tcg.taptrackmtg.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tcg.taptrackmtg.client.ScryfallClient;
import com.tcg.taptrackmtg.model.ScryfallAutocompleteResponse;
import com.tcg.taptrackmtg.model.ScryfallCardDetailList;
import com.tcg.taptrackmtg.model.ScryfallDetailCard;
import com.tcg.taptrackmtg.repository.TrackedCardRepository;
import com.tcg.taptrackmtg.room.TrackedCardEntity;
import com.tcg.taptrackmtg.service.ScryfallService;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDetailViewModel extends AndroidViewModel {

    private final TrackedCardRepository repository;

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

    public CardDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackedCardRepository(application);
    }

    public void insertCard(TrackedCardEntity card) {
        repository.insert(card);
    }

    public void getCardById(int id, Consumer<TrackedCardEntity> callback) {
        repository.getById(id, callback);
    }

    public void getByCardId(String cardId, Consumer<List<TrackedCardEntity>> callback) {
        repository.getByCardId(cardId, callback);
    }

    public void stopTrackingByCardId(String cardId) {
        repository.stopTrackingByCardId(cardId);
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