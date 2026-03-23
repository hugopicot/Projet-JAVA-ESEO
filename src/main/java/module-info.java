module org.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.demo2 to javafx.fxml;
    opens org.example.demo2.controller to javafx.fxml;
    opens org.example.demo2.model to javafx.fxml;

    exports org.example.demo2;
    exports org.example.demo2.controller;
    exports org.example.demo2.model;
    exports org.example.demo2.service;
    exports org.example.demo2.dao;
    exports org.example.demo2.util;
}