package controller.command;

import model.exception.DatabaseManagerException;
import model.DatabaseManager;
import view.View;

import java.util.*;

import static controller.command.util.CommandMessages.*;

public class Update extends UserCommand {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 6;
    private final View view;
    private final DatabaseManager manager;

    public Update(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(UPDATE);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (isCorrectNumberOfParameters(formatCommand)) {
            Map<String, String> namesToValuesOfUpdatableRow = getNamesToValuesOfUpdatableRow(formatCommand);
            executeUpdate(formatCommand[1], formatCommand[2], formatCommand[3],
                    namesToValuesOfUpdatableRow);
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeUpdate(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn,
                               Map<String, String> namesToValuesOfUpdatableRow) {
        try {
            manager.update(tableName, nameOfVerifiableColumn, valueOfVerifiableColumn, namesToValuesOfUpdatableRow);
            new Find(this.view, this.manager).execute(String.format("%s|%s", FIND, tableName));
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }

    private Map<String, String> getNamesToValuesOfUpdatableRow(String[] formatCommand) {
        Map<String, String> namesToValuesOfUpdatableRow = new HashMap<>();
        for (int i = 4; i < formatCommand.length; i += 2) {
            namesToValuesOfUpdatableRow.put(formatCommand[i], formatCommand[i + 1]);
        }
        return namesToValuesOfUpdatableRow;
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
