package com.tcg.empires.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "trackedCards", indices = {@Index(value = {"oracleId", "setCode"}, unique = true)})
public class TrackedCardEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @NonNull
    private String oracleId;

    @NonNull
    private String setCode;

    private int period;

    @Nullable
    private String condition;

    @NonNull
    private double lastKnownPrice;

    @NonNull
    private String symbol;


    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
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
