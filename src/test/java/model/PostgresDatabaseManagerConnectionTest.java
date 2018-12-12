package model;

import config.Config;
import model.exception.DatabaseManagerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostgresDatabaseManagerConnectionTest {
    private PostgresDatabaseManager manager;

    @BeforeEach
    void setUp() {
        manager = new PostgresDatabaseManager();
    }

    @AfterEach
    void tearDown() {
        manager.exit();
    }

    @Test
    void connectTest_WithAllWrongParameters() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                "WrongDatabaseName",
                "WrongUserName",
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectTest_WithWrongPassword() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                Config.getDatabaseName(),
                Config.getUserName(),
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectTest_WithWrongUserName() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                Config.getDatabaseName(),
                "WrongUserName",
                Config.getPassword()));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectTest_WithWrongDatabaseName() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                "WrongDatabaseName",
                Config.getUserName(),
                Config.getPassword()));
        assertEquals("Database WrongDatabaseName does not exist.", e.getMessage());
    }

    @Test
    void connectTest_WithCorrectParameters() {
        try {
            manager.connect(
                    Config.getDatabaseName(),
                    Config.getUserName(),
                    Config.getPassword());
        } catch (DatabaseManagerException e) {
            fail("Fail connecting with correct parameters.");
        }
    }

    @Test
    void connectTest_AfterConnect() {
        assertFalse(manager.isConnected());
        try {
            manager.connect(
                    Config.getDatabaseName(),
                    Config.getUserName(),
                    Config.getPassword());
        } catch (DatabaseManagerException e) {
            fail("Fail connecting with correct parameters.");
        }

        assertTrue(manager.isConnected());
        try {
            manager.connect(
                    Config.getDatabaseName(),
                    Config.getUserName(),
                    Config.getPassword());
        } catch (DatabaseManagerException e) {
            fail("Fail connecting with correct parameters.");
        }
    }

    @Test
    void isConnectedTest() {
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
