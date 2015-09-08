package Test;

import Server.MSNServer;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dennis
 */
public class ServerTest {

    public MSNServer server = new MSNServer();

    public ServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void isServerRunning() {
        assertTrue(server.getRunning());
    }
    
    @Test
    public void stopSever() {
        server.stopServer();
        assertFalse(server.getRunning());
    }

    @Test
    public void getConnection() {

    }

}
