package controller.command;

import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Create implements Command {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("create");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");

        if (isCorrectNumberOfParameters(formatCommand)) {
            String tableName = formatCommand[1];
            List<String> namesOfColumns = getNamesOfColumns(formatCommand);
            List<String> typesOfColumns = getTypesOfColumns(formatCommand);

            try {
                manager.create(tableName, namesOfColumns, typesOfColumns);
                view.write("Table " + tableName + " was created. Enter next command or help:");
            } catch (SQLException e) {
                view.write(e.getMessage());
                view.write(CommandMessages.ENTER_NEXT_COMMAND);
            }
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private ArrayList<String> getTypesOfColumns(String[] formatCommand) {
        ArrayList<String> typesOfColumns = new ArrayList<>();
        for (int i = 3; i < formatCommand.length; i += 2) {
            typesOfColumns.add(formatCommand[i]);
        }
        return typesOfColumns;
    }

    private ArrayList<String> getNamesOfColumns(String[] formatCommand) {
        ArrayList<String> namesOfColumns = new ArrayList<>();
        for (int i = 2; i < formatCommand.length; i += 2) {
            namesOfColumns.add(formatCommand[i]);
        }
        return namesOfColumns;
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
