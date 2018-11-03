package controller.command;

public interface Command {
    boolean canExecute(String command);
    void execute(String command);
}
