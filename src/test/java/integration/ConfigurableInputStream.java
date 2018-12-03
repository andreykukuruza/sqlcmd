package integration;

import java.io.InputStream;

class ConfigurableInputStream extends InputStream {
    private String line;
    private boolean endLine = false;

    @Override
    public int read() {
        if (line.length() == 0) {
            return -1;
        }
        if (endLine) {
            endLine = false;
            return -1;
        }
        char symbol = line.charAt(0);
        line = line.substring(1);
        if (symbol == '\n') {
            endLine = true;
        }
        return (int) symbol;
    }

    void add(String line) {
        if (this.line == null) {
            this.line = line;
        } else {
            this.line += "\n" + line;
        }
    }
}
