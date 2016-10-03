package cardsim.basics;


import java.util.*;

public interface DiscardPile {

    public void discard(Card c);
    public void discardAll(List<Card> cs);

}