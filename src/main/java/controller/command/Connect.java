package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import static controller.command.util.CommandMessages.*;

public class Connect implements Command {
    private static final int NUMBER_OF_PARAMETERS_IN_COMMAND = 4;
    private View view;
    private DatabaseManager manager;

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        return "Format: connect|database name|username|password";
    }

    @Override
    public String description() {
        return "You can use it for connection to database.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(CONNECT);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeConnect(formatCommand[1], formatCommand[2], formatCommand[3]);
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeConnect(String databaseName, String username, String password) {
        try {
            manager.connect(databaseName, username, password);
            view.write(CONNECT_SUCCESSFUL);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }
}
