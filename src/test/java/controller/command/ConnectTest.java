package controller.command;

import controller.command.util.CommandMessages;
import model.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Connect connect = new Connect(view, manager);

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(connect.canExecute("connect"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(connect.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() throws SQLException {
//        given
        doNothing().when(manager).connect(anyString(), anyString(), anyString());
        doNothing().when(view).write(anyString());
//        when
        connect.execute("connect|TestDatabaseName|TestUserName|TestPassword");
//        then
        verify(manager, times(1))
                .connect("TestDatabaseName", "TestUserName", "TestPassword");
        verify(view, times(1))
                .write("Connect is successful. Enter next command or help:");
    }

    @Test
    void executeTest_WithTooManyParameters() {
//        given
        doNothing().when(view).write(anyString());
//        when
        connect.execute("connect|TestDatabaseName|TestUserName|TestPassword|excessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutPassword() {
//        given
        doNothing().when(view).write(anyString());
//        when
        connect.execute("connect|TestDatabaseName|TestUserName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutPasswordAndUsername() {
//        given
        doNothing().when(view).write(anyString());
//        when
        connect.execute("connect|TestDatabaseName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutParameters() {
//        given
        doNothing().when(view).write(anyString());
//        when
        connect.execute("connect");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithIncorrectDatabaseName() throws SQLException {
//        given
        doNothing().when(view).write(anyString());
        doThrow(new SQLException("Database WrongDatabaseName does not exist."))
                .when(manager)
                .connect("WrongDatabaseName", "TestUserName", "TestPassword");
//        when
        connect.execute("connect|WrongDatabaseName|TestUserName|TestPassword");
//        then
        verify(manager, times(1))
                .connect("WrongDatabaseName", "TestUserName", "TestPassword");
        verify(view, times(1)).write("Database WrongDatabaseName does not exist.");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithIncorrectPassword() throws SQLException {
//        given
        doNothing().when(view).write(anyString());
        doThrow(new SQLException("Username or password are incorrect."))
                .when(manager)
                .connect("TestDatabaseName", "TestUserName", "WrongPassword");
//        when
        connect.execute("connect|TestDatabaseName|TestUserName|WrongPassword");
//        then
        verify(manager, times(1))
                .connect("TestDatabaseName", "TestUserName", "WrongPassword");
        verify(view, times(1)).write("Username or password are incorrect.");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithIncorrectUserName() throws SQLException {
//        given
        doNothing().when(view).write(anyString());
        doThrow(new SQLException("Username or password are incorrect."))
                .when(manager)
                .connect("TestDatabaseName", "WrongUserName", "TestPassword");
//        when
        connect.execute("connect|TestDatabaseName|WrongUserName|TestPassword");
//        then
        verify(manager, times(1))
                .connect("TestDatabaseName", "WrongUserName", "TestPassword");
        verify(view, times(1)).write("Username or password are incorrect.");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}