package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresDatabaseManagerTest {
    private PostgresDatabaseManager manager;
    private String correctDatabaseName = "sqlcmdDB";
    private String correctUserName = "postgres";
    private String correctPassword = "sup3r42pass";

    @BeforeEach
    public void setUp() {
        manager = new PostgresDatabaseManager();
    }

    @AfterEach
    public void tearDown() {
        try {
            manager.exit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connectWithAllWrongParametersTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                "WrongDatabaseName",
                "WrongUserName",
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    public void connectWithWrongPasswordTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                correctDatabaseName,
                correctUserName,
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    public void connectWithWrongUserNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                correctDatabaseName,
                "WrongUserName",
                correctPassword));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    public void connectWithWrongDatabaseNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                "WrongDatabaseName",
                correctUserName,
                correctPassword));
        assertEquals("Database WrongDatabaseName does not exist.", e.getMessage());
    }

    @Test
    public void connectWithCorrectParametersTest() {
        try {
            manager.connect(correctDatabaseName, correctUserName, correctPassword);
        } catch (SQLException e) {
            fail("Fail connect with correct parameters.");
        }
    }

    @Test
    public void connectAfterConnectTest() {
        assertFalse(manager.isConnected());
        try {
            manager.connect(correctDatabaseName, correctUserName, correctPassword);
        } catch (SQLException e) {
            fail("Fail connect with correct parameters.");
        }

        assertTrue(manager.isConnected());
        try {
            manager.connect(correctDatabaseName, correctUserName, correctPassword);
        } catch (SQLException e) {
            fail("Fail connect with correct parameters.");
        }
    }


}
