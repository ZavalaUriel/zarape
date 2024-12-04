module com.luxdev.zarape.muestra_zarape {
    requires javafx.controls;
    requires javafx.fxml;
    requires unirest.java;
    requires com.google.gson;


    opens com.luxdev.zarape.muestra_zarape to javafx.fxml;
    exports com.luxdev.zarape.muestra_zarape;
}