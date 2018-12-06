package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void executeTest_WithCorrectParametersInCommand() {
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
//        when
        connect.execute("connect|TestDatabaseName|TestUserName|TestPassword|excessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutPassword() {
//        when
        connect.execute("connect|TestDatabaseName|TestUserName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutPasswordAndUsername() {
//        when
        connect.execute("connect|TestDatabaseName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutParameters() {
//        when
        connect.execute("connect");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithIncorrectDatabaseName() {
//        given
        doThrow(new DatabaseManagerException("Database WrongDatabaseName does not exist."))
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
    void executeTest_WithIncorrectPassword() {
//        given
        doThrow(new DatabaseManagerException("Username or password are incorrect."))
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
    void executeTest_WithIncorrectUserName() {
//        given
        doThrow(new DatabaseManagerException("Username or password are incorrect."))
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
