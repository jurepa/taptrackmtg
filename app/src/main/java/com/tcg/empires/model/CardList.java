package com.tcg.empires.model;

import java.io.Serializable;
import java.util.List;

public class CardList implements Serializable {
    private static final long serialVersionUID = 12746127L;
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
