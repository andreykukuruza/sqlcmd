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
                Config.getDatabaseName(),
                Config.getUserName(),
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongUserNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                Config.getDatabaseName(),
                "WrongUserName",
                Config.getPassword()));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongDatabaseNameTest() {
        SQLException e = assertThrows(SQLException.class, () -> manager.connect(
                "WrongDatabaseName",
                Config.getUserName(),
                Config.getPassword()));
        assertEquals("Database WrongDatabaseName does not exist.", e.getMessage());
    }

    @Test
    void connectWithCorrectParametersTest() {
        try {
            manager.connect(
                    Config.getDatabaseName(),
                    Config.getUserName(),
                    Config.getPassword());
        } catch (SQLException e) {
            fail("Fail connecting with correct parameters.");
        }
    }

    @Test
    void connectAfterConnectTest() {
        assertFalse(manager.isConnected());
        try {
            manager.connect(
                    Config.getDatabaseName(),
                    Config.getUserName(),
                    Config.getPassword());
        } catch (SQLException e) {
            fail("Fail connecting with correct parameters.");
        }

        assertTrue(manager.isConnected());
        try {
            manager.connect(
                    Config.getDatabaseName(),
                    Config.getUserName(),
                    Config.getPassword());
        } catch (SQLException e) {
            fail("Fail connecting with correct parameters.");
        }
    }

    @Test
    void isConnectedTest() throws SQLException {
//        given
        manager.connect(
                Config.getDatabaseName(),
                Config.getUserName(),
                Config.getPassword());
//        when
        boolean isConnected = manager.isConnected();
//        then
        assertTrue(isConnected);
    }
}
