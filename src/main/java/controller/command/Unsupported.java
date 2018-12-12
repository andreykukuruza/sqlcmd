package controller.command;

import view.View;

import static controller.command.util.CommandMessages.*;

public class Unsupported extends SystemCommand {
    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canExecute(String command) {
        return true;
    }

    @Override
    public void execute(String command) {
        view.write(WRONG_COMMAND);
    }
}
