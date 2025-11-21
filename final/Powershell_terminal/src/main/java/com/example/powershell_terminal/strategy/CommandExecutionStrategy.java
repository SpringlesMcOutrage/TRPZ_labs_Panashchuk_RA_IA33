package com.example.powershell_terminal.strategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CommandExecutionStrategy implements ExecutionStrategy {

    public String execute(String command, String currentDirectory) {
        System.out.println("Executing PowerShell command: " + command);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", command);
            processBuilder.directory(new File(currentDirectory));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output;
            } else {
                return "Error: Command failed. Output: " + output;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while executing PowerShell command: " + e.getMessage();
        }
    }
}
