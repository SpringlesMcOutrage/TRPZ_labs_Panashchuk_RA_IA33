package com.example.powershell_terminal.interpreter;

import com.example.powershell_terminal.model.Tab;

public interface Expression {
    String interpret(String context, Tab tab);
}
