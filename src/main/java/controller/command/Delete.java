package controller.command;

import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

import java.sql.SQLException;

public class Delete implements Command {
    private static final int CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND = 4;
    private View view;
    private DatabaseManager manager;

    public Delete(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("delete");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeDelete(formatCommand[1], formatCommand[2], formatCommand[3]);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeDelete(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn) {
        try {
            manager.delete(tableName, nameOfVerifiableColumn, valueOfVerifiableColumn);
            new Find(this.view, this.manager).execute("find|" + tableName);
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
