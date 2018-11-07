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
        view.write("\t\tFormat: create|table Name|column 1|type 1|column 2|type 2|...|column N|type N");
        view.write("\t\t\"table Name\" - name of new table.");
        view.write("\t\t\"column 1|column 2|...|column N\" - names of columns in new table.");
        view.write("\t\t\"type 1|type 2|...|type N\" - data types of columns in new table.");
        view.write("");

        view.write("\t- find");
        view.write("\t\tYou can use it for watching data in the table.");
        view.write("\t\tFormat: find|table name");
        view.write("");

        view.write("\t- insert");
        view.write("\t\tYou can use it for inserting the row in the table.");
        view.write("\t\tFormat: insert|table name|column 1|value 1|column 2|value 2|...|column N|value N");
        view.write("\t\t\"column 1\" - name of the first column, \"value 1\" - value of the first column");
        view.write("\t\t\"column 2\" - name of the second column, \"value 2\" - value of the second column");
        view.write("\t\t\"column N\" - name of the N column, \"value N\" - value of the N column");
        view.write("\t\tP.S. If one or more of your values are text, you need use special symbol '.");
        view.write("\t\tExample, insert|users|name|'some text'|password|'another text'");
        view.write("");

        view.write("\t- update");
        view.write("\t\tYou can use it for updating data in columns.");
        view.write("\t\tFormat: update|table name|name of verifiable column|value of verifiable column|name of updatable column 1|value of updatable column 1|...|name of updatable column N|value of updatable column N|");
        view.write("");
//      TODO To finish "help" command
        view.write("\t- delete");
        view.write("\t\tTODO");
        view.write("");

        view.write("\t- help");
        view.write("\t\tYou can use it for watching all commands and their describes in SQLCmd.");
        view.write("\t\tFormat: help");
        view.write("");

        view.write("\t- exit");
        view.write("\t\tYou can use it for correct exiting from SQLCmd.");
        view.write("\t\tFormat: exit");
        view.write("");

        view.write("Enter next command:");
    }
}
