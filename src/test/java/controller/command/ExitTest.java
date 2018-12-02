package controller.command;

import model.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExitTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private Exit exit;

    @Test
    void canExecuteTest_WithCorrectCommandName() {
        assertTrue(exit.canExecute("exit"));
    }

    @Test
    void canExecuteTest_WithIncorrectCommandName() {
        assertFalse(exit.canExecute("WrongCommandName"));
    }

    @Test
    void executeTest_WithCorrectConnectionClosing() {
//        when
        exit.execute("exit");
//        then
        verify(view, times(1)).write("Goodbye!");
    }

    @Test
    void executeTest_WithException() throws SQLException {
//        given
        doThrow(new SQLException("Connection was not close.")).when(manager).exit();
//        when
        exit.execute("exit");
//        then
        verify(view, times(1)).write("Connection was not close. Goodbye!");
    }
}
