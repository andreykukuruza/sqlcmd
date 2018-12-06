package controller.command;

import controller.command.util.CommandMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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
        verify(view, times(1)).write("We have commands:\n" +
                "    - connect\n" +
                "        You can use it for connection to database.\n" +
                "        Format: connect|database name|username|password\n" +
                "\n" +
                "    - tables\n" +
                "        You can use it for watching all tables in database.\n" +
                "        Format: tables\n" +
                "\n" +
                "    - clear\n" +
                "        You can use it for clearing all data in the table.\n" +
                "        Format: clear|table name\n" +
                "\n" +
                "    - drop\n" +
                "        You can use it for deleting the table.\n" +
                "        Format: drop|table name\n" +
                "\n" +
                "    - create\n" +
                "        You can use it for creating the table with any number of columns.\n" +
                "        Format: create|table Name|column 1|type 1|column 2|type 2|...|column N|type N\n" +
                "        \"table Name\" - name of new table.\n" +
                "        \"column 1|column 2|...|column N\" - names of columns in new table.\n" +
                "        \"type 1|type 2|...|type N\" - data types of columns in new table.\n" +
                "\n" +
                "    - find\n" +
                "        You can use it for watching data in the table.\n" +
                "        Format: find|table name\n" +
                "\n" +
                "    - insert\n" +
                "        You can use it for inserting the row in the table.\n" +
                "        Format: insert|table name|column 1|value 1|column 2|value 2|...|column N|value N\n" +
                "        \"column 1\" - name of the first column, \\\"value 1\\\" - value of the first column\n" +
                "        \"column 2\" - name of the second column, \\\"value 2\\\" - value of the second column\n" +
                "        \"column N\" - name of the N column, \\\"value N\\\" - value of the N column\n" +
                "        P.S. If one or more of your values are text, you need to use special symbol '.\n" +
                "        Example, insert|users|name|'some text'|password|'another text'\n" +
                "\n" +
                "    - update\n" +
                "        You can use it for updating data in columns.\n" +
                "        Format: update|table name|name of verifiable column|value of verifiable column|" +
                "name of updatable column 1|value of updatable column 1|...|" +
                "name of updatable column N|value of updatable column N\n" +
                "\n" +
                "    - delete\n" +
                "        You can use it for deleting data in the table with parameters.\n" +
                "        Format: delete|table name|name of verifiable column|value of verifiable column\n" +
                "\n" +
                "    - help\n" +
                "        You can use it for watching all commands and their describes in SQLCmd.\n" +
                "        Format: help\n" +
                "\n" +
                "    - exit\n" +
                "        You can use it for correct exiting from SQLCmd.\n" +
                "        Format: exit\n");
        verify(view, times(1)).write(CommandMessages.ENTER_NEXT_COMMAND);
    }
}
