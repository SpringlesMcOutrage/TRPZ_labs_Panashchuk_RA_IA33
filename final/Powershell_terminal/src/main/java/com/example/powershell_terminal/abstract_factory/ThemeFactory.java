package com.example.powershell_terminal.abstract_factory;

public interface ThemeFactory {
    TextColor createTextColor();
    BackgroundColor createBackgroundColor();
}
