package controller.command;

import controller.command.util.CommandMessages;
import view.View;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        Path path = Paths.get("src/main/resources/help.txt");
        try {
            List<String> help = Files.readAllLines(path);
            for (String string: help) {
                view.write(string);
            }
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        } catch (IOException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
