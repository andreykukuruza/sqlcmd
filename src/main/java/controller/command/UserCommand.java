package controller.command;

import controller.command.util.InfoLoader;

public abstract class UserCommand implements Command {
    private static final InfoLoader loader = new InfoLoader();

    @Override
    public String format() {
        return loader.getFormat(this);
    }

    @Override
    public String description() {
        return loader.getDescription(this);
    }
}
