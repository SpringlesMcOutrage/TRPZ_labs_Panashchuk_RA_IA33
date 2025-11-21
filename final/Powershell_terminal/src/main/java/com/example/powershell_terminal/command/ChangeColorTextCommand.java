package com.example.powershell_terminal.command;

import com.example.powershell_terminal.services.TabService;

public class ChangeColorTextCommand implements Command {
    private TabService tabService;
    private Long tabId;
    private String newColor;

    public ChangeColorTextCommand(TabService tabService, Long tabId, String newColor) {
        this.tabService = tabService;
        this.tabId = tabId;
        this.newColor = newColor;
    }

    public void execute() {
        tabService.changeTextColor(tabId, newColor);
    }
}
