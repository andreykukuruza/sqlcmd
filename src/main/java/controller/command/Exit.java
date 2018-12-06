package controller.command;

import controller.command.exception.DatabaseManagerException;
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
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage() + " Goodbye!");
        }
    }
}
