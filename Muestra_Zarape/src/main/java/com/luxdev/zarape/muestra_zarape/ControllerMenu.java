package com.luxdev.zarape.muestra_zarape;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ControllerMenu{

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private ImageView btnAlimento;

    @FXML
    private ImageView btnBebida;

    @FXML
    private ImageView btnCliente;

    @FXML
    private ImageView btnCombo;

    @FXML
    private ImageView btnHome;

    @FXML
    private ImageView btnSucursal;

    @FXML
    private ImageView btnUsuario;

    public void initialize() {
        btnHome.setOnMouseClicked(event -> {
            loadFXML("home");
        });
        btnUsuario.setOnMouseClicked(event -> {
            loadFXML("EmpleadoTable");
        });
        btnAlimento.setOnMouseClicked(event -> {
            loadFXML("AlimentoTable");
        });
        btnBebida.setOnMouseClicked(event -> {
            loadFXML("BebidaTable");
        });
        btnCliente.setOnMouseClicked(event -> {
            loadFXML("ClienteTable");
        });
        btnCombo.setOnMouseClicked(event -> {
            loadFXML("ComboTable");
        });
        btnSucursal.setOnMouseClicked(event -> {
            loadFXML("SucursalTable");
        });
        /*btnSalir.setOnMouseClicked(event -> {
            System.exit(0);
        });*/
    }

    public void loadFXML(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/luxdev/zarape/muestra_zarape/" + fxml + ".fxml"));
            AnchorPane newLoadedPane = loader.load();
            AnchorPane.setTopAnchor(newLoadedPane, 0.0);
            AnchorPane.setRightAnchor(newLoadedPane, 0.0);
            AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
            AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
            mainContainer.getChildren().clear(); // Limpia el contenedor
            mainContainer.getChildren().add(newLoadedPane); // Añade el nuevo FXML cargado
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}