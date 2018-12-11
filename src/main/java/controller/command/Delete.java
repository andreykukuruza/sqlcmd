package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import static controller.command.util.CommandMessages.*;

public class Delete implements Command {
    private static final int CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND = 4;
    private View view;
    private DatabaseManager manager;

    public Delete(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        return "Format: delete|table name|name of verifiable column|value of verifiable column";
    }

    @Override
    public String description() {
        return "You can use it for deleting data in the table with parameters.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(DELETE);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeDelete(formatCommand[1], formatCommand[2], formatCommand[3]);
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeDelete(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn) {
        try {
            manager.delete(tableName, nameOfVerifiableColumn, valueOfVerifiableColumn);
            new Find(this.view, this.manager).execute(String.format("%s|%s", FIND, tableName));
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }
}
