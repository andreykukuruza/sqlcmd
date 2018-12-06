package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

import java.util.Set;

public class Tables implements Command {
    private View view;
    private DatabaseManager manager;

    public Tables(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals("tables");
    }

    @Override
    public void execute(String command) {
        try {
            Set<String> tableNames = manager.tables();
            writeTables(tableNames);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }

    private void writeTables(Set<String> tableNames) {
        if (tableNames.size() == 0) {
            view.write("No tables inside the database.");
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        } else {
            view.write(tableNames.toString());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
