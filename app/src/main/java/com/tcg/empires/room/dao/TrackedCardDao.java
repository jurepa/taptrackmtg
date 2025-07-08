package com.tcg.empires.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tcg.empires.room.TrackedCardEntity;

import java.util.List;

@Dao
public interface TrackedCardDao {

    // Insertar o actualizar en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(TrackedCardEntity card);

    // Buscar por ID
    @Query("SELECT * FROM trackedCards WHERE id = :id")
    TrackedCardEntity getById(int id);

    // Buscar por combinación de oracleId y setCode
    @Query("SELECT * FROM trackedCards WHERE oracleId = :oracleId AND setCode = :setCode")
    TrackedCardEntity getByOracleIdAndSetCode(String oracleId, String setCode);

    @Query("DELETE FROM trackedCards WHERE oracleId = :oracleId AND setCode = :setCode")
    void stopTrackingByOracleIdAndSetCode(String oracleId, String setCode);

    @Query(
            "    SELECT * FROM trackedCards" +
            "    WHERE id IN (SELECT MAX(id) FROM trackedCards GROUP BY oracleId, setCode) and period= :period")
    List<TrackedCardEntity> getTrackedCardsByPeriod(int period);
}


