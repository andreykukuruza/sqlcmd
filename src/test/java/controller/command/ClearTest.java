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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClearTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Clear clear = new Clear(view, manager);

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(clear.canExecute("clear"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(clear.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() throws SQLException {
//        given
        String tableName = "tableName";
        String command = "clear|" + tableName;
        doNothing().when(manager).clear(tableName);
        doNothing().when(view).write(anyString());
//        when
        clear.execute(command);
//        then
        verify(manager, times(1)).clear(tableName);
        verify(view, times(1))
                .write("Table " + tableName + " was cleared. Enter next command or help:");
    }

    @Test
    void executeTest_WithoutIncorrectNumberOfParameters() {
//        given
        doNothing().when(view).write(anyString());
//        when
        clear.execute("clear|tableName|excessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutTableName() {
//        given
        doNothing().when(view).write(anyString());
//        when
        clear.execute("clear");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithIncorrectTableName() throws SQLException {
//        given
        String tableName = "tableName";
        String command = "clear|" + tableName;
        SQLException e = new SQLException("Input data does not correct.");
        doThrow(e).when(manager).clear(tableName);
        doNothing().when(view).write(anyString());
//        when
        clear.execute(command);
//        then
        verify(manager, times(1)).clear(tableName);
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
