package com.tcg.taptrackmtg.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tcg.taptrackmtg.repository.TrackedCardRepository;
import com.tcg.taptrackmtg.room.TrackedCardEntity;

import java.util.List;
import java.util.function.Consumer;

public class TrackedCardsViewModel extends AndroidViewModel {

    private MutableLiveData<List<TrackedCardEntity>> trackedCards = new MutableLiveData<>();

    private TrackedCardRepository repository;

    public TrackedCardsViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackedCardRepository();
    }

    public MutableLiveData<List<TrackedCardEntity>> getTrackedCards() {
        return trackedCards;
    }

    public void getUserTrackedCards(String userId){
        repository.getTrackedCards(userId, new Consumer<>() {
            @Override
            public void accept(List<TrackedCardEntity> trackedCardEntities) {
                trackedCards.postValue(trackedCardEntities);
            }
        });
    }

    public void removeTrackedCardById(String cardId, String userId){
        repository.stopTrackingByCardId(cardId, userId);
    }
}
