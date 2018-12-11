package controller.command;

import model.DatabaseManager;
import view.View;

import static controller.command.util.CommandMessages.*;

public class IsConnected implements Command {
    private View view;
    private DatabaseManager manager;

    public IsConnected(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public String format() {
        throw new UnsupportedOperationException("IsConnected is inner helper command without format");
    }

    @Override
    public String description() {
        throw new UnsupportedOperationException("IsConnected is inner helper command without description");
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
