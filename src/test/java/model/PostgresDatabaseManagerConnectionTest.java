package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresDatabaseManagerConnectionTest {
    private PostgresDatabaseManager manager;

    @BeforeEach
    public void setUp() {
        manager = new PostgresDatabaseManager();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        manager.exit();
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
                Config.getCorrectDatabaseName(),
                Config.getCorrectUserName(),
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    public void connectWithWrongUserNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                Config.getCorrectDatabaseName(),
                "WrongUserName",
                Config.getCorrectPassword()));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    public void connectWithWrongDatabaseNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                "WrongDatabaseName",
                Config.getCorrectUserName(),
                Config.getCorrectPassword()));
        assertEquals("Database WrongDatabaseName does not exist.", e.getMessage());
    }

    @Test
    public void connectWithCorrectParametersTest() {
        try {
            manager.connect(
                    Config.getCorrectDatabaseName(),
                    Config.getCorrectUserName(),
                    Config.getCorrectPassword());
        } catch (SQLException e) {
            fail("Fail connecting with correct parameters.");
        }
    }

    @Test
    public void connectAfterConnectTest() {
        assertFalse(manager.isConnected());
        try {
            manager.connect(
                    Config.getCorrectDatabaseName(),
                    Config.getCorrectUserName(),
                    Config.getCorrectPassword());
        } catch (SQLException e) {
            fail("Fail connecting with correct parameters.");
        }

        assertTrue(manager.isConnected());
        try {
            manager.connect(
                    Config.getCorrectDatabaseName(),
                    Config.getCorrectUserName(),
                    Config.getCorrectPassword());
        } catch (SQLException e) {
            fail("Fail connecting with correct parameters.");
        }
    }

    @Test
    public void isConnectedTest() throws SQLException {
//        given
        manager.connect(
                Config.getCorrectDatabaseName(),
                Config.getCorrectUserName(),
                Config.getCorrectPassword());
//        when
        boolean isConnected = manager.isConnected();
//        then
        assertTrue(isConnected);
    }
}
