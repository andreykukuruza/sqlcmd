package controller.command;

import model.DatabaseManager;
import view.View;

import java.util.ArrayList;

public class Create implements Command {
    private static final int MIN_NUMBER_OF_PARAMETERS_IN_COMMAND = 2;
    private View view;
    private DatabaseManager manager;

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return command.startsWith("create");
    }

    @Override
    public void execute(String command) {
        String[] formatCommand = command.split("\\|");

        if (isCorrectNumberOfParameters(formatCommand)) {
            String tableName = formatCommand[1];
            ArrayList<String> namesAndTypesOfColumns = new ArrayList<>();

            for (int i = 2; i < formatCommand.length; i++) {
                namesAndTypesOfColumns.add(formatCommand[i]);
            }
            manager.create(tableName, namesAndTypesOfColumns);
        } else {
            view.write("Incorrect command format. Try again or enter help:");
        }
    }

    private boolean isCorrectNumberOfParameters(String[] formatCommand) {
        return formatCommand.length >= MIN_NUMBER_OF_PARAMETERS_IN_COMMAND && formatCommand.length % 2 == 0;
    }
}
//    String sql = "CREATE TABLE public." + tableName + "(";
//
//        String sql = "CREATE TABLE public.COMPANY " +
//                "(ID INT PRIMARY KEY     NOT NULL," +
//                " NAME           TEXT    NOT NULL, " +
//                " AGE            INT     NOT NULL, " +
//                " ADDRESS        CHAR(50), " +
//                " SALARY         REAL);";