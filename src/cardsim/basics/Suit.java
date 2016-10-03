package cardsim.basics;

import java.util.EnumSet;


public enum Suit {
    
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S");

    public final String strRep;
    public static final EnumSet<Suit> allSuits = EnumSet.allOf(Suit.class);

    private Suit(String strRep) {
        this.strRep = strRep;
    }

}