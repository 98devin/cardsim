
import cardsim.basics.*;
import cardsim.games.spades.*;
import java.util.*;


public class Test {

    public static void main(String[] args) {

        SpadesGame spades = new SpadesGame(
            SpadesPlayerImpls.Random,
            SpadesPlayerImpls.Random,
            SpadesPlayerImpls.Random,
            SpadesPlayerImpls.Random
        );

        System.out.println(spades.game(true));

    }

}