package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

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
            List<String> namesOfUpdatableColumns = getNamesOfUpdatableColumns(formatCommand);
            List<String> valuesOfUpdatableColumns = getValuesOfUpdatableColumns(formatCommand);
            executeUpdate(formatCommand[1], formatCommand[2], formatCommand[3],
                    namesOfUpdatableColumns, valuesOfUpdatableColumns);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeUpdate(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn,
                               List<String> namesOfUpdatableColumns, List<String> valuesOfUpdatableColumns) {
        try {
            manager.update(tableName, nameOfVerifiableColumn, valueOfVerifiableColumn,
                    namesOfUpdatableColumns, valuesOfUpdatableColumns);
            new Find(this.view, this.manager).execute("find|" + tableName);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }

    private List<String> getNamesOfUpdatableColumns(String[] formatCommand) {
        ArrayList<String> namesOfUpdatableColumns = new ArrayList<>();
        for (int i = 4; i < formatCommand.length; i += 2) {
            namesOfUpdatableColumns.add(formatCommand[i]);
        }
        return namesOfUpdatableColumns;
    }

    private List<String> getValuesOfUpdatableColumns(String[] formatCommand) {
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
