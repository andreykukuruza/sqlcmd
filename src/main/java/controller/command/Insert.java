package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import java.util.HashMap;
import java.util.Map;

import static controller.command.util.CommandMessages.*;

public class Insert implements Command {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 4;
    private final View view;
    private final DatabaseManager manager;

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        return "Format: insert|table name|column 1|value 1|column 2|value 2|...|column N|value N\n" +
                "\tcolumn 1 - name of the first column\n" +
                "\tvalue 1 - value of the first column\n" +
                "\tcolumn 2 - name of the second column\n" +
                "\tvalue 2 - value of the second column\n" +
                "\tcolumn N - name of the N column\n" +
                "\tvalue N - value of the N column";
    }

    @Override
    public String description() {
        return "You can use it for inserting the row in the table.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(INSERT);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (isCorrectNumberOfParameters(formatCommand)) {
            Map<String, String> columnNameToColumnValue = getColumnNameToColumnValue(formatCommand);
            executeInsert(formatCommand[1], columnNameToColumnValue);
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeInsert(String tableName, Map<String, String> columnNameToColumnValue) {
        try {
            manager.insert(tableName, columnNameToColumnValue);
            view.write(INSERT_SUCCESSFUL);
            view.write(ENTER_NEXT_COMMAND);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
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
