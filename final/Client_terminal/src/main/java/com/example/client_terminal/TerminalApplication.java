package com.example.client_terminal;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

public class TerminalApplication extends Application {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String serverUrl = "http://localhost:8080/tabs";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        ColorPicker backgroundColorPicker = new ColorPicker(Color.WHITE);
        ColorPicker textColorPicker = new ColorPicker(Color.BLACK);

        backgroundColorPicker.setOnAction(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                changeBackgroundColor(selectedTab, backgroundColorPicker.getValue());
            }
        });

        textColorPicker.setOnAction(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                changeTextColor(selectedTab, textColorPicker.getValue());
            }
        });

        // Buttons panel
        HBox controls = new HBox(10);
        Button createTabButton = new Button("Add Tab");
        controls.getChildren().addAll(createTabButton, new Label("Background:"), backgroundColorPicker, new Label("Text:"), textColorPicker);

        createTabButton.setOnAction(e -> createTab(tabPane));

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                Map<String, String> tabStyles = fetchTabStyles(Long.parseLong(newTab.getId()));
                backgroundColorPicker.setValue(Color.valueOf(tabStyles.get("background")));
                textColorPicker.setValue(Color.valueOf(tabStyles.get("textColor")));
            }
        });

        primaryStage.setOnCloseRequest(event -> {
            // Виклик методу для закриття всіх вкладок
            restTemplate.postForObject(serverUrl + "/close-all", null, String.class);
        });

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Tab Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

        createTab(tabPane);
    }

    private void createTab(TabPane tabPane) {
        Map<String, Object> createdTab = restTemplate.postForObject(serverUrl + "/create", null, Map.class);
        if (createdTab != null) {
            Tab tab = new Tab((String) createdTab.get("title"));
            tab.setId(String.valueOf(createdTab.get("id"))); // Set ID to identify the tab on the server

            VBox tabContent = new VBox(10);
            tabContent.setPadding(new Insets(10));
            tabContent.setFillWidth(true);

            String currentDirectory = (String) restTemplate.getForObject(serverUrl + "/get-directory?tabId=" + tab.getId(), String.class) + " > ";
            TextArea commandInputOutput = new TextArea(currentDirectory);
            commandInputOutput.setEditable(true);
            commandInputOutput.setStyle("-fx-font-size: 14;");
            commandInputOutput.setWrapText(true);
            VBox.setVgrow(commandInputOutput, Priority.ALWAYS);

            // Track the start of the editable region
            final int[] editableStart = {currentDirectory.length()};

            // Listen for changes and ensure the user cannot modify protected text
            commandInputOutput.textProperty().addListener((obs, oldText, newText) -> {
                if (newText.length() < editableStart[0]) {
                    commandInputOutput.setText(oldText); // Revert to old text if protected area is modified
                } else if (!newText.startsWith(currentDirectory)) {
                    commandInputOutput.setText(currentDirectory); // Ensure text always starts with the current directory
                }
            });

            // Listen for key presses
            commandInputOutput.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String[] lines = commandInputOutput.getText().split("\n");
                    String lastLine = lines[lines.length - 1];
                    if (lastLine.startsWith(currentDirectory)) {
                        String commandText = lastLine.substring(currentDirectory.length()).trim();
                        if (!commandText.isEmpty()) {
                            // Execute the command
                            Map request = Map.of("name", commandText, "tabId", Long.parseLong(tab.getId()));
                            String result = (String) restTemplate.postForObject(serverUrl + "/execute", request, Map.class).get("output");

                            // Apply theme if command is 'applyTheme'
                            if (commandText.startsWith("applyTheme")) {
                                Map<String, String> themeStyles = fetchTabStyles(Long.parseLong(tab.getId()));
                                changeBackgroundColor(tab, Color.valueOf(themeStyles.get("background")));
                                changeTextColor(tab, Color.valueOf(themeStyles.get("textColor")));
                            }

                            // Append the result and prompt for the next command
                            commandInputOutput.appendText("\n" + result + "\n" + currentDirectory);
                            editableStart[0] = commandInputOutput.getText().length(); // Update the editable start
                        }
                    }
                    event.consume(); // Prevent default behavior (new line)
                }
            });

            tabContent.getChildren().add(commandInputOutput);
            tab.setContent(tabContent);

            tabPane.getTabs().add(tab);
        }
    }

    private void changeBackgroundColor(Tab tab, Color color) {
        restTemplate.postForObject(serverUrl + "/change-background-color",
                Map.of("tabId", Long.parseLong(tab.getId()), "newBackgroundColor", toHexString(color)), String.class);

        VBox tabContent = (VBox) tab.getContent();
        tabContent.setStyle("-fx-background-color: " + toHexString(color) + ";");

        tabContent.getChildren().forEach(node -> {
            if (node instanceof TextField || node instanceof TextArea) {
                String textColor = extractStyleValue(node.getStyle(), "-fx-text-fill", toHexString(Color.BLACK)); // Save the text color
                node.setStyle(String.format("-fx-control-inner-background: %s; -fx-text-fill: %s; -fx-font-size: 14;",
                        toHexString(color), textColor));
            }
        });
    }

    private void changeTextColor(Tab tab, Color color) {
        restTemplate.postForObject(serverUrl + "/change-text-color",
                Map.of("tabId", Long.parseLong(tab.getId()), "newColor", toHexString(color)), String.class);

        VBox tabContent = (VBox) tab.getContent();

        tabContent.getChildren().forEach(node -> {
            if (node instanceof Label || node instanceof TextField || node instanceof TextArea) {
                String backgroundColor = extractStyleValue(node.getStyle(), "-fx-control-inner-background", toHexString(Color.WHITE)); // Save the background color
                node.setStyle(String.format("-fx-control-inner-background: %s; -fx-text-fill: %s; -fx-font-size: 14;",
                        backgroundColor, toHexString(color)));
            }
        });
    }

    private String extractStyleValue(String style, String property, String defaultValue) {
        if (style.contains(property)) {
            try {
                return style.split(property + ":")[1].split(";")[0].trim();
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private Map<String, String> fetchTabStyles(Long tabId) {
        return restTemplate.getForObject(serverUrl + "/get-styles?tabId=" + tabId, Map.class);
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}