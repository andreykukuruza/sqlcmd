package controller.command;

import model.DatabaseManager;
import view.View;

import java.sql.SQLException;
import java.util.List;

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
            List<String> tableNames = manager.tables();
            if (tableNames.size() == 0) {
                view.write("No tables inside this database.");
                view.write(CommandMessages.ENTER_NEXT_COMMAND);
            } else {
                view.write(tableNames.toString());
                view.write(CommandMessages.ENTER_NEXT_COMMAND);
            }
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
