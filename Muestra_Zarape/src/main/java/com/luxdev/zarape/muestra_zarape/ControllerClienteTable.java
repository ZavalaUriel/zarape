package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Ciudad;
import com.luxdev.zarape.muestra_zarape.Model.Cliente;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

public class ControllerClienteTable {

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
    private TableColumn<Cliente, String> colActivo;

    @FXML
    private TableColumn<Cliente, String> colApellidosPersona;

    @FXML
    private TableColumn<Cliente, String> colId;

    @FXML
    private TableColumn<Cliente, String> colIdCiudad;

    @FXML
    private TableColumn<Cliente, String> colNombrePersona;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private TableView<Cliente> tblClientes;

    private Stage stage;


    ObservableList<Cliente> clientes;
    ObservableList<Ciudad> ciudades;
    Cliente clienteSelected = null;
    @FXML

    public void initialize() {
        // Configuración inicial
        loadCliente();
        initColumns();

        tblClientes.setOnMouseClicked(event -> abrirFormulario());

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

        btnLimpiarMostrarFormulario.setOnMouseClicked(event -> {
            loadFXML("ClienteForm");
        });
    }

    private void abrirFormulario() {
        // Obtener la fila seleccionada
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();

        try {
            // Cargar el formulario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/luxdev/zarape/muestra_zarape/ClienteForm.fxml"));
            AnchorPane formularioPane = loader.load();

            // Obtener el controlador del formulario
            ControllerClienteForm controller = loader.getController();

            // Enviar los datos al formulario
            controller.setData(clienteSeleccionado);

            // Cambiar de pestaña usando loadFXML
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(formularioPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initColumns() {
        colId.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getIdCliente()).asString());
        colNombrePersona.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getNombrePersona()));
        colApellidosPersona.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getApellidos()));
        colActivo.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getActivoCliente()).asString());
        colTelefono.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getTelefono()));
        colIdCiudad.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getCiudad().getIdCiudad()).asString());
    }

    private void loadCliente() {
        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.get(globals.BASE_URL + "cliente/getAll").asString();
            Platform.runLater(() -> {
                Gson gson = new Gson();
                clientes = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Cliente[].class)));
                clientes.forEach(cliente -> System.out.println("ID " + cliente.getPersona().getCiudad().getIdCiudad()));
                tblClientes.setItems(clientes);
                tblClientes.refresh();
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


