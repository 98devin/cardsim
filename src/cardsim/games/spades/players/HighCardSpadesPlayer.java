package cardsim.games.spades.players;


import java.util.*;
import cardsim.basics.*;
import cardsim.games.spades.*;

public final class HighCardSpadesPlayer extends SpadesPlayer {

    public int getBid(List<SpadesBid> pastBids) {

        hand.sortRankSuit();

        // Get a conservative estimate here.
        // Actually maybe this is a bit overzealous, but whatever.
        return hand.filter(c -> c.rank >= Rank.JACK).size();

    }

    public void playCard(List<SpadesAction> pastPlays,
                         Map<Long, Integer> tricksGoal,
                         Map<Long, Integer> tricksTaken) {

        if (pastPlays.isEmpty()) {
            hand.sortRankSuit();
            hand.play(hand.size() - 1);
        } else {
            List<Card> validCard
        }

    }

}