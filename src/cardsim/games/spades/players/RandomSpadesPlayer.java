package cardsim.games.spades.players;

import cardsim.basics.*;
import cardsim.games.spades.*;
import java.util.*;


public final class RandomSpadesPlayer extends SpadesPlayer {

    private final Random random;

    public RandomSpadesPlayer(Hand hand, String displayName, long playerID) {
        super(hand, displayName, playerID);
        this.random = new Random();
    }

    public int getBid(List<SpadesBid> pastBids) {

        
        int totalTricksThisGame = hand.size();
        /*
        int pastBidsSum = 0;
        for (SpadesBid sb : pastBids) {
            pastBidsSum += sb.bid;
        }

        // Return a random number between 0 and the number of tricks
        // not already bet upon by another player.
        // This is a peaceful strategy for what it's worth.
        
        */
        return random.nextInt(totalTricksThisGame + 1);

    }

    public void playCard(List<SpadesAction> pastPlays,
                         Map<Long, Integer> tricksGoal,
                         Map<Long, Integer> tricksTaken) {
        
        // Of course, we ignore most of the input here and just
        // play a random card (of the selection of valid cards)
        
        // If we're the first player, just choose randomly of course.
        if (pastPlays.isEmpty())
            hand.play(random.nextInt(hand.size()));
        // Otherwise, play a random card of the right suit,
        // or if there are none, a totally random card.
        else {
            // We need to know the round's beginning suit.
            Suit beginningSuit = pastPlays.get(0).cardPlayed.suit;

            // Now we see which cards we have of the right suit.
            List<Card> correctSuit = hand.filterBySuit(beginningSuit);

            // If we have some, play one of those.
            if (!correctSuit.isEmpty())
                hand.play(correctSuit.get(random.nextInt(correctSuit.size())));
            // Otherwise, we can play a random card like before.
            else
                hand.play(random.nextInt(hand.size()));

        }

    }

    // We absolutely don't care about this info,
    // Since we're choosing arbitrary cards anyway.
    public void roundResults(List<SpadesAction> roundPlays,
                             long winningPlayerID) {}

}