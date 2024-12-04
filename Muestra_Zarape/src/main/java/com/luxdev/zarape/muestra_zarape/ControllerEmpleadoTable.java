package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Ciudad;
import com.luxdev.zarape.muestra_zarape.Model.Cliente;
import com.luxdev.zarape.muestra_zarape.Model.Sucursal;
import com.luxdev.zarape.muestra_zarape.Model.Empleado;
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

public class ControllerEmpleadoTable {
    @FXML
    private TableColumn<Empleado, String> ColCiudad;

    @FXML
    private Button bntNuevoEmpleado;

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

    @FXML
    private TableColumn<Empleado, String> colApellidos;

    @FXML
    private TableColumn<Empleado, String> colContrasenia;

    @FXML
    private TableColumn<Empleado, String> colEstado;

    @FXML
    private TableColumn<Empleado, String> colId;

    @FXML
    private TableColumn<Empleado, String> colNombre;

    @FXML
    private TableColumn<Empleado, String> colSucursal;

    @FXML
    private TableColumn<Empleado, String> colTelefono;

    @FXML
    private TableColumn<Empleado, String> colUsuario;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TableView<Empleado> tblEmpleados;

        ObservableList<Empleado> empleados;



        public void initialize(){
            initColumns();
            loadEmpleado();


            bntNuevoEmpleado.setOnMouseClicked(event -> {
                loadFXML("EmpleadoForm");
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
                loadFXML("ClienteTable");
            });
            btnCombo.setOnMouseClicked(event -> {
                loadFXML("ComboTable");
            });
            btnSucursal.setOnMouseClicked(event -> {
                loadFXML("SucursalTable");
            });
            bntNuevoEmpleado.setOnMouseClicked(event -> {
                abrirFormulario();
            });


        }


    private void abrirFormulario() {
        // Obtener la fila seleccionada
        Empleado empleadoSeleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        try {
            // Cargar el formulario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/luxdev/zarape/muestra_zarape/EmpleadoForm.fxml"));
            AnchorPane formularioPane = loader.load();

            // Obtener el controlador del formulario
            ControllerEmpleadoForm controller = loader.getController();

            // Enviar los datos al formulario
            controller.setData(empleadoSeleccionado);

            // Cambiar de pestaña usando loadFXML
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(formularioPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        public void initColumns() {

            colId.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getIdEmpleado()).asString());
            colNombre.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getNombrePersona()));
            colApellidos.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getApellidos()));
            colTelefono.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getTelefono()));
            colUsuario.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getUsuario().getNombreUsuario()));
            colContrasenia.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getUsuario().getContrasenia()));
            ColCiudad.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getCiudad().getNombreCiudad()));
            colSucursal.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getSucursal().getIdSucursal()).asString());
            colEstado.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getActivoEmpleado()).asString());

        }
        private void loadEmpleado() {
            Globals globals = new Globals();
            new Thread(() -> {
                HttpResponse<String> response = Unirest.get(globals.BASE_URL + "empleado/getAll").asString();
                Platform.runLater(() -> {
                    Gson gson = new Gson();
                    empleados = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Empleado[].class)));
                    empleados.forEach(empleado -> System.out.println("ID " + empleado.getPersona().getCiudad().getIdCiudad()));
                    tblEmpleados.setItems(empleados);
                    tblEmpleados.refresh();
                });
            }).start();
        }
}

