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
class FindTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Find find;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(find.canExecute("find"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(find.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectTableName() {
//        given
        String tableName = "TableName";
        when(manager.getColumnsNamesInTable(tableName))
                .thenReturn(new HashSet<>(Arrays.asList("ColumnName1", "ColumnName2")));
        when(manager.getTableData(tableName))
                .thenReturn(Arrays.asList("Value1", "Value2", "Value3", "Value4"));
//        when
        find.execute("find|" + tableName);
//        then
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
    void executeTest_WithEmptyDataInTable() {
//        given
        String tableName = "TableName";
        when(manager.getColumnsNamesInTable(tableName))
                .thenReturn(new HashSet<>(Arrays.asList("ColumnName1", "ColumnName2")));
        when(manager.getTableData(tableName))
                .thenReturn(Collections.emptyList());
//        when
        find.execute("find|" + tableName);
//        then
        verify(manager, times(1)).getColumnsNamesInTable(tableName);
        verify(manager, times(1)).getTableData(tableName);
        verify(view, times(1)).write("+-----------+-----------+\n" +
                "|ColumnName1|ColumnName2|\n" +
                "+-----------+-----------+");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithoutColumns() {
//        given
        String tableName = "TableName";
        when(manager.getColumnsNamesInTable(tableName))
                .thenReturn(new HashSet<>(Collections.emptyList()));
        when(manager.getTableData(tableName))
                .thenReturn(Collections.emptyList());
//        when
        find.execute("find|" + tableName);
//        then
        verify(manager, times(1)).getColumnsNamesInTable(tableName);
        verify(manager, times(1)).getTableData(tableName);
        verify(view, times(1)).write("");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithIncorrectTableName() {
//        given
        String tableName = "TableName";
        DatabaseManagerException e = new DatabaseManagerException("Input data does not correct.");
        when(manager.getColumnsNamesInTable(tableName)).thenThrow(e);
//        when
        find.execute("find|" + tableName);
//        then
        verify(view).write(e.getMessage());
        verify(view).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithExcessParameter() {
//        when
        find.execute("find|TableName|ExcessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutTableName() {
//        when
        find.execute("find");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }
}
