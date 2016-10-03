package cardsim.games.spades;

import cardsim.basics.*;
import cardsim.games.spades.players.*;


// This class links together the implementations of spades players
// and the SpadesGame class itself, by enumerating the players which have
// been made and providing a constructor for each of them.
public enum SpadesPlayerImpls {
   /*
   HighCard {
        @Override
        public SpadesPlayer get(Hand hand, long playerID) {
            return new HighCardSpadesPlayer(hand, this.name(), playerID);
        }
    },
    */
    Random {
        @Override
        public SpadesPlayer get(Hand hand, long playerID) {
            return new RandomSpadesPlayer(hand, "Random", playerID);
        }
    };

    // Creates a SpadesPlayer of a type decided by each individual enum member.
    public abstract SpadesPlayer get(Hand hand, long playerID);
    
}