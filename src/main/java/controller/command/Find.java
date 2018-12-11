package controller.command;

import model.exception.DatabaseManagerException;
import controller.command.util.TableFormatter;
import model.DatabaseManager;
import view.View;

import java.util.List;
import java.util.Set;

import static controller.command.util.CommandMessages.*;

public class Find implements Command {
    private static final int CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Find(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        return "Format: find|table name";
    }

    @Override
    public String description() {
        return "You can use it for watching data in the table.";
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith(FIND);
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");
        if (formatCommand.length == CORRECT_NUMBER_OF_PARAMETERS_IN_COMMAND) {
            executeFind(formatCommand[1]);
        } else {
            view.write(INCORRECT_FORMAT_ERR_MSG);
        }
    }

    private void executeFind(String tableName) {
        try {
            Set<String> columnNames = manager.getColumnsNamesInTable(tableName);
            List<String> tableData = manager.getTableData(tableName);
            TableFormatter table = new TableFormatter(columnNames, tableData);
            view.write(table.getTableString());
            view.write(ENTER_NEXT_COMMAND);
        } catch (DatabaseManagerException e) {
            view.write(e.getMessage());
            view.write(ENTER_NEXT_COMMAND);
        }
    }
}
