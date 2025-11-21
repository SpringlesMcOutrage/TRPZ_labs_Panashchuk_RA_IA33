package com.example.powershell_terminal.abstract_factory.light_theme;

import com.example.powershell_terminal.abstract_factory.BackgroundColor;
import com.example.powershell_terminal.abstract_factory.TextColor;
import com.example.powershell_terminal.abstract_factory.ThemeFactory;

public class LightThemeFactory implements ThemeFactory {

    public TextColor createTextColor() {
        return new LightTextColor();
    }

    public BackgroundColor createBackgroundColor() {
        return new LightBackgroundColor();
    }
}

