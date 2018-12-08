package controller.command;

import model.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import model.DatabaseManager;
import view.View;

import java.util.*;

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
            Map<String, String> namesToValuesOfUpdatableRow = getNamesToValuesOfUpdatableRow(formatCommand);
            executeUpdate(formatCommand[1], formatCommand[2], formatCommand[3],
                    namesToValuesOfUpdatableRow);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private Map<String, String> getNamesToValuesOfUpdatableRow(String[] formatCommand) {
        Map<String, String> namesToValuesOfUpdatableRow = new HashMap<>();
        for (int i = 4; i < formatCommand.length; i += 2) {
            namesToValuesOfUpdatableRow.put(formatCommand[i], formatCommand[i + 1]);
        }
        return namesToValuesOfUpdatableRow;
    }

    private void executeUpdate(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn,
                               Map<String, String> namesToValuesOfUpdatableRow) {
        try {
            manager.update(tableName, nameOfVerifiableColumn, valueOfVerifiableColumn, namesToValuesOfUpdatableRow);
            new Find(this.view, this.manager).execute("find|" + tableName);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
