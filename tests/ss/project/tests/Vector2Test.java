package ss.project.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.Vector3;

import static org.junit.Assert.*;

public class Vector2Test {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void newVector3() {
        Vector2 newVector = new Vector2(0, 0);
        assertNotNull("New vector should not be null.", newVector);

        assertEquals("x should be equal to 0.", newVector.getX(), 0);
        assertEquals("y should be equal to 0.", newVector.getY(), 0);

        newVector = new Vector2(1, 2);
        assertNotNull("New vector should not be null.", newVector);

        assertEquals("x should be equal to 1.", newVector.getX(), 1);
        assertEquals("y should be equal to 2.", newVector.getY(), 2);
    }

    @Test
    public void add() {
        Vector2 vector = new Vector2(1, 1);
        Vector2 anotherVector = new Vector2(2, 2);
        Assert.assertEquals(new Vector2(3, 3), vector.add(anotherVector));
    }

    @Test
    public void add2() {
        Vector2 vector = new Vector2(1, 1);
        Assert.assertEquals(new Vector2(3, 3), vector.add(2, 2));
    }

    @Test
    public void checkToString() {
        Vector2 vector = new Vector2(1, 2);
        String result = vector.toString();
        Assert.assertTrue(result.contains(1 + ""));
        Assert.assertTrue(result.contains(2 + ""));
        Assert.assertFalse(result.contains(3 + ""));
    }

    @Test
    public void staticVectorOne() {
        assertNotNull("Static vector should not be null.", Vector2.ONE);

        assertEquals("x should be equal to 1.", Vector2.ONE.getX(), 1);
        assertEquals("y should be equal to 1.", Vector2.ONE.getY(), 1);
    }

    @Test
    public void staticVectorZero() {
        assertNotNull("Static vector should not be null.", Vector2.ZERO);

        assertEquals("x should be equal to 0.", Vector2.ZERO.getX(), 0);
        assertEquals("y should be equal to 0.", Vector2.ZERO.getY(), 0);
    }

    @Test
    public void testEquals() {
        Vector2 staticVector = Vector2.ZERO;

        assertTrue(staticVector.equals(staticVector));
        assertTrue(staticVector.equals(Vector2.ZERO));
        assertTrue(staticVector.equals(new Vector2(0, 0)));

        assertFalse(staticVector.equals(new Vector2(1, 0)));
        assertFalse(staticVector.equals(new Vector2(0, 1)));
        assertFalse(staticVector.equals(new Vector2(1, 1)));
        assertFalse(staticVector.equals(null));

        assertNotEquals(staticVector, new Vector3(0, 0, 1));
        assertNotNull(staticVector);
    }
}
