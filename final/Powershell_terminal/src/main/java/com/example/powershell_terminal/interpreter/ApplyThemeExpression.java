package com.example.powershell_terminal.interpreter;

import com.example.powershell_terminal.abstract_factory.ThemeFactory;
import com.example.powershell_terminal.abstract_factory.dark_theme.DarkThemeFactory;
import com.example.powershell_terminal.abstract_factory.light_theme.LightThemeFactory;
import com.example.powershell_terminal.model.Tab;
import com.example.powershell_terminal.services.TabService;

public class ApplyThemeExpression implements Expression {
    private final String theme;
    private TabService tabService;

    public ApplyThemeExpression(String theme, Long tabId, TabService tabService) {
        this.theme = theme.toLowerCase();
        this.tabService = tabService;
    }

    public String interpret(String context, Tab tab) {
        if (context.startsWith("applyTheme")) {
            String[] parts = context.split("\\s+");
            if (parts.length == 2) {
                String themeName = parts[1].replace("'", "").toLowerCase(); // Видаляємо апострофи
                ThemeFactory themeFactory = createThemeFactory(themeName);
                if (themeFactory != null) {
                    tabService.applyTheme(tab.getId(), themeFactory);
                    return "Applying theme: " + themeName;
                }
            }
        }
        return "Unknown command";
    }
    private ThemeFactory createThemeFactory(String themeName) {
        switch (themeName) {
            case "dark":
                return new DarkThemeFactory();
            case "light":
                return new LightThemeFactory();
            default:
                System.out.println("Unknown theme: " + themeName);
                return null;
        }
    }
}
