package ss.project.tests;

import org.junit.Assert;
import org.junit.Test;
import ss.project.shared.model.Connection;

/**
 * Created by simon on 26.01.17.
 */
public class ConnectionTest {
    @Test
    public void validIP() {
        Assert.assertTrue(Connection.validIP("127.0.0.1"));
        Assert.assertFalse(Connection.validIP("127.0.0"));
        Assert.assertTrue(Connection.validIP("localhost"));
        Assert.assertFalse(Connection.validIP("loaclho"));
        Assert.assertFalse(Connection.validIP("123.243.533.533"));
    }
}