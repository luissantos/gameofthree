package pt.luissantos.gameofthree;

import org.junit.Assert;
import org.junit.Test;
import pt.luissantos.gameofthree.common.GameLogic;

import java.util.Optional;

public class GameLogicTest {

    @Test
    public void testThat6Produces2asNextNumber() {

        Optional<Integer> next = GameLogic.calculateNextNumber(6);

        Assert.assertEquals(new Integer(2),next.get());
    }

    @Test
    public void testThat5Produces2asNextNumber() {

        Optional<Integer> next = GameLogic.calculateNextNumber(5);


        Assert.assertEquals(new Integer(2),next.get());
    }

    @Test
    public void testThat7Produces2asNextNumber() {

        Optional<Integer> next = GameLogic.calculateNextNumber(5);


        Assert.assertEquals(new Integer(2),next.get());
    }

    @Test
    public void testThat2Produces1AsResult() {

        Optional<Integer> next = GameLogic.calculateNextNumber(2);


        Assert.assertEquals(new Integer(1),next.get());
    }

    @Test
    public void testThat1ProducesEmptyResult() {

        Optional<Integer> next = GameLogic.calculateNextNumber(1);


        Assert.assertFalse(next.isPresent());
    }
}
