package controller.command;

import controller.command.util.CommandMessages;
import view.View;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals("help");
    }

    @Override
    public void execute(String command) {
        try {
            view.write(new String(Files.readAllBytes(Paths.get("src/main/resources/help.txt")),
                    StandardCharsets.UTF_8));
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        } catch (IOException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
