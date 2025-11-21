package com.example.powershell_terminal.command;

import com.example.powershell_terminal.services.TabService;
import com.example.powershell_terminal.model.Tab;

public class CreateTabCommand implements Command {
    private TabService tabService;
    private Tab createdTab;

    public CreateTabCommand(TabService tabService) {
        this.tabService = tabService;
    }

    public void execute() {
        createdTab = tabService.createTab();
    }

    public Tab getResult() {
        return createdTab;
    }
}
