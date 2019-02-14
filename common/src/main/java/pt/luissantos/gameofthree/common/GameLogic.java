package pt.luissantos.gameofthree.common;


import java.util.Arrays;
import java.util.Optional;

public class GameLogic {

    public static Optional<Integer> calculateNextNumber(Integer currentNumber){

        for(int n : Arrays.asList(-1,0,1)) {

            if( currentNumber+n != 0 && (currentNumber+n) % 3 == 0){
                return Optional.of((currentNumber+n)/3);
            }
        }

       return Optional.empty();
    }

}
