package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Ciudad;
import com.luxdev.zarape.muestra_zarape.Model.Cliente;
import com.luxdev.zarape.muestra_zarape.Model.Persona;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

public class ControllerCliente {

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

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnLimpiarFormulario;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<Ciudad> cmbCiudad;

    @FXML
    private TextField txtApellidosPersona;

    @FXML
    private TextField txtIdCliente;

    @FXML
    private TextField txtIdPersona;

    @FXML
    private TextField txtNombrePersona;

    @FXML
    private TextField txtTelefono;



    ObservableList<Cliente> clientes;
    ObservableList<Ciudad> ciudades;
    Cliente clienteSelected = null;

    public void initialize(){
            if (cmbCiudad != null) {
                cmbCiudad.setConverter(new StringConverter<Ciudad>() {
                    @Override
                    public String toString(Ciudad ciudad) {
                        return ciudad != null ? ciudad.getNombreCiudad() : "";
                    }

                    @Override
                    public Ciudad fromString(String nombre) {
                        // Este método no se usará en este caso
                        return null;
                    }
                });
            } else {
                System.out.println("El ComboBox cmbCiudad es nulo");
            }
            // Resto de la inicialización*/
            loadCiudades("");


        tblClientes.setItems(clientes);
        cmbCiudad.setItems(ciudades);

        btnSave.setOnMouseClicked(event -> {
            save();
        });

        btnIniciar.setOnMouseClicked(event -> {
            loadCliente();
        });

        tblClientes.setOnMouseClicked(event -> {
            showClienteSelected();
        });

        btnLimpiarMostrarFormulario.setOnMouseClicked(event -> {
            loadFXML("ClienteForm");
            cleanForm();
        });

    }

    private void initColumns() {
        colId.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getIdCliente()).asString());
        colNombrePersona.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getNombrePersona()));
        colApellidosPersona.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getApellidos()));
        colActivo.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getActivoCliente()).asString());
        colTelefono.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getTelefono()));
        colIdCiudad.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getPersona().getCiudad().getNombreCiudad()));
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void save() {

        if (txtNombrePersona.getText().isEmpty() || txtApellidosPersona.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
            mostrarAlerta("Error", "Debe llenar todos los campos", Alert.AlertType.ERROR);
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setIdCliente(txtIdCliente.getText().isEmpty() ? 0 : Integer.parseInt(txtIdCliente.getText()));

        cliente.setPersona(new Persona());
        cliente.getPersona().setNombrePersona(txtNombrePersona.getText());
        cliente.getPersona().setApellidos(txtApellidosPersona.getText());
        cliente.getPersona().setTelefono(txtTelefono.getText());
        Ciudad ciudadSeleccionada = cmbCiudad.getSelectionModel().getSelectedItem();
        cliente.getPersona().setCiudad(ciudadSeleccionada);

        if (!txtIdPersona.getText().trim().isEmpty()) {
            int idPersona = Integer.parseInt(txtIdPersona.getText().trim());

            // Si la ID de la persona es la misma, actualiza
            if (clienteSelected != null && clienteSelected.getPersona().getIdPersona() == idPersona) {
                cliente.getPersona().setIdPersona(idPersona);
            } else {
                // Si la ID no es la misma, asignar nueva persona o crear una nueva
                cliente.getPersona().setIdPersona(idPersona);
            }
        }
        Gson gson = new Gson();
        String datosCliente = gson.toJson(cliente);

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "cliente/save")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosCliente", datosCliente)
                    .asString();

            Platform.runLater(() -> {
                if (response.getStatus() == 200) {
                    mostrarAlerta("Éxito", "Cliente guardado con éxito.", Alert.AlertType.INFORMATION);
                    loadCliente();
                    cleanForm();
                } else {
                    mostrarAlerta("Error", "Hubo un error al guardar el cliente.", Alert.AlertType.ERROR);
                }
            });
        }).start();
    }



    private void loadCliente() {
        Globals globals = new Globals();
        new Thread(() ->{
            HttpResponse<String> response = Unirest.get(globals.BASE_URL+"cliente/getAll").asString();
            Platform.runLater(() ->{
                Gson gson = new Gson();
                clientes = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Cliente[].class)));
                clientes.forEach(cliente -> System.out.println("ID "+cliente.getPersona().getCiudad().getIdCiudad()));
                tblClientes.setItems(clientes);
                tblClientes.refresh();
            });
        }).start();
    }

    private void cleanForm(){
        txtNombrePersona.setText("");
        txtApellidosPersona.setText("");
        txtTelefono.setText("");
        txtIdCliente.setText("");



    }

    private void loadCiudades(String filtro) {
        Globals globals = new Globals();
        new Thread(() ->{
            HttpResponse<String> response = Unirest.get(globals.BASE_URL+"cliente/getAllCiudades?filtro="+filtro).asString();
            //System.out.println(response.getBody());
            Platform.runLater(() ->{
                //txtRespuesta.setText(response.getBody());
                Gson gson = new Gson();
                ciudades = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Ciudad[].class)));
                cmbCiudad.setItems(ciudades);
            });
        }).start();
    }

    public void showClienteSelected(){

        clienteSelected = tblClientes.getSelectionModel().getSelectedItem();
        txtIdCliente.setText(String.valueOf(clienteSelected.getIdCliente()));
        txtIdPersona.setText(String.valueOf(clienteSelected.getPersona().getIdPersona()));
        txtNombrePersona.setText(clienteSelected.getPersona().getNombrePersona());
        txtApellidosPersona.setText(clienteSelected.getPersona().getApellidos());
        txtTelefono.setText(clienteSelected.getPersona().getTelefono());
        cmbCiudad.getSelectionModel().getSelectedItem().getIdCiudad();
        //txtCiudad.getSelectionModel().select(findCiudadById(sucursalSelected.getCiudad().getIdCiudad()));
        //ctionModel().select(findCiudadById(sucursalSelected.getCiudad().getIdCiudad()));
        //txtEstatus.setSelected(sucursalSelected.getActivo() == 1);
        btnSave.setText("Modificar");
        System.out.println(clienteSelected.getPersona().getCiudad());
    }

    public Ciudad findCiudadById(int id) {
        Ciudad ciudad = null;
        for (Ciudad item : ciudades) {
            if(item.getIdCiudad() == id) {
                return item;
            }
        }
        return null;
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


