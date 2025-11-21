package com.example.powershell_terminal.services;

import com.example.powershell_terminal.abstract_factory.BackgroundColor;
import com.example.powershell_terminal.abstract_factory.TextColor;
import com.example.powershell_terminal.abstract_factory.ThemeFactory;
import com.example.powershell_terminal.model.Tab;
import com.example.powershell_terminal.repositories.TabRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TabService {

    private final TabRepository tabRepository;
    private AtomicInteger tabCounter = new AtomicInteger(1);

    public TabService(TabRepository tabRepository) {
        this.tabRepository = tabRepository;
    }

    public void applyTheme(Long tabId, ThemeFactory themeFactory) {
        TextColor textColor = themeFactory.createTextColor();
        BackgroundColor backgroundColor = themeFactory.createBackgroundColor();

        changeTextColor(tabId, textColor.getTextColor());
        changeBackgroundColor(tabId, backgroundColor.getColor());

        System.out.println("Applied theme to tab " + tabId + ": " +
                "Text color = " + textColor.getTextColor() +
                ", Background color = " + backgroundColor.getColor());
    }

    public Tab createTab() {
        String generatedName = "Tab " + tabCounter.getAndIncrement();
        Tab newTab = new Tab(generatedName);
        tabRepository.save(newTab);
        System.out.println("Tab created: " + generatedName);
        return newTab;
    }

    public void closeAllTabs() {
        tabRepository.deleteAll();
        System.out.println("All tabs closed");
    }

    public void closeTab(Long tabId) {
        tabRepository.deleteById(tabId);
        System.out.println("Tab closed: " + tabId);
    }

    public void changeTextColor(Long tabId, String newColor) {
        Tab tab = tabRepository.findById(tabId).orElseThrow(() -> new IllegalArgumentException("Tab not found"));
        tab.setSyntaxColor(newColor);
        tabRepository.save(tab);
        System.out.println("Text color for tab " + tabId + " changed to " + newColor);
    }

    public void changeBackgroundColor(Long tabId, String newBackgroundColor) {
        Tab tab = tabRepository.findById(tabId).orElseThrow(() -> new IllegalArgumentException("Tab not found"));
        tab.setWindowBackground(newBackgroundColor);
        tabRepository.save(tab);
        System.out.println("Background color for tab " + tabId + " changed to " + newBackgroundColor);
    }

    public Tab findTabById(Long tabId) {
        return tabRepository.findById(tabId)
                .orElseThrow(() -> new IllegalArgumentException("Tab with ID " + tabId + " not found"));
    }
    public void changeDirectory(Tab tab, String newDirectory) {
        tab.setCurrentDirectory(newDirectory);
        tabRepository.save(tab);
        System.out.println("Tab directory" + tab.getId() + " changed to " + newDirectory);
    }

}
