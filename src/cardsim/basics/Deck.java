package cardsim.basics;

import java.util.*;


public final class Deck implements DrawPile, DiscardPile {

    private List<Card> cards;
    
    // number of 52-card decks this deck is made up of
    private final int numberOfDecks; 
    
    // these two values determine the suits and ranks used in a full deck
    private final Set<Suit> usedSuits;
    private final Set<Rank> usedRanks;

    public Deck(int numberOfDecks, Set<Rank> usedRanks, Set<Suit> usedSuits) {
        this.numberOfDecks = numberOfDecks;
        this.usedSuits = usedSuits;
        this.usedRanks = usedRanks;
        this.cards = new ArrayList<>();
        reset();
    }

    public Deck(Set<Rank> usedRanks, Set<Suit> usedSuits) {
        this(
            1,
            usedRanks,
            usedSuits
        );
    }

    public Deck(int numberOfDecks) {
        this(
            numberOfDecks,
            Rank.allRanks,
            Suit.allSuits
        );
    }

    public Deck() {
        this(
            1,
            Rank.allRanks,
            Suit.allSuits
        );
    }

    // shuffles the deck
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    // retrieves a card from the Deck, or null if none remain
    public Card draw() {
        if (cards.size() > 0)
            return cards.remove(0);
        else
            return null;
    }
    
    // retrieves `n` cards from the Deck, or as many remain if there are fewer
    public List<Card> draw(int n) {
        List<Card> drawnCards = new ArrayList<>();
        Card card;
        for (int i = 0; i < n; i++) {
            card = draw();
            if (card != null)
                drawnCards.add(card);
        }
        return drawnCards;
    }
    
    // returns a card to the deck (to be used as a discard pile for example)
    public void discard(Card c) {
        cards.add(0, c);
    }

    public void discardAll(List<Card> cs) {
        for (Card c : cs) {
            cards.add(0, c);
        }
    }
    
    // clears the Deck and repopulates with new cards
    public void reset() {

        cards.clear();

        for (int i = 0; i < numberOfDecks; i++)
        for (Suit s : usedSuits)
        for (Rank r : usedRanks) {
            cards.add(new Card(r, s));
        }

    }

    // informs the player whether cards remain
    public boolean isEmpty() {
        return (cards.size() == 0);
    }

    // informs the player how many cards remain
    public int size() {
        return cards.size();
    }

    // returns the list of cards, immutably
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    // sorts the list of cards by suit then rank
    public void sortSuitRank() {
        Collections.sort(cards, Card.suitThenRankComparator);
    }
    
    // sorts the list of cards by rank then suit
    public void sortRankSuit() {
        Collections.sort(cards, Card.rankThenSuitComparator);
    }

    // sorts by a given other comparator
    public void sort(Comparator<Card> comparator) {
        Collections.sort(cards, comparator);
    }

}