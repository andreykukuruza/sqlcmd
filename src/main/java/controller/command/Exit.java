package controller.command;

import model.DatabaseManager;
import view.View;

public class Exit implements Command {
    private View view;
    private DatabaseManager manager;

    public Exit(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals("exit");
    }

    @Override
    public void execute(String command) {
        try {
            manager.exit();
            view.write("Goodbye!");
        } catch (Exception e) {
            view.write(e.getMessage() + " Goodbye!");
        }
    }
}
