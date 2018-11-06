package controller;

import model.PostgresDatabaseManager;
import view.Console;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Console(), new PostgresDatabaseManager());
        controller.run();
    }
}
