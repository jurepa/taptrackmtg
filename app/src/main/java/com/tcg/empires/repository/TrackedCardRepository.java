package com.tcg.empires.repository;

import android.app.Application;

import androidx.room.Room;

import com.tcg.empires.room.TrackedCardEntity;
import com.tcg.empires.room.dao.TrackedCardDao;
import com.tcg.empires.room.db.TaptrackDatabase;

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

    public void stopTrackingByCardId(String cardId) {
        executor.execute(() -> {
            trackedCardDao.stopTrackingByCardID(cardId);
        });
    }

    public List<TrackedCardEntity> getTrackedCardsByPeriod(int period) {

        return trackedCardDao.getTrackedCardsByPeriod(period);

    }


}
