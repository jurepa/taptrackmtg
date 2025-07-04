package com.tcg.empires.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tcg.empires.room.TrackedCardEntity;
@Dao
public interface TrackedCardDao {

    // Insertar o actualizar en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(TrackedCardEntity card);

    // Buscar por ID
    @Query("SELECT * FROM trackedCards WHERE id = :id")
    TrackedCardEntity getById(String id);

    // Buscar por combinación de oracleId y setCode
    @Query("SELECT * FROM trackedCards WHERE oracleId = :oracleId AND setCode = :setCode")
    TrackedCardEntity getByOracleIdAndSetCode(String oracleId, String setCode);

    @Query("DELETE FROM trackedCards WHERE oracleId = :oracleId AND setCode = :setCode")
    void stopTrackingByOracleIdAndSetCode(String oracleId, String setCode);
}


