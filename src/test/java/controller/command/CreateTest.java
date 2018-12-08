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

import java.util.Collections;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Create create;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(create.canExecute("create"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(create.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() {
//        when
        create.execute("create|TableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1))
                .create("TableName",
                        new LinkedHashMap<String, String>() {{
                            put("ColumnName1", "ColumnValue1");
                            put("ColumnName2", "ColumnValue2");
                        }});
        verify(view, times(1)).write("Table TableName was created. Enter next command or help:");
    }

    @Test
    void executeTest_WithDifferentNumberOfColumnsNamesAndTypes() {
//        when
        create.execute("create|TableName|ColumnName1|ColumnValue1|ColumnName2");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithoutColumns() {
//        when
        create.execute("create|TableName");
//        then
        verify(manager, times(1))
                .create("TableName", Collections.emptyMap());
        verify(view, times(1)).write("Table TableName was created. Enter next command or help:");
    }

    @Test
    void executeTest_WithoutTableName() {
//        when
        create.execute("create|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }

    @Test
    void executeTest_WithIncorrectTableName() {
//        given
        DatabaseManagerException e = new DatabaseManagerException("Input data does not correct.");
        LinkedHashMap<String, String> columnNameToColumnType = new LinkedHashMap<String, String>() {{
            put("ColumnName1", "ColumnValue1");
            put("ColumnName2", "ColumnValue2");
        }};
        doThrow(e).when(manager).create("WrongTableName", columnNameToColumnType);
//        when
        create.execute("create|WrongTableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1)).create("WrongTableName", columnNameToColumnType);
        verify(view, times(1)).write(e.getMessage());
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }

    @Test
    void executeTest_WithoutParameters() {
//        when
        create.execute("create");
//        then
        verify(view, times(1)).write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
    }
}
