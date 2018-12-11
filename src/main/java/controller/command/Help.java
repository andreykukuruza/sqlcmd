package controller.command;

import view.View;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static controller.command.util.CommandMessages.*;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public String format() {
        return "Format: help";
    }

    @Override
    public String description() {
        return "You can use it for watching all commands and their describes in SQLCmd.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals(HELP);
    }

    @Override
    public void execute(String command) {
        try {
            view.write(new String(Files.readAllBytes(Paths.get(PATH_TO_HELP_FILE)), StandardCharsets.UTF_8));
            view.write(ENTER_NEXT_COMMAND);
        } catch (IOException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }
}
