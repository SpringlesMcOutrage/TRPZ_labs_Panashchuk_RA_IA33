module com.example.client_terminal {
    requires javafx.fxml;
    requires spring.web;
    requires javafx.web;


    opens com.example.client_terminal to javafx.fxml;
    exports com.example.client_terminal;
}