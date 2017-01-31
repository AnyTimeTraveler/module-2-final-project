package ss.project.tests;

import org.junit.Assert;
import org.junit.Test;
import ss.project.shared.model.Color;

/**
 * Created by simon on 28.01.17.
 */
public class ColorTest {

    @Test
    public void serialize() throws Exception {
        Color color = new Color(80, 50, 25);
        System.out.println(color.getR() + "" + color.getG() + "" + color.getB() + " --> " + color.serialize());
        Assert.assertEquals(color, Color.fromString(color.serialize()));
        System.out.println(color.serialize() + " --> " + color.getR() + "" + color.getG() + "" + color.getB());
    }

}