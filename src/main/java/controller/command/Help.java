package controller.command;

import view.View;

import java.util.List;
import java.util.stream.Collectors;

import static controller.command.util.CommandMessages.*;

public class Help extends UserCommand {
    private View view;
    private List<Command> userCommands;

    public Help(View view, List<Command> allCommands) {
        this.view = view;
        userCommands = allCommands.stream()
                .filter(c -> c instanceof UserCommand)
                .collect(Collectors.toList());
        userCommands.add(this);
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals(HELP);
    }

    @Override
    public void execute(String command) {
        view.write(HELP_TITLE);
        for (Command c : userCommands) {
            view.write(String.format("- %s", c.getClass().getSimpleName().toLowerCase()));
            view.write(c.description());
            view.write(c.format());
            view.write("");
        }
        view.write(HELP_ADDITIONAL_INFO);
        view.write(ENTER_NEXT_COMMAND);
    }
}
