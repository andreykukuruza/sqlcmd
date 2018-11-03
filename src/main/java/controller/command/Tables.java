package controller.command;

import model.DatabaseManager;
import view.View;

import java.util.HashSet;

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
            HashSet<String> tableNames = manager.tables();
            if (tableNames.size() == 0) {
                view.write("No tables inside this database.");
                view.write("Enter next command or help:");
            } else {
                view.write(tableNames.toString());
                view.write("Enter next command or help:");
            }
        } catch (Exception e) {
            view.write(e.getMessage());
            view.write("Enter next command or help:");
        }
    }
}
