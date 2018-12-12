package controller.command.util;

import controller.command.Command;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class InfoLoader {
    private static final String PATH_TO_INFO = "src/main/resources/Info.properties";
    private static final String FORMAT = ".format";
    private static final String DESCRIPTION = ".description";

    public String getFormat(Command c) {
        return load(String.format("%s%s", c.getClass().getSimpleName(), FORMAT));
    }

    public String getDescription(Command c) {
        return load(String.format("%s%s", c.getClass().getSimpleName(), DESCRIPTION));
    }

    private String load(String key) {
        try (BufferedInputStream inStream = new BufferedInputStream(Files.newInputStream(Paths.get(PATH_TO_INFO)))) {
            Properties properties = new Properties();
            properties.load(inStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(String.format("File not found. Check it: %s", PATH_TO_INFO), e);
        }
    }
}
