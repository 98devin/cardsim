package cardsim.games.spades;

import cardsim.basics.*;
import java.util.*;


public final class SpadesGame /* implements SimulatableGame */ {

    // The primary deck of the game
    private final Deck deck;

    // The players participating, accessible by their ID
    private final Map<Long, SpadesPlayer> players;

    // The list of player implementations to draw from
    private final List<SpadesPlayerImpls> playerImpls;

    // How many cards need to be dealt in a hand
    private final int handSize;

    public SpadesGame(SpadesPlayerImpls... playerImpls) {
        this.deck = new Deck(Math.max(playerImpls.length / 4, 1));
        this.players = new HashMap<>(playerImpls.length);
        this.playerImpls = Arrays.asList(playerImpls);
        this.handSize = deck.size() / playerImpls.length;
    }

    // Play a game of Spades.
    // Returns a mapping of playerID to points earned.
    public Map<Long, Long> game() {
        return game(false);
    }
    
    // Play a game with a chosen verbosity level.
    public Map<Long, Long> game(boolean verbose) {

        // Prepare deck and players map to be used
        deck.reset();
        deck.shuffle();
        players.clear();
        List<Long> playerOrder = new ArrayList<>(playerImpls.size());

        // Increments for each player created.
        // Presumably no more than (2^31)-1 players will exist.
        long playerIDcounter = 0;

        // Initialize each player and add them to players map         
        for (SpadesPlayerImpls spi : playerImpls) {

            Hand hand = new OpenHand(deck.draw(handSize));
            SpadesPlayer player = spi.get(hand, playerIDcounter);
            players.put(playerIDcounter, player);
            playerOrder.add(playerIDcounter);
            
            playerIDcounter++;

        }

        // Ensure order of play is random every game
        Collections.shuffle(playerOrder);


        //
        // Stage 1: Bidding
        //

        // The number of tricks each player wants to get
        Map<Long, Integer> tricksGoal  = new HashMap<>(players.size());
        // The number of tricks each player has actually gotten
        Map<Long, Integer> tricksTaken = new HashMap<>(players.size()); 

        {
            List<SpadesBid> pastBids = new ArrayList<>(players.size());
            
            for (Long playerID : playerOrder) {
                SpadesPlayer player = players.get(playerID);
                int bid = player.getBid(pastBids);
                if (verbose) {
                    System.out.println(player + " bid " + bid + " tricks.");
                }
                tricksGoal.put(playerID, bid);
                tricksTaken.put(playerID, 0);
                pastBids.add(new SpadesBid(playerID, bid));
            }
        }


        //
        // Stage 2: Main game
        //


        List<SpadesAction> pastPlays = new ArrayList<>(players.size());
        List<Long> thisTurnOrder;
        Card bestCardSoFar;
        Suit beginningSuit;
        Long bestPlayerSoFar;

        // Gameplay repeats once for each card in players' initial hands
        for (int round = 0; round < handSize; round++) {

            pastPlays.clear();
            thisTurnOrder = new ArrayList<>(playerOrder);
            bestCardSoFar = null;
            bestPlayerSoFar = null;
            beginningSuit = null;

            if (verbose)
                System.out.println("-- Trick " + (round + 1) + " --");

            // This loop runs through the players and asks them to play
            for (long playerID : thisTurnOrder) {

                // Get the object representing the next player to play.
                SpadesPlayer player = players.get(playerID);
                
                // Ask the player to play based on past plays and other info.
                player.playCard(pastPlays, tricksGoal, tricksTaken);
                // Retrieve the player's hand with info on what they played.
                Hand hand = player.hand;

                // This card will be initialized unless
                // the error scenario below happens, in which case the program ends anyway.
                Card playedCard = null;

                // If the player didn't play enough cards or played too many, it is an unrecoverable scenario.
                if (hand.scheduledSize() != 1) {
                    majorInfraction(
                        player, 
                        "(1) card was expected to be played, instead got (" + hand.scheduledSize() + ")."
                    );
                // Or we just have the normal case when they play one card as intended.
                } else {
                    playedCard = hand.accept();
                }


                // Add the playedCard and playerID to the pastPlays list
                // so that future players will know that it's been played.
                pastPlays.add(new SpadesAction(playerID, playedCard));

                // Determine whether the new card is better than those already played,
                // or if it is the first, store information such as its suit for later comparisons.
                if (bestCardSoFar == null) {
                    if (verbose)
                        System.out.println(player + " began the round playing [" + playedCard + "]");
                    bestCardSoFar = playedCard;
                    bestPlayerSoFar = playerID;
                    beginningSuit = playedCard.suit;
                // If it wasn't the first card, see whether it's better than the previous best one.
                } else {
                    
                    // First we have to make sure the player followed the rules.
                    // If the suit doesn't match, they must not have had any cards of the correct suit.
                    if (playedCard.suit != beginningSuit) {
                        // Either the player broke the rules and played another suit for no good reason
                        if (!hand.filterBySuit(beginningSuit).isEmpty()) {
                            majorInfraction(
                                player,
                                "The suit (" + beginningSuit + ") was expected, instead got (" + playedCard.suit + ") although the correct suit was available."
                            );
                        // Or the player really didn't have a card of the right suit.
                        } else {
                            if (verbose)
                                System.out.println("In response, " + player + " played [" + playedCard + "]");
                            // In that case, see if it's a spade, and if the past card was too.
                            // Either way, if the played card isn't a spade it will lose.
                            if (playedCard.suit == Suit.SPADES) {
                                // If the newly played card wins in a spade battle, update the best card so far and reorder
                                // the turn so that the new winner will go first in the next round.
                                if ((bestCardSoFar.suit != Suit.SPADES) || // Either the spade wins against non-spade by default
                                    (bestCardSoFar.suit == Suit.SPADES && playedCard.rank.compareTo(bestCardSoFar.rank) > 0)) { // Or it is decided by rank
                                    if (verbose)
                                        System.out.println("    ... It's the new best card!");
                                    bestCardSoFar = playedCard;
                                    bestPlayerSoFar = playerID;
                                }
                            }
                        }
                    // Or, in the normal case, the player DID play a card of the beginning suit.
                    } else {
                        if (verbose)
                            System.out.println("In response, " + player + " played [" + playedCard + "]");
                        // In which case we just compare similarly to above.
                        // First we consider that the suit for the round might be spades anyway.
                        if (beginningSuit == Suit.SPADES) {
                            // In this case, the best card HAS to be spades, so we just compare ranks.
                            if (playedCard.rank.compareTo(bestCardSoFar.rank) > 0) { // check for victory
                                if (verbose)
                                    System.out.println("    ... It's the new best card!");
                                bestCardSoFar = playedCard;
                                bestPlayerSoFar = playerID;
                            }
                        // Otherwise the beginning suit isn't spades.
                        } else {
                            // If the beginning suit wasn't spades, the winning card still might be,
                            // in which case the new card loses. If it isn't, though, there's a chance!
                            if (!(bestCardSoFar.suit == Suit.SPADES)) {
                                if (playedCard.rank.compareTo(bestCardSoFar.rank) > 0) { // check for victory
                                    if (verbose)
                                        System.out.println("    ... It's the new best card!");
                                    bestCardSoFar = playedCard;
                                    bestPlayerSoFar = playerID;
                                }
                            }
                        }
                    }
                }

            }



            if (verbose) {
                System.out.println(players.get(bestPlayerSoFar) + " won the trick!");
                System.out.println();
            }

            // Notify each player of the winner of the round.
            for (long playerID : thisTurnOrder)
                players.get(playerID).roundResults(pastPlays, bestPlayerSoFar);

            // Increment the number of tricks taken for the player who won.
            tricksTaken.put(bestPlayerSoFar, tricksTaken.get(bestPlayerSoFar) + 1);

            // Rotate the player order so the winner goes first next round.
            Collections.rotate(playerOrder, -(playerOrder.indexOf(bestPlayerSoFar)));

        }

        // Now all the rounds have been played, and the results are in. (In theory).

        //
        // Stage 3: Score calculation
        //

        // Prepare the mapping of points as the return value of this function.
        Map<Long, Long> finalScore = new HashMap<>(players.size());


        // Sort the order so we can print them in order of ID
        if (verbose)
            Collections.sort(playerOrder);

        // Iterate through the players and check each of their performances
        for (long playerID : playerOrder) {

            int goalTricks = tricksGoal.get(playerID);
            int wonTricks  = tricksTaken.get(playerID);

            long actualScore;
            // If the player won fewer tricks than they bet,
            if (wonTricks < goalTricks) {
                actualScore = 0; // They win nothing at all.
            // But if they met or exceeded their goal,
            } else {
                // They get 10 points per trick, and -10 for each over their bet.
                actualScore = Math.max(0, 10*(2*goalTricks - wonTricks));
            }

            if (verbose)
                System.out.println(players.get(playerID) + " won " + wonTricks + "/" + goalTricks + " predicted tricks, receiving " + actualScore + " points.");
            
            finalScore.put(playerID, actualScore);
        }

        // And lastly, report the results!
        // Let's hope this works!
        return finalScore;

    }

    // Function to notify players and end the simulation when something has gone unrecoverably wrong.
    public void majorInfraction(SpadesPlayer infractor, String infractionDescription) {
        System.out.println("Error! Player " + infractor + " failed to abide by the rules.");
        System.out.println("    ... " + infractionDescription);
        System.out.println("    ... Please debug the implementation of " + infractor.displayName);
        System.out.println("    ... This simulation will now end.");
        System.exit(1);
    }

    // Function to notify players of a recoverable problem, and the method of resolving it which was used.
    public void minorInfraction(SpadesPlayer infractor, String infractionDescription, String resolution) {
        System.out.println("Error! Player " + infractor + " failed to abide by the rules.");
        System.out.println("    ... " + infractionDescription);
        System.out.println("    ... Please debug the implementation of " + infractor.displayName);
        System.out.println("    ... " + resolution);
        System.out.println("    ... This simulation will continue.");    
    }

}
