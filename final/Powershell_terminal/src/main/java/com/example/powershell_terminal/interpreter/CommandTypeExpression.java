package com.example.powershell_terminal.interpreter;

import com.example.powershell_terminal.model.Tab;
import com.example.powershell_terminal.services.TabService;
import com.example.powershell_terminal.strategy.CommandExecutionStrategy;
import com.example.powershell_terminal.strategy.ContextStrategy;
import com.example.powershell_terminal.strategy.FileExecutionStrategy;

import java.io.File;

public class CommandTypeExpression implements Expression {

    private ContextStrategy contextStrategy;
    private DirectoryCommandHandler directoryCommandHandler;

    public CommandTypeExpression(ContextStrategy contextStrategy, TabService tabService) {
        this.contextStrategy = contextStrategy;
        this.directoryCommandHandler = new DirectoryCommandHandler(tabService);
    }

    public String interpret(String context, Tab tab) {
        String cleanedCommand = context.trim();

        if (cleanedCommand.toLowerCase().startsWith("cd") ||
                cleanedCommand.toLowerCase().startsWith("set-location") ||
                cleanedCommand.matches("^[a-zA-Z]:$") ||
                new File(cleanedCommand).exists()) {
            return directoryCommandHandler.handleDirectoryCommand(cleanedCommand, tab);
        }

        if (cleanedCommand.endsWith(".exe") || cleanedCommand.endsWith(".bat") || cleanedCommand.endsWith(".ps1")) {
            System.out.println("Command is an executable file: " + context);
            contextStrategy.setStrategy(new FileExecutionStrategy());
        } else {
            System.out.println("Command is assumed to be a PowerShell command: " + context);
            contextStrategy.setStrategy(new CommandExecutionStrategy());
        }

        return contextStrategy.executeStrategy(cleanedCommand, tab.getCurrentDirectory());
    }
}