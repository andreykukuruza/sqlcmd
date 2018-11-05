package controller;

import controller.command.*;
import model.DatabaseManager;
import view.View;

public class Controller {
    private View view;
    private Command[] commands;

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[] {
                new Connect(view, manager),
                new Exit(view, manager),
                new Help(view),
                new IsConnected(view, manager),
                new Tables(view, manager),
                new Drop(view, manager),
                new Create(view, manager),
                new Clear(view, manager),
                new Unsupported(view)
                /*TODO Init array of all commands. Последовательность имеет значение.
                TODO До isConnected() - функции, которые не зависят от connection, после - которые зависят. Последняя - Unsupported*/};
    }

    public void run() {
        view.write("Hello! It is SQLCmd! Enter command or help:");
        String inputCommand = view.read();
        while (!inputCommand.equals("exit")) {
            for (Command command: commands) {
                if (command.canExecute(inputCommand)) {
                    command.execute(inputCommand);
                    break;
                }
            }
            inputCommand = view.read();
        }
        commands[1].execute("exit"); //Следить за индексом комманды exit в массиве Command[]!!!
    }
}
