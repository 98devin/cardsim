package cardsim.basics;

import java.util.EnumSet;


public enum Rank {
    
    LOW_ACE(1, "A"),
    TWO  (2),
    THREE(3),
    FOUR (4),
    FIVE (5),
    SIX  (6),
    SEVEN(7),
    EIGHT(8),
    NINE (9),
    TEN  (10, "T"),
    JACK (11, "J"),
    QUEEN(12, "Q"),
    KING (13, "K"),
    ACE  (14, "A");

    public final int value;
    public final String strRep;
    public static final EnumSet<Rank> allRanks       = EnumSet.range(TWO, ACE);
    public static final EnumSet<Rank> allRanksLowAce = EnumSet.range(LOW_ACE, KING);
    
    private Rank(int value) {
        this.value = value;
        this.strRep = Integer.toString(value);
    }

    private Rank(int value, String strRep) {
        this.value = value;
        this.strRep = strRep;
    }

}