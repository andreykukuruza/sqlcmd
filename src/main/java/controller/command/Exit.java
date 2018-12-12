package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import static controller.command.util.CommandMessages.*;

public class Exit extends UserCommand {
    private View view;
    private DatabaseManager manager;

    public Exit(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals(EXIT);
    }

    @Override
    public void execute(String command) {
        try {
            manager.exit();
            view.write(GOODBYE);
        } catch (DatabaseManagerException e) {
            view.write(String.format("%s %s", e.getMessage(), GOODBYE));
        }
    }
}
