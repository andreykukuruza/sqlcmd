package controller.command;

import view.View;

import java.util.List;
import java.util.stream.Collectors;

import static controller.command.util.CommandMessages.*;

public class Help implements Command {
    private View view;
    private List<Command> userCommands;

    public Help(View view, List<Command> allCommands) {
        this.view = view;
        userCommands = allCommands.stream()
                .filter(this::isUserCommand)
                .collect(Collectors.toList());
        userCommands.add(this);
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
        view.write("We have commands:");
        for (Command c : userCommands) {
            view.write("- " + c.getClass().getSimpleName().toLowerCase());
            view.write(c.description());
            view.write(c.format());
            view.write("");
        }
        view.write("P.S. If one or more of your values are text, you need to use special symbol: '\n" +
                "Example, insert|users|name|'some text'|password|'another text'");
        view.write(ENTER_NEXT_COMMAND);
    }

    private boolean isUserCommand(Command c) {
        try {
            c.format();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
