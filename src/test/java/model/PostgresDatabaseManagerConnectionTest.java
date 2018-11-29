package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PostgresDatabaseManagerConnectionTest {
    private PostgresDatabaseManager manager;

    @BeforeEach
    void setUp() {
        manager = new PostgresDatabaseManager();
    }

    @AfterEach
    void tearDown() throws SQLException {
        manager.exit();
    }

    @Test
    void connectWithAllWrongParametersTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                "WrongDatabaseName",
                "WrongUserName",
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongPasswordTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                Config.getCorrectDatabaseName(),
                Config.getCorrectUserName(),
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongUserNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                Config.getCorrectDatabaseName(),
                "WrongUserName",
                Config.getCorrectPassword()));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongDatabaseNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                "WrongDatabaseName",
                Config.getCorrectUserName(),
                Config.getCorrectPassword()));
        assertEquals("Database WrongDatabaseName does not exist.", e.getMessage());
    }

    @Test
    void connectWithCorrectParametersTest() {
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
    void connectAfterConnectTest() {
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
    void isConnectedTest() throws SQLException {
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
