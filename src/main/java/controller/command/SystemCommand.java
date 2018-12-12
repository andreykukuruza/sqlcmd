package controller.command;

import static controller.command.util.CommandMessages.UNSUPPORTED_OPERATION_ERR_MSG;

public abstract class SystemCommand implements Command {
    @Override
    public String format() {
        throw new UnsupportedOperationException(
                String.format(UNSUPPORTED_OPERATION_ERR_MSG, this.getClass().getSimpleName()));
    }

    @Override
    public String description() {
        throw new UnsupportedOperationException(
                String.format(UNSUPPORTED_OPERATION_ERR_MSG, this.getClass().getSimpleName()));
    }
}
