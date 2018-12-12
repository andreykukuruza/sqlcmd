package controller;

import controller.command.*;
import model.DatabaseManager;
import view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static controller.command.util.CommandMessages.*;

public class Controller {
    private View view;
    private List<Command> commands;
    private static final int EXIT_INDEX = 2;

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new ArrayList<>(Arrays.asList(
                new Connect(view, manager),
                new Exit(view, manager),
                new IsConnected(view, manager),
                new Tables(view, manager),
                new Find(view, manager),
                new Drop(view, manager),
                new Create(view, manager),
                new Insert(view, manager),
                new Delete(view, manager),
                new Update(view, manager),
                new Clear(view, manager),
                new Unsupported(view)));
        commands.add(0, new Help(view, commands));
    }

    public void run() {
        view.write("Hello! It is SQLCmd! Enter command or help:");
        String inputCommand = view.read();
        while (!inputCommand.equals(EXIT)) {
            executeCommand(inputCommand);
            inputCommand = view.read();
        }
        commands.get(EXIT_INDEX).execute(EXIT);
    }

    private void executeCommand(String inputCommand) {
        for (Command command : commands) {
            if (command.canExecute(inputCommand)) {
                command.execute(inputCommand);
                break;
            }
        }
    }
}
