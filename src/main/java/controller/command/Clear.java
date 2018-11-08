package controller.command;

import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

import java.sql.SQLException;

public class Clear implements Command {
    private static final int CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Clear(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("clear");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");

        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            String tableName = formatCommand[1];
            try {
                manager.clear(tableName);
                view.write("Table " + tableName + " was cleared. Enter next command or help:");
            } catch (SQLException e) {
                view.write(e.getMessage());
                view.write(CommandMessages.ENTER_NEXT_COMMAND);
            }
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }
}
