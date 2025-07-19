package com.tcg.taptrackmtg.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tcg.taptrackmtg.room.TrackedCardEntity;

import java.util.List;

@Dao
public interface TrackedCardDao {

    // Insertar o actualizar en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(TrackedCardEntity card);

    // Buscar por ID
    @Query("SELECT * FROM trackedCards WHERE id = :id")
    TrackedCardEntity getById(int id);

    @Query("SELECT * FROM trackedCards")
    List<TrackedCardEntity> getAllCards();

    @Query("DELETE FROM trackedCards")
    void removeAllCards();

    // Buscar por combinación de oracleId y setCode

    @Query("SELECT * FROM trackedCards WHERE cardId = :cardId")
    List<TrackedCardEntity> getByCardId(String cardId);

    @Query("DELETE FROM trackedCards WHERE cardId = :cardID and userId = :userId")
    void stopTrackingByCardIDAndUserId(String cardID, String userId);

    @Query(
            "    SELECT * FROM trackedCards" +
            "    WHERE id IN (SELECT MAX(id) FROM trackedCards GROUP BY cardId) and period= :period and userId = :userId")
    List<TrackedCardEntity> getTrackedCardsByPeriodAndUserId(int period, String userId);

    @Query(
            "    SELECT * FROM trackedCards" +
                    "    WHERE id IN (SELECT MAX(id) FROM trackedCards GROUP BY cardId) and userId = :userId")
    List<TrackedCardEntity> getTrackedCardsByUserId(String userId);
}


