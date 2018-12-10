package controller.command.util;

public class CommandMessages {
    public static final String INCORRECT_FORMAT_ERR_MSG = "Incorrect command format. Try again or enter help:";
    public static final String ENTER_NEXT_COMMAND = "Enter next command or help:";
    public static final String WRONG_COMMAND = "Wrong command! Please enter correct command or help:";
    public static final String NEED_CONNECT = "You can't use this command before connect to database." +
            " Enter next command or help:";

    public static final String CLEAR = "clear";
    public static final String CLEAR_SUCCESSFUL = "Table %s was cleared. Enter next command or help:";

    public static final String CONNECT = "connect";
    public static final String CONNECT_SUCCESSFUL = "Connect is successful. Enter next command or help:";

    public static final String CREATE = "create";
    public static final String CREATE_SUCCESSFUL = "Table %s was created. Enter next command or help:";

    public static final String DROP = "drop";
    public static final String DROP_SUCCESSFUL = "Table %s was dropped. Enter next command or help:";

    public static final String INSERT = "insert";
    public static final String INSERT_SUCCESSFUL = "Data was successful insert in the table.";

    public static final String TABLES = "tables";
    public static final String EMPTY_DATABASE = "No tables inside the database.";

    public static final String HELP = "help";
    public static final String PATH_TO_HELP_FILE = "src/main/resources/help.txt";

    public static final String EXIT = "exit";
    public static final String GOODBYE = "Goodbye!";

    public static final String DELETE = "delete";
    public static final String FIND = "find";
    public static final String UPDATE = "update";
}
