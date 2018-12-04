package controller.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HelpTest {
    @Mock
    private View view;
    @InjectMocks
    private Help help;

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
        verify(view, times(1)).write("We have commands:");

        verify(view, times(1)).write("\t- connect");
        verify(view, times(1)).write("\t\tYou can use it for connection to database.");
        verify(view, times(1)).write("\t\tFormat: connect|database name|username|password");
        verify(view, times(11)).write("");

        verify(view, times(1)).write("\t- tables");
        verify(view, times(1)).write("\t\tYou can use it for watching all tables in database.");
        verify(view, times(1)).write("\t\tFormat: tables");

        verify(view, times(1)).write("\t- clear");
        verify(view, times(1)).write("\t\tYou can use it for clearing all data in the table.");
        verify(view, times(1)).write("\t\tFormat: clear|table name");

        verify(view, times(1)).write("\t- drop");
        verify(view, times(1)).write("\t\tYou can use it for deleting the table.");
        verify(view, times(1)).write("\t\tFormat: drop|table name");

        verify(view, times(1)).write("\t- create");
        verify(view, times(1)).write("\t\tYou can use it for creating the table with any number of columns.");
        verify(view, times(1)).write("\t\tFormat: create|table Name|column 1|type 1|column 2|type 2|...|column N|type N");
        verify(view, times(1)).write("\t\t\"column 1|column 2|...|column N\" - names of columns in new table.");
        verify(view, times(1)).write("\t\t\"type 1|type 2|...|type N\" - data types of columns in new table.");

        verify(view, times(1)).write("\t- find");
        verify(view, times(1)).write("\t\tYou can use it for watching data in the table.");
        verify(view, times(1)).write("\t\tFormat: find|table name");

        verify(view, times(1)).write("\t- insert");
        verify(view, times(1)).write("\t\tYou can use it for inserting the row in the table.");
        verify(view, times(1)).write("\t\tFormat: insert|table name|column 1|value 1|column 2|value 2|...|column N|value N");
        verify(view, times(1)).write("\t\t\"column 1\" - name of the first column, \"value 1\" - value of the first column");
        verify(view, times(1)).write("\t\t\"column 2\" - name of the second column, \"value 2\" - value of the second column");
        verify(view, times(1)).write("\t\t\"column N\" - name of the N column, \"value N\" - value of the N column");
        verify(view, times(1)).write("\t\tP.S. If one or more of your values are text, you need to use special symbol '.");
        verify(view, times(1)).write("\t\tExample, insert|users|name|'some text'|password|'another text'");

        verify(view, times(1)).write("\t- update");
        verify(view, times(1)).write("\t\tYou can use it for updating data in columns.");
        verify(view, times(1)).write("\t\tFormat: update|table name|name of verifiable column|value of verifiable column|name of updatable column 1|value of updatable column 1|...|name of updatable column N|value of updatable column N");

        verify(view, times(1)).write("\t- delete");
        verify(view, times(1)).write("\t\tYou can use it for deleting data in the table with parameters.");
        verify(view, times(1)).write("\t\tFormat: delete|table name|name of verifiable column|value of verifiable column");

        verify(view, times(1)).write("\t- help");
        verify(view, times(1)).write("\t\tYou can use it for watching all commands and their describes in SQLCmd.");
        verify(view, times(1)).write("\t\tFormat: help");

        verify(view, times(1)).write("\t- exit");
        verify(view, times(1)).write("\t\tYou can use it for correct exiting from SQLCmd.");
        verify(view, times(1)).write("\t\tFormat: exit");

        verify(view, times(1)).write("Enter next command:");
    }
}
