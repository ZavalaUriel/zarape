package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Ciudad;
import com.luxdev.zarape.muestra_zarape.Model.Cliente;
import com.luxdev.zarape.muestra_zarape.Model.Persona;
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

public class ControllerClienteForm {

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
    private Button btnDelete;

    @FXML
    private ImageView btnHome;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnLimpiarFormulario;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView btnSucursal;

    @FXML
    private ImageView btnUsuario;

    @FXML
    private  ComboBox<Ciudad> cmbCiudad;

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

    ObservableList<Ciudad> ciudades;

    public void initialize() {

        cleanForm();
        // Asegúrate de que la lista de ciudades esté inicializada
        cmbCiudad.setItems(FXCollections.observableArrayList());

        // Configura el StringConverter
        cmbCiudad.setConverter(new StringConverter<>() {
            @Override
            public String toString(Ciudad ciudad) {
                return ciudad != null ? ciudad.getNombreCiudad() : "";
            }

            @Override
            public Ciudad fromString(String string) {
                return null; // No se necesita
            }
        });

        // Cargar las ciudades desde la API
        loadCiudades("");


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
        loadCiudades("");

        cmbCiudad.setItems(ciudades);

        btnDelete.setOnMouseClicked(event -> {
            delete();
        });

        btnSave.setOnMouseClicked(event -> {
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
        btnLimpiarFormulario.setOnMouseClicked(event -> {
            cleanForm();
        });

        btnIniciar.setOnMouseClicked(event -> {
            loadFXML("ClienteTable");
        });
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
            if (cliente.getIdCliente() == cliente.getIdCliente() && cliente.getPersona().getIdPersona() == idPersona) {
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

            txtIdCliente.setText(String.valueOf(cliente.getIdCliente()));
            txtIdPersona.setText(String.valueOf(cliente.getPersona().getIdPersona()));

            Platform.runLater(() -> {
                if (response.getStatus() == 200) {
                    mostrarAlerta("Éxito", "Cliente guardado con éxito.", Alert.AlertType.INFORMATION);

                } else {
                    mostrarAlerta("Error", "Hubo un error al guardar el cliente.", Alert.AlertType.ERROR);
                }
            });
        }).start();
    }


    private void delete(){

        Cliente cliente = new Cliente();

        cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "cliente/delete")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("idCliente", cliente.getIdCliente())
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

    private void loadCiudades(String filtro) {
        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.get(globals.BASE_URL + "cliente/getAllCiudades?filtro=" + filtro).asString();
            //System.out.println(response.getBody());
            Platform.runLater(() -> {
                //txtRespuesta.setText(response.getBody());
                Gson gson = new Gson();
                ciudades = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Ciudad[].class)));
                cmbCiudad.setItems(ciudades);
            });
        }).start();
    }

    private void cleanForm() {
        txtNombrePersona.setText("");
        txtApellidosPersona.setText("");
        txtTelefono.setText("");
        txtIdCliente.setText("");
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

    public void setData(Cliente cliente) {
        txtIdCliente.setText(String.valueOf(cliente.getIdCliente()));
        txtIdPersona.setText(String.valueOf(cliente.getPersona().getIdPersona()));
        txtNombrePersona.setText(cliente.getPersona().getNombrePersona());
        txtApellidosPersona.setText(cliente.getPersona().getApellidos());
        txtTelefono.setText(cliente.getPersona().getTelefono());

    }


}



