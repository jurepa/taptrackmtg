package com.tcg.taptrackmtg.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "trackedCards", indices = {@Index(value = {"cardId"})})
public class TrackedCardEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @NonNull
    private String userId;

    @NonNull
    private String cardId;

    @NonNull
    private String cardName;

    @NonNull
    private String setName;

    @NonNull
    private String imageUrl;

    private int period;

    @NonNull
    private boolean foil;

    @NonNull
    private double lastKnownPrice;

    @NonNull
    private String symbol;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getCardName() {
        return cardName;
    }

    public void setCardName(@NonNull String cardName) {
        this.cardName = cardName;
    }

    @NonNull
    public String getSetName() {
        return setName;
    }

    public void setSetName(@NonNull String setName) {
        this.setName = setName;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NonNull String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getCardId() {
        return cardId;
    }

    public void setCardId(@NonNull String cardId) {
        this.cardId = cardId;
    }

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

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public double getLastKnownPrice() {
        return lastKnownPrice;
    }

    public void setLastKnownPrice(double lastKnownPrice) {
        this.lastKnownPrice = lastKnownPrice;
    }
}
