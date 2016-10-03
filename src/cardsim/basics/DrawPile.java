package cardsim.basics;


import java.util.*;

public interface DrawPile {

    public Card draw();
    public List<Card> draw(int n);

    public boolean isEmpty();

}