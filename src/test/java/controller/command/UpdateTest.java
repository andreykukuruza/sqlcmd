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
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Update update;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(update.canExecute("update"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(update.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() {
//        given
        String tableName = "TableName";
        when(manager.getColumnsNamesInTable(tableName))
                .thenReturn(new HashSet<>(Arrays.asList("ColumnName1", "ColumnName2")));
        when(manager.getTableData(tableName))
                .thenReturn(Arrays.asList("Value1", "Value2", "Value3", "Value4"));
//        when
        update.execute("update|" + tableName + "|ColumnName1|Value|ColumnName2|Value");
//        then
        verify(manager, times(1)).update(
                tableName, "ColumnName1", "Value",
                Collections.singletonMap("ColumnName2", "Value"));
        verify(manager, times(1)).getColumnsNamesInTable(tableName);
        verify(manager, times(1)).getTableData(tableName);
        verify(view, times(1)).write("+-----------+-----------+\n" +
                "|ColumnName1|ColumnName2|\n" +
                "+-----------+-----------+\n" +
                "|Value1     |Value2     |\n" +
                "|Value3     |Value4     |\n" +
                "+-----------+-----------+");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithDifferentNumberOfNamesAndTypesInUpdatableColumns() {
//        when
        update.execute("update|TableName|VerifiableColumnName|VerifiableValue|ColumnName1|Value|ColumnName2");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithAnyException() {
//        given
        DatabaseManagerException e = new DatabaseManagerException("Any Exception");
        doThrow(e).when(manager).update(anyString(), anyString(),anyString(), anyMap());
//        when
        update.execute("update|TableName|VerifiableColumnName|VerifiableValue|ColumnName1|Value1");
//        then
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
