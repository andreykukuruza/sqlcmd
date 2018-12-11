package controller.command;

import view.View;

import static controller.command.util.CommandMessages.*;

public class Unsupported implements Command {
    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public String format() {
        throw new UnsupportedOperationException("Unsupported is inner helper command without format");
    }

    @Override
    public String description() {
        throw new UnsupportedOperationException("Unsupported is inner helper command without description");
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
