package com.example.powershell_terminal.command;

import com.example.powershell_terminal.model.Tab;

public class CommandInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        } else {
            throw new IllegalStateException("No command set");
        }
    }
    public Tab executeAndReturn() {
        if (command instanceof CreateTabCommand createCommand) {
            executeCommand();
            return createCommand.getResult();
        }
        throw new UnsupportedOperationException("Command does not return a result");
    }
}
