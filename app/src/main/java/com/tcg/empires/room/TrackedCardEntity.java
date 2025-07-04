package com.tcg.empires.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "trackedCards", indices = {@Index(value = {"oracleId", "setCode"}, unique = true)})
public class TrackedCardEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String id;

    @NonNull
    private String oracleId;

    @NonNull
    private String setCode;

    @NonNull
    private double lastKnownPrice;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getOracleId() {
        return oracleId;
    }

    public void setOracleId(@NonNull String oracleId) {
        this.oracleId = oracleId;
    }

    @NonNull
    public String getSetCode() {
        return setCode;
    }

    public void setSetCode(@NonNull String setCode) {
        this.setCode = setCode;
    }

    public double getLastKnownPrice() {
        return lastKnownPrice;
    }

    public void setLastKnownPrice(double lastKnownPrice) {
        this.lastKnownPrice = lastKnownPrice;
    }
}
