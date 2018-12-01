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
import java.util.Collections;

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
    private Create create = new Create(view, manager);

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(create.canExecute("create"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(create.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectParametersInCommand() throws SQLException {
//        when
        create.execute("create|TableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1))
                .create("TableName",
                        Arrays.asList("ColumnName1", "ColumnName2"),
                        Arrays.asList("ColumnValue1", "ColumnValue2"));
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
    void executeTest_WithoutColumns() throws SQLException {
//        when
        create.execute("create|TableName");
//        then
        verify(manager, times(1))
                .create("TableName", Collections.emptyList(), Collections.emptyList());
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
    void executeTest_WithIncorrectTableName() throws SQLException {
//        given
        SQLException e = new SQLException("Input data does not correct.");
        doThrow(e).when(manager).create("WrongTableName",
                Arrays.asList("ColumnName1", "ColumnName2"),
                Arrays.asList("ColumnValue1", "ColumnValue2"));
//        when
        create.execute("create|WrongTableName|ColumnName1|ColumnValue1|ColumnName2|ColumnValue2");
//        then
        verify(manager, times(1)).create("WrongTableName",
                Arrays.asList("ColumnName1", "ColumnName2"),
                Arrays.asList("ColumnValue1", "ColumnValue2"));
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
