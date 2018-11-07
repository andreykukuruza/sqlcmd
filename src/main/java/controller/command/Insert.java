package controller.command;

import model.DatabaseManager;
import view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Insert implements Command {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 4;
    private final View view;
    private final DatabaseManager manager;

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("insert");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");

        if (isCorrectNumberOfParameters(formatCommand)) {
            String tableName = formatCommand[1];
            List<String> columnNames = getColumnNames(formatCommand);
            List<String> columnValues = getColumnValues(formatCommand);
            try {
                manager.insert(tableName, columnNames, columnValues);
                view.write("Data was successful insert in the table.");
            } catch (SQLException e) {
                view.write(e.getMessage());
                view.write(CommandMessages.ENTER_NEXT_COMMAND);
            }
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private ArrayList<String> getColumnValues(String[] formatCommand) {
        ArrayList<String> columnValues = new ArrayList<>();
        for (int i = 3; i < formatCommand.length; i += 2) {
            columnValues.add(formatCommand[i]);
        }
        return columnValues;
    }

    private ArrayList<String> getColumnNames(String[] formatCommand) {
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 2; i < formatCommand.length; i += 2) {
            columnNames.add(formatCommand[i]);
        }
        return columnNames;
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
