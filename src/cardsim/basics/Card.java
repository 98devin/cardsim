package cardsim.basics;

import java.util.*;


public final class Card {

    public final Suit suit; 
    public final Rank rank;

    public Card(Rank rank, Suit suit) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return (rank.strRep + "/" + suit.strRep);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Card))
            return false;
        Card othercard = (Card)obj;
        return (this.suit == othercard.suit
             && this.rank == othercard.rank);
    }

    // Comparator for sorting by rank then suit
    public static final Comparator<Card> rankThenSuitComparator =
        (c1, c2) -> {
            if (c1.rank == c2.rank)
                return (c1.suit.compareTo(c2.suit));
            else
                return (c1.rank.compareTo(c2.rank));
        };

    // Comparator for sorting by suit then rank
    public static final Comparator<Card> suitThenRankComparator =
        (c1, c2) -> {
            if (c1.suit == c2.suit)
                return (c1.rank.compareTo(c2.rank));
            else
                return (c1.suit.compareTo(c2.suit));
        };    
    
}