package com.tcg.taptrackmtg.model;

import com.google.gson.annotations.SerializedName;

public class RelatedUris {
    private String gatherer;

    @SerializedName("tcgplayer_infinite_articles")
    private String tcgplayerInfiniteArticles;

    @SerializedName("tcgplayer_infinite_decks")
    private String tcgplayerInfiniteDecks;

    private String edhrec;

    // Getters y setters

    public String getGatherer() {
        return gatherer;
    }

    public void setGatherer(String gatherer) {
        this.gatherer = gatherer;
    }

    public String getTcgplayerInfiniteArticles() {
        return tcgplayerInfiniteArticles;
    }

    public void setTcgplayerInfiniteArticles(String tcgplayerInfiniteArticles) {
        this.tcgplayerInfiniteArticles = tcgplayerInfiniteArticles;
    }

    public String getTcgplayerInfiniteDecks() {
        return tcgplayerInfiniteDecks;
    }

    public void setTcgplayerInfiniteDecks(String tcgplayerInfiniteDecks) {
        this.tcgplayerInfiniteDecks = tcgplayerInfiniteDecks;
    }

    public String getEdhrec() {
        return edhrec;
    }

    public void setEdhrec(String edhrec) {
        this.edhrec = edhrec;
    }
}

