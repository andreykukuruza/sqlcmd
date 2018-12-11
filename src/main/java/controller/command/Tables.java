package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import java.util.Set;

import static controller.command.util.CommandMessages.*;

public class Tables implements Command {
    private View view;
    private DatabaseManager manager;

    public Tables(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        return "Format: tables";
    }

    @Override
    public String description() {
        return "You can use it for watching all tables in database.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals(TABLES);
    }

    @Override
    public void execute(String command) {
        try {
            Set<String> tableNames = manager.tables();
            writeTables(tableNames);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }

    private void writeTables(Set<String> tableNames) {
        if (tableNames.size() == 0) {
            view.write(EMPTY_DATABASE);
            view.write(ENTER_NEXT_COMMAND);
        } else {
            view.write(tableNames.toString());
            view.write(ENTER_NEXT_COMMAND);
        }
    }
}
