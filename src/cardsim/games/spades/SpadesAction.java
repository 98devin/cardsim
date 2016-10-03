package cardsim.games.spades;

import cardsim.basics.*;


// Class representing a past turn during the main
// phase of Spades.
public final class SpadesAction {

    // The ID of the player whose turn it was
    public final long playerID;

    // The card that the player played
    public final Card cardPlayed;

    public SpadesAction(long playerID, Card cardPlayed) {
        this.playerID = playerID;
        this.cardPlayed = cardPlayed;
    }

}