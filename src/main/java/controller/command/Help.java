package controller.command;

import view.View;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canExecute(String command) {
        return command.equals("help");
    }

    @Override
    public void execute(String command) {
        view.write("We have commands:");

        view.write("\t- connect");
        view.write("\t\tYou can use it for connection to database.");
        view.write("\t\tFormat: connect|database name|username|password");
        view.write("");

        view.write("\t- tables");
        view.write("\t\tYou can use it for watching all tables in database.");
        view.write("\t\tFormat: tables");
        view.write("");

        view.write("\t- clear");
        view.write("\t\tYou can use it for clearing all data in the table.");
        view.write("\t\tFormat: clear|table name");
        view.write("");

        view.write("\t- drop");
        view.write("\t\tYou can use it for deleting the table.");
        view.write("\t\tFormat: drop|table name");
        view.write("");

        view.write("\t- create");
        view.write("\t\tYou can use it for creating the table with any number of columns.");
        view.write("\t\tFormat: create|column 1|column 2|...|column N");
        view.write("\t\t\"column 1|column 2|...|column N\" - names of columns in new table.");
        view.write("");

        view.write("\t- find");
        view.write("\t\tYou can use it for watching data in the table.");
        view.write("\t\tFormat: find|table name");
        view.write("");

        view.write("\t- insert");
        view.write("\t\tYou can use it for insert the row in the table.");
        view.write("\t\tFormat: insert|table name|column 1|value 1|column 2|value 2|...|column N|value N");
        view.write("\t\t\"column 1\" - name of first column, \"value 1\" - value of first column");
        view.write("\t\t\"column 2\" - name of second column, \"value 2\" - value of second column");
        view.write("\t\t\"column N\" - name of N column, \"value N\" - value of N column");
        view.write("");
//      TODO To finish "help" command
        view.write("\t- update");
        view.write("\t\tTODO");
        view.write("");

        view.write("\t- delete");
        view.write("\t\tTODO");
        view.write("");

        view.write("\t- help");
        view.write("\t\tTODO");
        view.write("");

        view.write("\t- exit");
        view.write("\t\tTODO");
        view.write("");

        view.write("Enter next command:");
    }
}
