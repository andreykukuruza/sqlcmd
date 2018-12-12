package controller.command;

public abstract class SystemCommand implements Command {
    @Override
    public String format() {
        throw new UnsupportedOperationException(
                String.format("%s is inner helper command without format", this.getClass().getSimpleName()));
    }

    @Override
    public String description() {
        throw new UnsupportedOperationException(
                String.format("%s is inner helper command without description", this.getClass().getSimpleName()));
    }
}
