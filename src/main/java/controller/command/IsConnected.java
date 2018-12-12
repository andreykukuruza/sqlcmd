package controller.command;

import model.DatabaseManager;
import view.View;

import static controller.command.util.CommandMessages.*;

public class IsConnected extends SystemCommand {
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
        view.write(NEED_CONNECT);
    }
}
