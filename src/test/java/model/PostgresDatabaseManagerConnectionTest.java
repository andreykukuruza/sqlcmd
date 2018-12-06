package model;

import config.Config;
import controller.command.exception.DatabaseManagerException;
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
    void connectWithAllWrongParametersTest() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                "WrongDatabaseName",
                "WrongUserName",
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongPasswordTest() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                Config.getDatabaseName(),
                Config.getUserName(),
                "WrongPassword"));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongUserNameTest() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
                Config.getDatabaseName(),
                "WrongUserName",
                Config.getPassword()));
        assertEquals("Username or password are incorrect.", e.getMessage());
    }

    @Test
    void connectWithWrongDatabaseNameTest() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.connect(
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
        } catch (DatabaseManagerException e) {
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
