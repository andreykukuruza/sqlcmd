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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsertTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Insert insert;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(insert.canExecute("insert"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(insert.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() throws SQLException {
//        when
        insert.execute("insert|TableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1))
                .insert("TableName",
                        Arrays.asList("ColumnName1", "ColumnName2"),
                        Arrays.asList("ColumnValue1", "ColumnValue2"));
        verify(view, times(1)).write("Data was successful insert in the table.");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithDifferentNumberOfColumnsNamesAndValues() {
//        when
        insert.execute("insert|TableName|ColumnName1|ColumnValue1|ColumnName2");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutColumnsNamesAndValues() {
//        when
        insert.execute("insert|TableName");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithAnyException() throws SQLException {
//        given
        SQLException e = new SQLException("Any exception");
        doThrow(e).when(manager).insert("WrongTableName",
                Arrays.asList("ColumnName1", "ColumnName2"),
                Arrays.asList("ColumnValue1", "ColumnValue2"));
//        when
        insert.execute("insert|WrongTableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1)).insert("WrongTableName",
                Arrays.asList("ColumnName1", "ColumnName2"),
                Arrays.asList("ColumnValue1", "ColumnValue2"));
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
