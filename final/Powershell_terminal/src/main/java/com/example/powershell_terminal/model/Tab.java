package com.example.powershell_terminal.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String syntaxColor;
    private String windowBackground;
    private String currentDirectory;

    public Tab() {
    }

    public Tab(String title) {
        this.title = title;
        this.syntaxColor = "BLACK";
        this.windowBackground = "WHITE";
        this.currentDirectory = System.getProperty("user.home");
    }

    // Геттери та сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSyntaxColor() {
        return syntaxColor;
    }

    public void setSyntaxColor(String syntaxColor) {
        this.syntaxColor = syntaxColor;
    }

    public String getWindowBackground() {
        return windowBackground;
    }

    public void setWindowBackground(String windowBackground) {
        this.windowBackground = windowBackground;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

}