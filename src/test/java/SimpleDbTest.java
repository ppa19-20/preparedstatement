import java.sql.*;

import com.sun.xml.internal.ws.developer.UsesJAXBContext;

import org.junit.jupiter.api.*;

import static java.sql.DriverManager.getConnection;

/**
 * Created by pwilkin on 27-Apr-20.
 */
public class SimpleDbTest {

    private static final String DBDESC = "jdbc:hsqldb:mem:test";

    @BeforeAll
    public static void prepareDatabase() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.createStatement().execute("CREATE TABLE TESTING (ID INT PRIMARY KEY IDENTITY, TCOL VARCHAR(255), NUM DECIMAL(8, 2))");
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO TESTING (TCOL, NUM) VALUES (?, ?)")) {
                ps.setString(1, "val1");
                ps.setDouble(2, 4.2);
                ps.execute();
                ps.setString(1, "val2");
                ps.setDouble(2, 5.0);
                ps.execute();
                ps.setString(1, "val3");
                ps.setDouble(2, -4.3);
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void destroyDatabase() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.createStatement().execute("DROP TABLE TESTING");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testConnection() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.createStatement().executeQuery("SELECT * FROM TESTING");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThreeEntries() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            int cnt = 0;
            try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM TESTING")) {
                while (rs.next()) {
                    cnt++;
                }
            }
            Assertions.assertEquals(cnt, 3);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
