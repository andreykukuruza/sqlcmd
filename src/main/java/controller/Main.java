package controller;

import model.JDBCDatabaseManager;
import view.Console;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Console(), new JDBCDatabaseManager());
        controller.run();
    }
}
