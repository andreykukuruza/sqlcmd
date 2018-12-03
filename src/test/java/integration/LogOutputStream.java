package integration;

import java.io.OutputStream;

public class LogOutputStream extends OutputStream {
    private String log;

    @Override
    public void write(int b) {
        if (log == null) {
            log = String.valueOf((char) b);
        } else {
            log += String.valueOf((char) b);
        }
    }

    String getData() {
        return log;
    }

    void resetData() {
        log = null;
    }
}
