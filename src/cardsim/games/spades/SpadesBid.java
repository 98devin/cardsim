package cardsim.games.spades;


// Class representing a past bid during
// the bidding phase of Spades.
public final class SpadesBid {

    // The ID of the player who bid
    public final long playerID;

    // The amount of the bid
    public final int bid;

    public SpadesBid(long playerID, int bid) {
        this.playerID = playerID;
        this.bid = bid;
    }
}