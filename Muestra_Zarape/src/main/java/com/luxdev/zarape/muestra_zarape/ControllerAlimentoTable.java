package com.luxdev.zarape.muestra_zarape;


import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Alimento;
import com.luxdev.zarape.muestra_zarape.Model.Cliente;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

public class ControllerAlimentoTable {

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
    private Button btnLimpiarMostrarFormulario;

    @FXML
    private ImageView btnSucursal;

    @FXML
    private ImageView btnUsuario;

    @FXML
    private TableColumn<Alimento, String> colActivo;

    @FXML
    private TableColumn<Alimento, String> colCategoria;

    @FXML
    private TableColumn<Alimento, String> colIdAlimento;

    @FXML
    private TableColumn<Alimento, String> colNombreProducto;

    @FXML
    private TableColumn<Alimento, String> colPrecioProducto;

    @FXML
    private TableView<Alimento> tblAlimento;

    ObservableList<Alimento> alimentos;

    public void initialize() {

        initColumns();
        loadCliente();

        tblAlimento.setOnMouseClicked(event -> {
            abrirFormulario();
        });

        btnLimpiarMostrarFormulario.setOnMouseClicked(event -> {
            loadFXML("AlimentoForm");
        });
        btnHome.setOnMouseClicked(event -> {
            loadFXML("home");
        });
        btnAlimento.setOnMouseClicked(event -> {
            loadFXML("AlimentoTable");
        });
        btnBebida.setOnMouseClicked(event -> {
            loadFXML("BebidaTable");
        });
        btnCliente.setOnMouseClicked(event -> {
            loadFXML("ClienteForm");
        });
        btnCombo.setOnMouseClicked(event -> {
            loadFXML("ComboTable");
        });
        btnSucursal.setOnMouseClicked(event -> {
            loadFXML("SucursalTable");
        });

    }


    private void abrirFormulario() {
        // Obtener la fila seleccionada
        Alimento alimentoSeleccionado = tblAlimento.getSelectionModel().getSelectedItem();

        try {
            // Cargar el formulario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/luxdev/zarape/muestra_zarape/AlimentoForm.fxml"));
            AnchorPane formularioPane = loader.load();

            // Obtener el controlador del formulario
            ControllerAlimentoForm controller = loader.getController();

            // Enviar los datos al formulario
            controller.setData(alimentoSeleccionado);

            // Cambiar de pestaña usando loadFXML
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(formularioPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



private void initColumns() {
        colIdAlimento.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getIdAlimento()).asString());
        colNombreProducto.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getNombreProducto()));
        colPrecioProducto.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getPrecioProducto()).asString());
        colActivo.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getActivoProducto()).asString());
        colCategoria.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getCategoria().getIdCategoria()).asString());

    }

    private void loadCliente() {
        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.get(globals.BASE_URL + "alimento/getAll").asString();
            Platform.runLater(() -> {
                Gson gson = new Gson();
                alimentos = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Alimento[].class)));
                tblAlimento.setItems(alimentos);
                tblAlimento.refresh();
            });
        }).start();
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
