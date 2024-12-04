package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {

    @FXML
    private AnchorPane mainContainer;
    @FXML
    private ImageView btnRegistrar;

    @FXML
    private ImageView btnSalir;

    @FXML
    private PasswordField txtContrasenia;

    @FXML
    private TextField txtNombreUsuario;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    btnRegistrar.setOnMouseClicked(event -> {
        Ingresar();
    });
    btnSalir.setOnMouseClicked(event -> {
        System.out.close();
    });


    }


    private void loadFXML(String fxml) {
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
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    @FXML
    private void Ingresar(){

        if (this.txtNombreUsuario.getText().isEmpty() || this.txtContrasenia.getText().isEmpty()) {
            mostrarAlerta("Error", "Debe llenar todos los campos", Alert.AlertType.ERROR);
        }

        Usuario usuario = new Usuario();

        usuario.setNombreUsuario(this.txtNombreUsuario.getText());
        usuario.setContrasenia(this.txtContrasenia.getText());

        Gson gson = new Gson();
        String ingreso = gson.toJson(usuario);

        Globals globals = new Globals();
        new Thread(() -> {


            HttpResponse<String> response = Unirest.get(globals.BASE_URL + "principal/login") // Ruta del endpoint
                    .queryString("user", usuario.getNombreUsuario()).queryString("password", usuario.getContrasenia()) // Envía los datos del cliente en el cuerpo de la solicitud.
                    .asString(); // Ejecuta la solicitud y obtiene la respuesta como una cadena.

            Platform.runLater(() -> {

                if (response.getStatus() == 200) {

                    String jsonResponse = response.getBody();
                    Usuario usuarios = gson.fromJson(jsonResponse, Usuario.class);

                    if(usuarios.isIngreso()){

                        loadFXML("Menu");

                    } else{
                        mostrarAlerta("Error","Datos incorrectos", Alert.AlertType.ERROR);
                    }

                } else {

                    System.out.println("No conecto con la api");
                }
            });
        }).start();



    }
}






