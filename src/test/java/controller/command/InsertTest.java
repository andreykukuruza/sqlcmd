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

import java.util.HashMap;

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
    void executeTest_WithCorrectParametersInCommand() {
//        when
        insert.execute("insert|TableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1))
                .insert("TableName",
                        new HashMap<String, String>() {{
                            put("ColumnName1", "ColumnValue1");
                            put("ColumnName2", "ColumnValue2");
                        }}
                );
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
    void executeTest_WithAnyException() {
//        given
        DatabaseManagerException e = new DatabaseManagerException("Any exception");
        doThrow(e).when(manager).insert("WrongTableName",
                new HashMap<String, String>() {{
                    put("ColumnName1", "ColumnValue1");
                    put("ColumnName2", "ColumnValue2");
                }});
//        when
        insert.execute("insert|WrongTableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1)).insert("WrongTableName",
                new HashMap<String, String>() {{
                    put("ColumnName1", "ColumnValue1");
                    put("ColumnName2", "ColumnValue2");
                }});
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
