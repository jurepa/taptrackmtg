package com.tcg.empires.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScryfallDetailCard {

    private String object;
    private String id;

    @SerializedName("oracle_id")
    private String oracleId;

    @SerializedName("multiverse_ids")
    private List<Integer> multiverseIds;

    @SerializedName("mtgo_id")
    private int mtgoId;

    @SerializedName("tcgplayer_id")
    private int tcgplayerId;

    @SerializedName("cardmarket_id")
    private int cardmarketId;

    private String name;
    private String lang;

    @SerializedName("released_at")
    private String releasedAt;

    private String uri;

    @SerializedName("scryfall_uri")
    private String scryfallUri;

    private String layout;

    @SerializedName("highres_image")
    private boolean highresImage;

    @SerializedName("image_status")
    private String imageStatus;

    @SerializedName("image_uris")
    private ImageUris imageUris;

    @SerializedName("mana_cost")
    private String manaCost;

    private double cmc;

    @SerializedName("type_line")
    private String typeLine;

    @SerializedName("oracle_text")
    private String oracleText;

    private List<String> colors;

    @SerializedName("color_identity")
    private List<String> colorIdentity;

    private List<String> keywords;
    private Legalities legalities;
    private List<String> games;
    private boolean reserved;

    @SerializedName("game_changer")
    private boolean gameChanger;

    private boolean foil;
    private boolean nonfoil;
    private List<String> finishes;
    private boolean oversized;
    private boolean promo;
    private boolean reprint;
    private boolean variation;

    @SerializedName("set_id")
    private String setId;

    private String set;

    @SerializedName("set_name")
    private String setName;

    @SerializedName("set_type")
    private String setType;

    @SerializedName("set_uri")
    private String setUri;

    @SerializedName("set_search_uri")
    private String setSearchUri;

    @SerializedName("scryfall_set_uri")
    private String scryfallSetUri;

    @SerializedName("rulings_uri")
    private String rulingsUri;

    @SerializedName("prints_search_uri")
    private String printsSearchUri;

    @SerializedName("collector_number")
    private String collectorNumber;

    private boolean digital;
    private String rarity;

    @SerializedName("card_back_id")
    private String cardBackId;

    private String artist;

    @SerializedName("artist_ids")
    private List<String> artistIds;

    @SerializedName("illustration_id")
    private String illustrationId;

    @SerializedName("border_color")
    private String borderColor;

    private String frame;

    @SerializedName("frame_effects")
    private List<String> frameEffects;

    @SerializedName("security_stamp")
    private String securityStamp;

    @SerializedName("full_art")
    private boolean fullArt;

    private boolean textless;
    private boolean booster;

    @SerializedName("story_spotlight")
    private boolean storySpotlight;

    @SerializedName("edhrec_rank")
    private int edhrecRank;

    @SerializedName("penny_rank")
    private int pennyRank;

    private Prices prices;

    @SerializedName("related_uris")
    private RelatedUris relatedUris;

    @SerializedName("purchase_uris")
    private PurchaseUris purchaseUris;


    public List<String> getFrameEffects() {
        return frameEffects;
    }

    public void setFrameEffects(List<String> frameEffects) {
        this.frameEffects = frameEffects;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOracleId() {
        return oracleId;
    }

    public void setOracleId(String oracleId) {
        this.oracleId = oracleId;
    }

    public List<Integer> getMultiverseIds() {
        return multiverseIds;
    }

    public void setMultiverseIds(List<Integer> multiverseIds) {
        this.multiverseIds = multiverseIds;
    }

    public int getMtgoId() {
        return mtgoId;
    }

    public void setMtgoId(int mtgoId) {
        this.mtgoId = mtgoId;
    }

    public int getTcgplayerId() {
        return tcgplayerId;
    }

    public void setTcgplayerId(int tcgplayerId) {
        this.tcgplayerId = tcgplayerId;
    }

    public int getCardmarketId() {
        return cardmarketId;
    }

    public void setCardmarketId(int cardmarketId) {
        this.cardmarketId = cardmarketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(String releasedAt) {
        this.releasedAt = releasedAt;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getScryfallUri() {
        return scryfallUri;
    }

    public void setScryfallUri(String scryfallUri) {
        this.scryfallUri = scryfallUri;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public boolean isHighresImage() {
        return highresImage;
    }

    public void setHighresImage(boolean highresImage) {
        this.highresImage = highresImage;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public ImageUris getImageUris() {
        return imageUris;
    }

    public void setImageUris(ImageUris imageUris) {
        this.imageUris = imageUris;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public double getCmc() {
        return cmc;
    }

    public void setCmc(double cmc) {
        this.cmc = cmc;
    }

    public String getTypeLine() {
        return typeLine;
    }

    public void setTypeLine(String typeLine) {
        this.typeLine = typeLine;
    }

    public String getOracleText() {
        return oracleText;
    }

    public void setOracleText(String oracleText) {
        this.oracleText = oracleText;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getColorIdentity() {
        return colorIdentity;
    }

    public void setColorIdentity(List<String> colorIdentity) {
        this.colorIdentity = colorIdentity;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Legalities getLegalities() {
        return legalities;
    }

    public void setLegalities(Legalities legalities) {
        this.legalities = legalities;
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isGameChanger() {
        return gameChanger;
    }

    public void setGameChanger(boolean gameChanger) {
        this.gameChanger = gameChanger;
    }

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    public boolean isNonfoil() {
        return nonfoil;
    }

    public void setNonfoil(boolean nonfoil) {
        this.nonfoil = nonfoil;
    }

    public List<String> getFinishes() {
        return finishes;
    }

    public void setFinishes(List<String> finishes) {
        this.finishes = finishes;
    }

    public boolean isOversized() {
        return oversized;
    }

    public void setOversized(boolean oversized) {
        this.oversized = oversized;
    }

    public boolean isPromo() {
        return promo;
    }

    public void setPromo(boolean promo) {
        this.promo = promo;
    }

    public boolean isReprint() {
        return reprint;
    }

    public void setReprint(boolean reprint) {
        this.reprint = reprint;
    }

    public boolean isVariation() {
        return variation;
    }

    public void setVariation(boolean variation) {
        this.variation = variation;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public String getSetUri() {
        return setUri;
    }

    public void setSetUri(String setUri) {
        this.setUri = setUri;
    }

    public String getSetSearchUri() {
        return setSearchUri;
    }

    public void setSetSearchUri(String setSearchUri) {
        this.setSearchUri = setSearchUri;
    }

    public String getScryfallSetUri() {
        return scryfallSetUri;
    }

    public void setScryfallSetUri(String scryfallSetUri) {
        this.scryfallSetUri = scryfallSetUri;
    }

    public String getRulingsUri() {
        return rulingsUri;
    }

    public void setRulingsUri(String rulingsUri) {
        this.rulingsUri = rulingsUri;
    }

    public String getPrintsSearchUri() {
        return printsSearchUri;
    }

    public void setPrintsSearchUri(String printsSearchUri) {
        this.printsSearchUri = printsSearchUri;
    }

    public String getCollectorNumber() {
        return collectorNumber;
    }

    public void setCollectorNumber(String collectorNumber) {
        this.collectorNumber = collectorNumber;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getCardBackId() {
        return cardBackId;
    }

    public void setCardBackId(String cardBackId) {
        this.cardBackId = cardBackId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(List<String> artistIds) {
        this.artistIds = artistIds;
    }

    public String getIllustrationId() {
        return illustrationId;
    }

    public void setIllustrationId(String illustrationId) {
        this.illustrationId = illustrationId;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getSecurityStamp() {
        return securityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        this.securityStamp = securityStamp;
    }

    public boolean isFullArt() {
        return fullArt;
    }

    public void setFullArt(boolean fullArt) {
        this.fullArt = fullArt;
    }

    public boolean isTextless() {
        return textless;
    }

    public void setTextless(boolean textless) {
        this.textless = textless;
    }

    public boolean isBooster() {
        return booster;
    }

    public void setBooster(boolean booster) {
        this.booster = booster;
    }

    public boolean isStorySpotlight() {
        return storySpotlight;
    }

    public void setStorySpotlight(boolean storySpotlight) {
        this.storySpotlight = storySpotlight;
    }

    public int getEdhrecRank() {
        return edhrecRank;
    }

    public void setEdhrecRank(int edhrecRank) {
        this.edhrecRank = edhrecRank;
    }

    public int getPennyRank() {
        return pennyRank;
    }

    public void setPennyRank(int pennyRank) {
        this.pennyRank = pennyRank;
    }

    public Prices getPrices() {
        return prices;
    }

    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public RelatedUris getRelatedUris() {
        return relatedUris;
    }

    public void setRelatedUris(RelatedUris relatedUris) {
        this.relatedUris = relatedUris;
    }

    public PurchaseUris getPurchaseUris() {
        return purchaseUris;
    }

    public void setPurchaseUris(PurchaseUris purchaseUris) {
        this.purchaseUris = purchaseUris;
    }
}
