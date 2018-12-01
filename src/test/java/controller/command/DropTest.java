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
class DropTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Drop drop = new Drop(view, manager);

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(drop.canExecute("drop"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(drop.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() throws SQLException {
//        given
        String tableName = "TableName";
        doNothing().when(manager).drop(tableName);
        doNothing().when(view).write("Table " + tableName + " was dropped. Enter next command or help:");
//        when
        drop.execute("drop|" + tableName);
//        then
        verify(manager, times(1)).drop(tableName);
        verify(view, times(1))
                .write("Table " + tableName + " was dropped. Enter next command or help:");
    }

    @Test
    void executeTest_WithExcessParameter() {
//        given
        doNothing().when(view).write(anyString());
//        when
        drop.execute("drop|TableName|ExcessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutTableName() {
//        given
        doNothing().when(view).write(anyString());
//        when
        drop.execute("drop");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithIncorrectTableName() throws SQLException {
//        given
        String tableName = "tableName";
        SQLException e = new SQLException("Input data does not correct.");
        doThrow(e).when(manager).drop(tableName);
        doNothing().when(view).write(anyString());
//        when
        drop.execute("drop|" + tableName);
//        then
        verify(manager, times(1)).drop(tableName);
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
