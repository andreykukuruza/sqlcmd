package controller.command;

import model.exception.DatabaseManagerException;
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
class DropTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Drop drop;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(drop.canExecute("drop"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(drop.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() {
//        when
        drop.execute("drop|TableName");
//        then
        verify(manager, times(1)).drop("TableName");
        verify(view, times(1))
                .write("Table TableName was dropped. Enter next command or help:");
    }

    @Test
    void executeTest_WithExcessParameter() {
//        when
        drop.execute("drop|TableName|ExcessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutTableName() {
//        when
        drop.execute("drop");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithIncorrectTableName() {
//        given
        String tableName = "tableName";
        DatabaseManagerException e = new DatabaseManagerException("Input data does not correct.");
        doThrow(e).when(manager).drop(tableName);
//        when
        drop.execute("drop|" + tableName);
//        then
        verify(manager, times(1)).drop(tableName);
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
