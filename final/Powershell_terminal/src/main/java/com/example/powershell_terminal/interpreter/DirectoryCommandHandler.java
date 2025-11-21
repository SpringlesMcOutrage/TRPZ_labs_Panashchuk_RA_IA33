package com.example.powershell_terminal.interpreter;

import com.example.powershell_terminal.command.CommandInvoker;
import com.example.powershell_terminal.model.Tab;
import com.example.powershell_terminal.services.TabService;

import java.io.File;

public class DirectoryCommandHandler {

    private final TabService tabService;

    public DirectoryCommandHandler(TabService tabService) {
        this.tabService = tabService;
    }

    public String handleDirectoryCommand(String command, Tab tab) {
        String currentDirectory = tab.getCurrentDirectory();

        if (command.matches("^[a-zA-Z]:$")) {
            File newDrive = new File(command + "\\");
            if (newDrive.exists() && newDrive.isDirectory()) {
                tabService.changeDirectory(tab, newDrive.getAbsolutePath());
                return "Changed to drive: " + newDrive.getAbsolutePath();
            } else {
                return "Error: Drive " + command + " does not exist.";
            }
        }

        if (command.toLowerCase().startsWith("cd ") || command.toLowerCase().startsWith("set-location ")) {
            String targetPath = command.substring(command.indexOf(" ") + 1).trim();
            File newDir = resolvePath(targetPath, currentDirectory);

            if (newDir.exists() && newDir.isDirectory()) {
                tabService.changeDirectory(tab, newDir.getAbsolutePath());
                return "Changed directory to: " + newDir.getAbsolutePath();
            } else {
                return "Error: Directory does not exist: " + targetPath;
            }
        }

        File directPath = new File(command);
        if (directPath.exists() && directPath.isDirectory()) {
            tabService.changeDirectory(tab, directPath.getAbsolutePath());
            return "Changed directory to: " + directPath.getAbsolutePath();
        }

        return "Error: Unrecognized or invalid directory command: " + command;
    }

    private static File resolvePath(String path, String currentDirectory) {
        File resolvedPath = new File(path);
        if (!resolvedPath.isAbsolute()) {
            resolvedPath = new File(currentDirectory, path);
        }
        return resolvedPath;
    }
}
