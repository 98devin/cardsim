package cardsim.basics;

import java.util.*;
import java.util.function.Predicate;


public interface Hand {

    // Prepares a card for play.
    // Multiple cards can be scheduled for play in this manner in one turn.
    // Returns whether the play was successful (i.e. whether you had the card)
    public boolean play(Card card);

    // Play a card by its index in the cards list.
    public boolean play(int n);

    // Unschedules a card to be played.
    // Returns whether the unplay was successful (i.e. whether it had been played)
    public boolean unplay(Card card);

    // Unschedules a card by its index in the scheduled cards list.
    // Worth noting is that newly scheduled cards will be last.
    public boolean unplay(int n);

    // Unschedules all cards scheduled to be played.
    public void unplayAll();

    // These sort functions allow the player to
    // sort their hand by rank or suit first.
    public void sortSuitRank();
    public void sortRankSuit();

    // This sort function takes in a Comparator<Card>
    // allowing the player to define a custom sort order.
    public void sort(Comparator<Card> comparator);

    // These filter functions allow the player to
    // see cards of only one rank or suit.
    public List<Card> filterBySuit(Suit suit);
    public List<Card> filterByRank(Rank rank);

    // This filter function takes in a Predicate<Card>
    // allowing the player to define a custom filter.
    public List<Card> filter(Predicate<Card> predicate);

    // Returns the number of cards in the hand.
    public int size();

    // Returns the number of cards scheduled for play.
    public int scheduledSize();

    // Returns an immutable list of the cards in the hand.
    public List<Card> getCards();

    // Returns an immutable list of the cards scheduled to be played.
    public List<Card> getScheduledCards();

    // Processes one scheduled card as having been played.
    public Card accept();

    // Processes `n` scheduled cards as having been played.
    public List<Card> accept(int n);

    // Processes all scheduled cards as having been played.
    public List<Card> acceptAll();

    // Returns all unaccepted scheduled cards to the hand.
    public void returnUnused();

}