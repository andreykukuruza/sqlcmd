package controller.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.View;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnsupportedTest {
    @Mock
    private View view;
    @InjectMocks
    private Unsupported unsupported;

    @Test
    void canExecuteTest() {
        assertTrue(unsupported.canExecute("AnyString"));
    }

    @Test
    void executeTest() {
//        when
        unsupported.execute("anyString");
//        then
        verify(view, times(1))
                .write("Wrong command! Please enter correct command or help:");
    }
}
