package com.example.powershell_terminal.strategy;

public interface ExecutionStrategy {
    String execute(String command, String directory);
}
