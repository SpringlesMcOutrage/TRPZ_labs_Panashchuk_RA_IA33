package com.example.powershell_terminal.abstract_factory.dark_theme;

import com.example.powershell_terminal.abstract_factory.BackgroundColor;

public class DarkBackgroundColor implements BackgroundColor {

    public String getColor() {
        return "Black";
    }
}
