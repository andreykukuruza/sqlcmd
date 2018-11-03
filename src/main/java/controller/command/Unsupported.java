package controller.command;

import view.View;

public class Unsupported implements Command {
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
        view.write("Wrong command! Please enter correct command or help:");
    }
}
