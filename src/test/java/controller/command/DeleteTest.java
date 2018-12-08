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
class DeleteTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Delete delete;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(delete.canExecute("delete"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(delete.canExecute("WrongCommandName"));
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
        delete.execute("delete|" + tableName + "|ColumnName1|Value");
//        then
        verify(manager, times(1))
                .delete(tableName, "ColumnName1", "Value");
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
    void executeTest_WithExcessParameter() {
//        when
        delete.execute("delete|TableName|ColumnName|Value|ExcessParameter");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutVerifiableValue() {
//        when
        delete.execute("delete|TableName|ColumnName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutVerifiableNameAndValue() {
//        when
        delete.execute("delete|TableName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutParameters() {
//        when
        delete.execute("delete");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithAnyException() {
//        given
        DatabaseManagerException e = new DatabaseManagerException("Any exception");
        doThrow(e).when(manager)
                .delete("WrongTableName", "ColumnName", "Value");
//        when
        delete.execute("delete|WrongTableName|ColumnName|Value");
//        then
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
