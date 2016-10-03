package cardsim.basics;

import java.util.*;


public final class InfiniteDrawPile implements DrawPile {

    // a draw pile which produces cards infinitely.

    private final List<Card> cards;
    private final int numberOfCards;
    private final Random random = new Random();

    public InfiniteDrawPile(Set<Rank> usedRanks, Set<Suit> usedSuits) {
        this.cards = new Deck(usedRanks, usedSuits).getCards();
        this.numberOfCards = cards.size();
    }

    public InfiniteDrawPile() {
        this.cards = new Deck().getCards();
        this.numberOfCards = cards.size();
    }

    public boolean isEmpty() { return false; }

    public Card draw() {
        return cards.get(random.nextInt(numberOfCards));
    }

    public List<Card> draw(int n) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            drawnCards.add(draw());
        }
        return drawnCards;
    }

}