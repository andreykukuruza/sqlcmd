package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import java.util.LinkedHashMap;
import java.util.Map;

import static controller.command.util.CommandMessages.*;

public class Create implements Command {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        return "Format: create|table Name|column 1|type 1|column 2|type 2|...|column N|type N\n" +
                "\ttable Name - name of new table.\n" +
                "\tcolumn 1|column 2|...|column N - names of columns in new table.\n" +
                "\ttype 1|type 2|...|type N - data types of columns in new table.";
    }

    @Override
    public String description() {
        return "You can use it for creating the table with any number of columns.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(CREATE);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (isCorrectNumberOfParameters(formatCommand)) {
            executeCreate(formatCommand[1], getColumnNameToColumnType(formatCommand));
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeCreate(String tableName, Map<String, String> columnNameToColumnType) {
        try {
            manager.create(tableName, columnNameToColumnType);
            view.write(String.format(CREATE_SUCCESSFUL, tableName));
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }

    private Map<String, String> getColumnNameToColumnType(String[] formatCommand) {
        Map<String, String> columnNameToColumnType = new LinkedHashMap<>();
        for (int i = 2; i < formatCommand.length; i += 2) {
            columnNameToColumnType.put(formatCommand[i], formatCommand[i + 1]);
        }
        return columnNameToColumnType;
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
