package com.example.powershell_terminal.strategy;

public class ContextStrategy {
    private ExecutionStrategy strategy;

    public void setStrategy(ExecutionStrategy strategy) {
        this.strategy = strategy;
    }
    public String executeStrategy(String command, String directory) {
        return strategy.execute(command, directory);
    }
}
