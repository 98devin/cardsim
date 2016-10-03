package cardsim.games.spades;

import cardsim.basics.*;
import java.util.*;


public abstract class SpadesPlayer {

    // The player's hand.
    // It is public, but no player will be able
    // to gain a reference to another's hand.
    public final Hand hand;

    // The display name which will be printed after simulations.
    public final String displayName;

    // An ID which identifies a player uniquely in a given simulation.
    public final long playerID;

    public SpadesPlayer(Hand hand, String displayName, long playerID) {
        this.hand = hand;
        this.displayName = displayName;
        this.playerID = playerID;
    }

    @Override
    public final String toString() {
        return (displayName + " (" + Long.toString(playerID) + ")");
    }


    // Called when you are required to bid
    public abstract int getBid(List<SpadesBid> pastBids);

    // Called when you are required to play a card
    public abstract void playCard(List<SpadesAction> pastPlays,
                                  Map<Long, Integer> tricksGoal,
                                  Map<Long, Integer> tricksTaken);

    // Called to notify the player of the result of a round.
    public abstract void roundResults(List<SpadesAction> roundPlays,
                                      long winningPlayerID);


}