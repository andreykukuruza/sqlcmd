package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

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
            List<String> columnNames = getColumnNames(formatCommand);
            List<String> columnValues = getColumnValues(formatCommand);
            executeInsert(formatCommand[1], columnNames, columnValues);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeInsert(String tableName, List<String> columnNames, List<String> columnValues) {
        try {
            manager.insert(tableName, columnNames, columnValues);
            view.write("Data was successful insert in the table.");
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }

    private List<String> getColumnValues(String[] formatCommand) {
        ArrayList<String> columnValues = new ArrayList<>();
        for (int i = 3; i < formatCommand.length; i += 2) {
            columnValues.add(formatCommand[i]);
        }
        return columnValues;
    }

    private List<String> getColumnNames(String[] formatCommand) {
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
