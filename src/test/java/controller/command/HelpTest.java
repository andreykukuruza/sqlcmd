package controller.command;

import controller.command.util.CommandMessages;
import model.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Help help = new Help(view, new ArrayList<>(Arrays.asList(
            new Connect(view, manager),
            new Exit(view, manager),
            new IsConnected(view, manager),
            new Tables(view, manager),
            new Find(view, manager),
            new Drop(view, manager),
            new Create(view, manager),
            new Insert(view, manager),
            new Delete(view, manager),
            new Update(view, manager),
            new Clear(view, manager),
            new Unsupported(view))));

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(help.canExecute("help"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(help.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest() {
//        when
        help.execute("help");
//        then
        verify(view, times(11)).write("");

        verify(view, times(1)).write("We have commands:");

        verify(view, times(1)).write("- connect");
        verify(view, times(1)).write("- clear");
        verify(view, times(1)).write("- create");
        verify(view, times(1)).write("- delete");
        verify(view, times(1)).write("- drop");
        verify(view, times(1)).write("- exit");
        verify(view, times(1)).write("- find");
        verify(view, times(1)).write("- help");
        verify(view, times(1)).write("- insert");
        verify(view, times(1)).write("- tables");
        verify(view, times(1)).write("- update");

        verify(view, times(1)).write("You can use it for connection to database.");
        verify(view, times(1)).write("You can use it for correct exiting from SQLCmd.");
        verify(view, times(1)).write("You can use it for watching all tables in database.");
        verify(view, times(1))
                .write("You can use it for watching all commands and their describes in SQLCmd.");
        verify(view, times(1)).write("You can use it for watching data in the table.");
        verify(view, times(1)).write("You can use it for deleting the table.");
        verify(view, times(1))
                .write("You can use it for creating the table with any number of columns.");
        verify(view, times(1)).write("You can use it for inserting the row in the table.");
        verify(view, times(1))
                .write("You can use it for deleting data in the table with parameters.");
        verify(view, times(1)).write("You can use it for updating data in columns.");
        verify(view, times(1)).write("You can use it for clearing all data in the table.");
        verify(view, times(1))
                .write("You can use it for watching all commands and their describes in SQLCmd.");

        verify(view, times(1)).write("Format: connect|database name|username|password");
        verify(view, times(1)).write("Format: exit");
        verify(view, times(1)).write("Format: tables");
        verify(view, times(1)).write("Format: help");
        verify(view, times(1)).write("Format: find|table name");
        verify(view, times(1)).write("Format: drop|table name");
        verify(view, times(1))
                .write("Format: create|table Name|column 1|type 1|column 2|type 2|...|column N|type N\n" +
                "\ttable Name - name of new table.\n" +
                "\tcolumn 1|column 2|...|column N - names of columns in new table.\n" +
                "\ttype 1|type 2|...|type N - data types of columns in new table.");
        verify(view, times(1))
                .write("Format: insert|table name|column 1|value 1|column 2|value 2|...|column N|value N\n" +
                "\tcolumn 1 - name of the first column\n" +
                "\tvalue 1 - value of the first column\n" +
                "\tcolumn 2 - name of the second column\n" +
                "\tvalue 2 - value of the second column\n" +
                "\tcolumn N - name of the N column\n" +
                "\tvalue N - value of the N column");
        verify(view, times(1))
                .write("Format: delete|table name|name of verifiable column|value of verifiable column");
        verify(view, times(1))
                .write("Format: update|table name|name of verifiable column|value of verifiable column|" +
                "name of updatable column 1|value of updatable column 1|...|" +
                "name of updatable column N|value of updatable column N");
        verify(view, times(1)).write("Format: clear|table name");
        verify(view, times(1))
                .write("Format: update|table name|name of verifiable column|value of verifiable column|" +
                "name of updatable column 1|value of updatable column 1|...|" +
                "name of updatable column N|value of updatable column N");

        verify(view, times(1))
                .write("P.S. If one or more of your values are text, you need to use special symbol: '\n" +
                "Example, insert|users|name|'some text'|password|'another text'");

        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
