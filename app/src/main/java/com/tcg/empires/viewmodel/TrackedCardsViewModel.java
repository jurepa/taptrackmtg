package com.tcg.empires.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tcg.empires.repository.TrackedCardRepository;
import com.tcg.empires.room.TrackedCardEntity;

import java.util.List;
import java.util.function.Consumer;

public class TrackedCardsViewModel extends AndroidViewModel {

    private MutableLiveData<List<TrackedCardEntity>> trackedCards = new MutableLiveData<>();

    private TrackedCardRepository repository;

    public TrackedCardsViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackedCardRepository(application);
    }

    public MutableLiveData<List<TrackedCardEntity>> getTrackedCards() {
        return trackedCards;
    }

    public void getUserTrackedCards(){
        repository.getTrackedCards(new Consumer<>() {
            @Override
            public void accept(List<TrackedCardEntity> trackedCardEntities) {
                trackedCards.postValue(trackedCardEntities);
            }
        });
    }
}
