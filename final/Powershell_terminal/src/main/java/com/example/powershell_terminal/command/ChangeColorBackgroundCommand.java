package com.example.powershell_terminal.command;

import com.example.powershell_terminal.services.TabService;

public class ChangeColorBackgroundCommand implements Command {
    private TabService tabService;
    private Long tabId;
    private String newBackgroundColor;

    public ChangeColorBackgroundCommand(TabService tabService, Long tabId, String newBackgroundColor) {
        this.tabService = tabService;
        this.tabId = tabId;
        this.newBackgroundColor = newBackgroundColor;
    }

    public void execute() {
        tabService.changeBackgroundColor(tabId, newBackgroundColor);
    }
}

