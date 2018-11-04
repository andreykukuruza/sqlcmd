package controller.command;

import model.DatabaseManager;
import view.View;

import java.sql.SQLException;

public class Connect implements Command {
    private static final int NUMBER_OF_PARAMETERS_IN_COMMAND = 4;
    private View view;
    private DatabaseManager manager;

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("connect");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");

        if (formatCommand.length == NUMBER_OF_PARAMETERS_IN_COMMAND) {
            try {
                manager.connect(formatCommand[1], formatCommand[2], formatCommand[3]);
                view.write("Connect is successful. Enter next command or help:");
            } catch (SQLException e) {
                view.write(e.getMessage());
                view.write("Enter next command or help:");
            }
        } else {
            view.write("Incorrect command format. Try again or enter help:");
        }
    }
}
