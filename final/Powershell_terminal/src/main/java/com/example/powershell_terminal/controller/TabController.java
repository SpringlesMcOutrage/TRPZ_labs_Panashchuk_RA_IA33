package com.example.powershell_terminal.controller;


import com.example.powershell_terminal.command.*;
import com.example.powershell_terminal.interpreter.ApplyThemeExpression;
import com.example.powershell_terminal.interpreter.CommandTypeExpression;
import com.example.powershell_terminal.model.Tab;
import com.example.powershell_terminal.services.TabService;
import com.example.powershell_terminal.strategy.ContextStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tabs")
public class TabController {

    private final TabService tabService;
    private final CommandInvoker commandInvoker;


    public TabController(TabService tabService) {
        this.tabService = tabService;
        this.commandInvoker = new CommandInvoker();

    }

    @PostMapping("/create")
    public ResponseEntity<Tab> createTab() {
        Command createTabCommand = new CreateTabCommand(tabService);
        commandInvoker.setCommand(createTabCommand);
        Tab createdTab = commandInvoker.executeAndReturn();
        return ResponseEntity.ok(createdTab);
    }

    @PostMapping("/close")
    public ResponseEntity<String> closeTab(@RequestBody Map<String, Long> request) {
        Long tabId = request.get("tabId");
        Command closeTabCommand = new CloseTabCommand(tabService, tabId);
        commandInvoker.setCommand(closeTabCommand);
        commandInvoker.executeCommand();
        return ResponseEntity.ok("Tab closed: " + tabId);
    }

    @PostMapping("/change-text-color")
    public ResponseEntity<String> changeTextColor(@RequestBody Map<String, Object> request) {
        Long tabId = Long.valueOf(request.get("tabId").toString());
        String newColor = request.get("newColor").toString();
        Command changeColorCommand = new ChangeColorTextCommand(tabService, tabId, newColor);
        commandInvoker.setCommand(changeColorCommand);
        commandInvoker.executeCommand();
        return ResponseEntity.ok("Text color changed to: " + newColor);
    }

    @PostMapping("/close-all")
    public ResponseEntity<String> closeAllTabs() {
        tabService.closeAllTabs();
        return ResponseEntity.ok("All tabs closed.");
    }

    @PostMapping("/change-background-color")
    public ResponseEntity<String> changeBackgroundColor(@RequestBody Map<String, Object> request) {
        Long tabId = Long.valueOf(request.get("tabId").toString());
        String newBackgroundColor = request.get("newBackgroundColor").toString();
        Command changeBackgroundColorCommand = new ChangeColorBackgroundCommand(tabService, tabId, newBackgroundColor);
        commandInvoker.setCommand(changeBackgroundColorCommand);
        commandInvoker.executeCommand();
        return ResponseEntity.ok("Background color changed to: " + newBackgroundColor);
    }

    @GetMapping("/get-styles")
    public ResponseEntity<Map<String, String>> getStyles(@RequestParam Long tabId) {
        Tab tab = tabService.findTabById(tabId);
        if (tab == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of(
                "background", tab.getWindowBackground(),
                "textColor", tab.getSyntaxColor()
        ));
    }

    @GetMapping("/get-directory")
    public ResponseEntity<String> getCurrentDirectory(@RequestParam Long tabId) {
        Tab tab = tabService.findTabById(tabId);
        return ResponseEntity.ok(tab.getCurrentDirectory());
    }

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> executeCommand(@RequestBody Map<String, String> request) {
        String commandText = request.get("name");
        Long tabId = Long.valueOf(request.get("tabId").toString());

        Tab tab = tabService.findTabById(tabId);
        if (tab == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("output", "Tab not found"));
        }

        ApplyThemeExpression themeExpression = new ApplyThemeExpression(commandText, tabId, tabService);
        CommandTypeExpression typeExpression = new CommandTypeExpression(new ContextStrategy(), tabService);

        String themeResult = themeExpression.interpret(commandText, tab);
        if (!themeResult.equals("Unknown command")) {
            return ResponseEntity.ok(Map.of("output", themeResult));
        }

        String result = typeExpression.interpret(commandText, tab);

        return ResponseEntity.ok(Map.of("output", result, "currentDirectory", tab.getCurrentDirectory()));
    }
}