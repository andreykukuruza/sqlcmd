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

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TablesTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Tables tables;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(tables.canExecute("tables"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(tables.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithSomeTablesInDatabase() {
//        given
        when(manager.tables()).thenReturn(new HashSet<>(Arrays.asList("TableName1", "TableName2")));
//        when
        tables.execute("tables");
//        then
        verify(manager, times(1)).tables();
        verify(view, times(1)).write(contains("TableName1"));
        verify(view, times(1)).write(contains("TableName2"));
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithoutTablesInDatabase() {
//        given
        when(manager.tables()).thenReturn(new HashSet<>());
//        when
        tables.execute("tables");
//        then
        verify(manager, times(1)).tables();
        verify(view, times(1)).write("No tables inside the database.");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithAnyException() {
//        given
        when(manager.tables()).thenThrow(new DatabaseManagerException("Any error message"));
//        when
        tables.execute("tables");
//        then
        verify(manager, times(1)).tables();
        verify(view, times(2)).write(anyString());
    }
}
