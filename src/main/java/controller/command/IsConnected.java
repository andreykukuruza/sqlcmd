package controller.command;

import model.DatabaseManager;
import view.View;

public class IsConnected implements Command {
    private View view;
    private DatabaseManager manager;

    public IsConnected(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canExecute(String command) {
        return !(manager.isConnected());
    }

    @Override
    public void execute(String command) {
        view.write("You can not use this command before connect to database. Enter next command or help:");
    }
}
