package controller.command;

import model.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsConnectedTest {
    @Mock
    private View view;
    @Mock
    private DatabaseManager manager;
    @InjectMocks
    private IsConnected isConnected;

    @Test
    void canExecuteTest_IfConnected() {
//        given
        when(manager.isConnected()).thenReturn(true);
//        then
        assertFalse(isConnected.canExecute("isConnected"));
    }

    @Test
    void canExecuteTest_IfNotConnected() {
//        given
        when(manager.isConnected()).thenReturn(false);
//        then
        assertTrue(isConnected.canExecute("isConnected"));
    }

    @Test
    void executeTest() {
//        when
        isConnected.execute("isConnected");
//        then
        verify(view)
                .write("You can't use this command before connect to database. Enter next command or help:");
    }

    @Test
    void formatTest() {
//        when
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class,
                () -> isConnected.format());
//        then
        assertEquals("IsConnected is inner helper command without format and description", e.getMessage());
    }

    @Test
    void descriptionTest() {
//        when
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class,
                () -> isConnected.description());
//        then
        assertEquals("IsConnected is inner helper command without format and description", e.getMessage());
    }
}
