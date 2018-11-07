package controller.command;

import model.DatabaseManager;
import view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Update implements Command {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 6;
    private final View view;
    private final DatabaseManager manager;

    public Update(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("update");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");

        if (isCorrectNumberOfParameters(formatCommand)) {
            String tableName = formatCommand[1];
            String nameOfVerifiableColumn = formatCommand[2];
            String valueOfVerifiableColumn = formatCommand[3];
            List<String> namesOfUpdatableColumns = getNamesOfUpdatableColumns(formatCommand);
            List<String> valuesOfUpdatableColumns = getValuesOfUpdatableColumns(formatCommand);
            try {
                manager.update(tableName, nameOfVerifiableColumn, valueOfVerifiableColumn, namesOfUpdatableColumns, valuesOfUpdatableColumns);
                new Find(this.view, this.manager).execute("find|" + tableName);
            } catch (SQLException e) {
                view.write(e.getMessage());
                view.write(CommandMessages.ENTER_NEXT_COMMAND);
            }
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private ArrayList<String> getNamesOfUpdatableColumns(String[] formatCommand) {
        ArrayList<String> namesOfUpdatableColumns = new ArrayList<>();
        for (int i = 4; i < formatCommand.length; i += 2) {
            namesOfUpdatableColumns.add(formatCommand[i]);
        }
        return namesOfUpdatableColumns;
    }

    private ArrayList<String> getValuesOfUpdatableColumns(String[] formatCommand) {
        ArrayList<String> valuesOfUpdatableColumns = new ArrayList<>();
        for (int i = 5; i < formatCommand.length; i += 2) {
            valuesOfUpdatableColumns.add(formatCommand[i]);
        }
        return valuesOfUpdatableColumns;
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
