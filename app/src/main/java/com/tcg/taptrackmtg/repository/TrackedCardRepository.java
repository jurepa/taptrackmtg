package com.tcg.taptrackmtg.repository;

import android.app.Application;

import androidx.room.Room;

import com.tcg.taptrackmtg.room.TrackedCardEntity;
import com.tcg.taptrackmtg.room.dao.TrackedCardDao;
import com.tcg.taptrackmtg.room.db.TaptrackDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TrackedCardRepository {
    private final TrackedCardDao trackedCardDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TrackedCardRepository(Application application) {
        TaptrackDatabase db = Room.databaseBuilder(application, TaptrackDatabase.class, "taptrack_database").build();
        this.trackedCardDao = db.trackedCardDao();
    }

    public void insert(final TrackedCardEntity card) {
        executor.execute(() -> trackedCardDao.insertOrUpdate(card));
    }

    public void getById(int id, Consumer<TrackedCardEntity> callback) {
        executor.execute(() -> {
            TrackedCardEntity result = trackedCardDao.getById(id);
            callback.accept(result);
        });
    }

    public void getByCardId(String cardId, Consumer<List<TrackedCardEntity>> callback) {
        executor.execute(() -> {
            List<TrackedCardEntity> result = trackedCardDao.getByCardId(cardId);
            callback.accept(result);
        });
    }

    public void stopTrackingByCardId(String cardId, String userId) {
        executor.execute(() -> {
            trackedCardDao.stopTrackingByCardIDAndUserId(cardId, userId);
        });
    }

    public void getTrackedCards(String userId, Consumer<List<TrackedCardEntity>> callback) {
        executor.execute(() -> {
            List<TrackedCardEntity> result = trackedCardDao.getTrackedCardsByUserId(userId);
            callback.accept(result);
        });
    }

    public List<TrackedCardEntity> getTrackedCardsByPeriod(int period, String userId) {

        return trackedCardDao.getTrackedCardsByPeriodAndUserId(period, userId);

    }

    public List<TrackedCardEntity> getAllCards(){
        return trackedCardDao.getAllCards();
    }


}
