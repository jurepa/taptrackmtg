package com.tcg.empires.model;

import com.google.gson.annotations.SerializedName;

public class Prices {
    private String usd;

    @SerializedName("usd_foil")
    private String usdFoil;

    @SerializedName("usd_etched")
    private String usdEtched;

    private String eur;

    @SerializedName("eur_foil")
    private String eurFoil;

    private String tix;

    // Getters y setters

    public String getUsd() {
        return usd;
    }

    public void setUsd(String usd) {
        this.usd = usd;
    }

    public String getUsdFoil() {
        return usdFoil;
    }

    public void setUsdFoil(String usdFoil) {
        this.usdFoil = usdFoil;
    }

    public String getUsdEtched() {
        return usdEtched;
    }

    public void setUsdEtched(String usdEtched) {
        this.usdEtched = usdEtched;
    }

    public String getEur() {
        return eur;
    }

    public void setEur(String eur) {
        this.eur = eur;
    }

    public String getEurFoil() {
        return eurFoil;
    }

    public void setEurFoil(String eurFoil) {
        this.eurFoil = eurFoil;
    }

    public String getTix() {
        return tix;
    }

    public void setTix(String tix) {
        this.tix = tix;
    }
}

