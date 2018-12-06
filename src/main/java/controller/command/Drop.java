package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

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
        return command.startsWith("drop");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeDrop(formatCommand[1]);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeDrop(String tableName) {
        try {
            manager.drop(tableName);
            view.write("Table " + tableName + " was dropped. Enter next command or help:");
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
