package cardsim.basics;

import java.util.*;
import java.util.function.Predicate;


public final class OpenHand implements Hand {

    private List<Card> cards;          // all cards the player has
    private List<Card> scheduledCards; // cards scheduled to be played this turn

    public OpenHand(List<Card> cards) {
        this.cards = cards;
        this.scheduledCards = new ArrayList<>();
    }

    public boolean play(Card c) {
        boolean successful;
        if (successful = cards.remove(c)) { // if the player did in fact have the card, and now does not...
            scheduledCards.add(c); // add the removed card to be scheduled for play
        }
        return successful;
    }

    public boolean play(int n) {
        if (cards.size() <= n)
            return false;
        else {
            scheduledCards.add(cards.remove(n));
            return true;
        }
    }

    public boolean unplay(Card c) {
        boolean successful;
        if (successful = scheduledCards.remove(c)) {
            cards.add(c);
        }
        return successful;
    }

    public boolean unplay(int n) {
        if (scheduledCards.size() <= n)
            return false;
        else {
            cards.add(scheduledCards.remove(n));
            return true;
        }
    }

    public void unplayAll() {
        cards.addAll(scheduledCards);
        scheduledCards.clear();
    }

    public void add(Card c) {
        cards.add(c);
    }   

    public void addAll(Collection<? extends Card> newCards) {
        cards.addAll(newCards);
    }

    public void sortSuitRank() {
        Collections.sort(cards, Card.suitThenRankComparator);
    }

    public void sortRankSuit() {
        Collections.sort(cards, Card.rankThenSuitComparator);
    }

    public void sort(Comparator<Card> comparator) {
        Collections.sort(cards, comparator);
    }

    public List<Card> filterBySuit(Suit s) {
        return filter(c -> c.suit == s);
    }

    public List<Card> filterByRank(Rank r) {
        return filter(c -> c.rank == r);
    }

    public List<Card> filter(Predicate<Card> pred) {
        List<Card> filtered = new ArrayList<>();
        for (Card c : cards) {
            if (pred.test(c))
                filtered.add(c);
        }
        return filtered;
    }

    public int size() {
        return cards.size();
    }

    public int scheduledSize() {
        return scheduledCards.size();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public List<Card> getScheduledCards() {
        return Collections.unmodifiableList(scheduledCards);
    }

    public Card accept() {
        return scheduledCards.remove(0);
    }

    public List<Card> accept(int n) {
        List<Card> acceptedCards = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            acceptedCards.add(scheduledCards.remove(0));
        }
        return acceptedCards;
    }

    public List<Card> acceptAll() {
        List<Card> acceptedCards = new ArrayList<>(scheduledCards);
        scheduledCards.clear();
        return acceptedCards;
    }

    public void returnUnused() {
        cards.addAll(scheduledCards);
        scheduledCards.clear();
    }

}