package com.example.powershell_terminal.command;

import com.example.powershell_terminal.services.TabService;

public class CloseTabCommand implements Command {
    private TabService tabService;
    private Long tabId;

    public CloseTabCommand(TabService tabService, Long tabId) {
        this.tabService = tabService;
        this.tabId = tabId;
    }

    public void execute() {
        tabService.closeTab(tabId);
    }
}
