package controller.command;

import model.DatabaseManager;
import view.View;

public class Clear implements Command {
    private static final int NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
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

        if (formatCommand.length == NUMBER_OF_PARAMETERS_IN_COMMAND) {
            String tableName = formatCommand[1];
            try {
                manager.clear(tableName);
                view.write("Table " + tableName + " was cleared. Enter next command or help:");
            } catch (Exception e) {
                view.write(e.getMessage());
                view.write("Enter next command or help:");
            }
        } else {
            view.write("Incorrect command format. Try again or enter help:");
        }
    }
}
