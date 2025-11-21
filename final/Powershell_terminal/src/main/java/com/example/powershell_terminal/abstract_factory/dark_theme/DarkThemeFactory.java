package com.example.powershell_terminal.abstract_factory.dark_theme;

import com.example.powershell_terminal.abstract_factory.BackgroundColor;
import com.example.powershell_terminal.abstract_factory.TextColor;
import com.example.powershell_terminal.abstract_factory.ThemeFactory;

public class DarkThemeFactory implements ThemeFactory {

    public TextColor createTextColor() {
        return new DarkTextColor();
    }

    public BackgroundColor createBackgroundColor() {
        return new DarkBackgroundColor();
    }

}