package controller.command;

import controller.command.exception.DatabaseManagerException;
import controller.command.util.CommandMessages;
import controller.command.util.TableFormatter;
import model.DatabaseManager;
import view.View;

import java.util.List;
import java.util.Set;

public class Find implements Command {
    private static final int CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Find(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("find");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeFind(formatCommand[1]);
        } else {
            view.write(CommandMessages.INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeFind(String tableName) {
        try {
            Set<String> columnNames = manager.getColumnsNamesInTable(tableName);
            List<String> tableData = manager.getTableData(tableName);
            TableFormatter table = new TableFormatter(columnNames, tableData);
            view.write(table.getTableString());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(CommandMessages.ENTER_NEXT_COMMAND);
        }
    }
}
