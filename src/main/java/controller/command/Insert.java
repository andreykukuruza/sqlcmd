package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

import java.util.HashMap;
import java.util.Map;

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
            Map<String, String> columnNameToColumnValue = getColumnNameToColumnValue(formatCommand);
            executeInsert(formatCommand[1], columnNameToColumnValue);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeInsert(String tableName, Map<String, String> columnNameToColumnValue) {
        try {
            manager.insert(tableName, columnNameToColumnValue);
            view.write("Data was successful insert in the table.");
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }

    private Map<String, String> getColumnNameToColumnValue(String[] formatCommand) {
        Map<String, String> columnNameToColumnValue = new HashMap<>();
        for (int i = 2; i < formatCommand.length; i += 2) {
            columnNameToColumnValue.put(formatCommand[i], formatCommand[i + 1]);
        }
        return columnNameToColumnValue;
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
