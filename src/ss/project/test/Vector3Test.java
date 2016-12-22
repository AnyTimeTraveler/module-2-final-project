package ss.project.test;

import org.junit.Before;
import org.junit.Test;
import ss.project.shared.game.Vector3;
import ss.project.shared.game.WorldPosition;

import static org.junit.Assert.*;

public class Vector3Test {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void newVector3() {
        Vector3 newVector = new Vector3(0, 0, 0);
        assertNotNull("New vector should not be null.", newVector);

        assertEquals("x should be equal to 0.", newVector.getX(), 0);
        assertEquals("y should be equal to 0.", newVector.getY(), 0);
        assertEquals("z should be equal to 0.", newVector.getZ(), 0);

        newVector = new Vector3(1, 2, 3);
        assertNotNull("New vector should not be null.", newVector);

        assertEquals("x should be equal to 1.", newVector.getX(), 1);
        assertEquals("y should be equal to 2.", newVector.getY(), 2);
        assertEquals("z should be equal to 3.", newVector.getZ(), 3);
    }

    @Test
    public void staticVectorOne() {
        assertNotNull("Static vector should not be null.", Vector3.ONE);

        assertEquals("x should be equal to 1.", Vector3.ONE.getX(), 1);
        assertEquals("y should be equal to 1.", Vector3.ONE.getY(), 1);
        assertEquals("z should be equal to 1.", Vector3.ONE.getZ(), 1);
    }

    @Test
    public void staticVectorZero() {
        assertNotNull("Static vector should not be null.", Vector3.ZERO);

        assertEquals("x should be equal to 0.", Vector3.ZERO.getX(), 0);
        assertEquals("y should be equal to 0.", Vector3.ZERO.getY(), 0);
        assertEquals("z should be equal to 0.", Vector3.ZERO.getZ(), 0);
    }

    @Test
    public void testEquals() {
        Vector3 staticVector = Vector3.ZERO;

        assertTrue(staticVector.equals(staticVector));
        assertTrue(staticVector.equals(Vector3.ZERO));

        assertFalse(staticVector.equals(new Vector3(1, 0, 0)));
        assertFalse(staticVector.equals(new Vector3(0, 1, 0)));
        assertFalse(staticVector.equals(new Vector3(0, 0, 1)));
        assertFalse(staticVector.equals(new Vector3(1, 1, 1)));

        assertNotEquals(staticVector, new WorldPosition(new Vector3(0, 0, 0)));
        assertNotNull(staticVector);
    }
}
