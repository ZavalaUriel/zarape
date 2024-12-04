package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

public class ControllerEmpleadoForm {
    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TextField IdEmpleado;

    @FXML
    private ImageView btnAlimento;

    @FXML
    private ImageView btnBebida;

    @FXML
    private ImageView btnCliente;

    @FXML
    private ImageView btnCombo;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnGuardar;

    @FXML
    private ImageView btnHome;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnSalir;

    @FXML
    private ImageView btnSucursal;

    @FXML
    private ImageView btnUsuario;

    @FXML
    private TextField txtApellido;

    @FXML
    private ComboBox<Ciudad> cmbCiudad;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private ComboBox<Sucursal> cmbSucursal;

    @FXML
    private TextField txtTelefono;

    ObservableList<Empleado> empleados;
    ObservableList<Ciudad> ciudades;
    ObservableList<Sucursal> sucursales;



    public void initialize(){
        btnEliminar.setOnMouseClicked(event -> {
            delete();
        });
        btnGuardar.setOnMouseClicked(event -> {
            save();
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
        btnUsuario.setOnMouseClicked(event -> {
            loadFXML("EmpleadoTable");
        });
        btnNuevo.setOnMouseClicked(event -> {
            cleanForm();
        });


        loadCiudad();
        cmbCiudad.setConverter(new StringConverter<Ciudad>() {
            @Override
            public String toString(Ciudad ciudad) {
                return ciudad != null ? ciudad.getNombreCiudad() : "";
            }

            @Override
            public Ciudad fromString(String nombre) {
                return null;
            }
        });

        loadSucursal();
        cmbSucursal.setConverter(new StringConverter<Sucursal>() {
            @Override
            public String toString(Sucursal sucursal) {
                return sucursal != null ? sucursal.getNombreSucursal() : "";
            }

            @Override
            public Sucursal fromString(String nombre) {
                return null;
            }
        });

    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void delete(){

        Empleado empleado  = new Empleado();

        empleado.setIdEmpleado(Integer.parseInt(IdEmpleado.getText()));

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "empleado/delete")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("idEmpleado", empleado.getIdEmpleado())
                    .asString();

            Platform.runLater(() -> {
                if (response.getStatus() == 200) {
                    mostrarAlerta("Éxito", "Cliente guardado con éxito.", Alert.AlertType.INFORMATION);

                } else {
                    mostrarAlerta("Error", "Hubo un error al guardar el cliente.", Alert.AlertType.ERROR);
                }
            });
        }).start();
    }


    private void cleanForm(){
        IdEmpleado.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtNombreUsuario.setText("");
        txtContrasenia.setText("");
        cmbCiudad.setPromptText("");
        cmbSucursal.setPromptText("");
    }

    private void loadCiudad() {
        Globals globals = new Globals();
        new Thread(() ->{
            HttpResponse<String> response = Unirest.get(globals.BASE_URL+"sucursal/getAllCiudades").asString();
            //System.out.println(response.getBody());
            Platform.runLater(() ->{
                //txtRespuesta.setText(response.getBody());
                Gson gson = new Gson();
                ciudades = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Ciudad[].class)));
                cmbCiudad.setItems(ciudades);
            });
        }).start();
    }

    public void setData(Empleado empleado) {
        IdEmpleado.setText(String.valueOf(empleado.getIdEmpleado()));
        txtNombre.setText(String.valueOf(empleado.getPersona().getNombrePersona()));
        txtApellido.setText(empleado.getPersona().getNombrePersona());
        txtTelefono.setText(empleado.getPersona().getTelefono());
        txtNombreUsuario.setText(empleado.getUsuario().getNombreUsuario());
        txtContrasenia.setText(empleado.getUsuario().getContrasenia());

    }

    private void loadSucursal() {
        Globals globals = new Globals();
        new Thread(() ->{
            HttpResponse<String> response = Unirest.get(globals.BASE_URL+"sucursal/getAll").asString();
            //System.out.println(response.getBody());
            Platform.runLater(() ->{
                //txtRespuesta.setText(response.getBody());
                Gson gson = new Gson();
                sucursales = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Sucursal[].class)));
                cmbSucursal.setItems(sucursales);
            });
        }).start();
    }

    public void save(){
        Empleado empleado = new Empleado();
        Usuario u = new Usuario();
        empleado.setIdEmpleado(IdEmpleado.getText().isEmpty() ? 0 : Integer.parseInt(IdEmpleado.getText()));

        empleado.setPersona(new Persona());
        empleado.getPersona().setNombrePersona(txtNombre.getText());
        empleado.getPersona().setApellidos(txtApellido.getText());
        empleado.getPersona().setTelefono(txtTelefono.getText());
        Ciudad ciudadSeleccionada = cmbCiudad.getSelectionModel().getSelectedItem();
        empleado.getPersona().setCiudad(ciudadSeleccionada);
        u.setNombreUsuario(txtNombreUsuario.getText());
        u.setContrasenia(txtContrasenia.getText());
        empleado.setUsuario(u);
        Sucursal sucursalSeleccionada = cmbSucursal.getSelectionModel().getSelectedItem();
        empleado.setSucursal(sucursalSeleccionada);

        Gson gson = new Gson();
        String datosEmpleado = gson.toJson(empleado);

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "empleado/save")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosEmpleado", datosEmpleado)
                    .asString();
        }).start();

    }
    public void loadFXML(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/utleon/zarape_empleado/" + fxml + ".fxml"));
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