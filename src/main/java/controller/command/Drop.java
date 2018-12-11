package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import static controller.command.util.CommandMessages.*;

public class Drop implements Command {
    private static final int CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Drop(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(DROP);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeDrop(formatCommand[1]);
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    @Override
    public String format() {
        return "You can use it for deleting the table.";
    }

    @Override
    public String description() {
        return "Format: drop|table name";
    }

    private void executeDrop(String tableName) {
        try {
            manager.drop(tableName);
            view.write(String.format(DROP_SUCCESSFUL, tableName));
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }
}
