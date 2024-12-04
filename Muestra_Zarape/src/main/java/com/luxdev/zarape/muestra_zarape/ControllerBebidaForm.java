package com.luxdev.zarape.muestra_zarape;


import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Bebida;
import com.luxdev.zarape.muestra_zarape.Model.Categoria;
import com.luxdev.zarape.muestra_zarape.Model.Producto;
import javafx.application.Platform;
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

public class ControllerBebidaForm {
    @FXML
    private ImageView btnAlimento;

    @FXML
    private ImageView btnBebida;

    @FXML
    private Button btnCargarFoto;

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
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private ImageView imgFoto;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtIdBebida;

    @FXML
    private TextField txtIdProducto;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtaFoto;

    //lista de arreglos
    ObservableList<Categoria> categorias;
    //  Bebida bebidaSelected = null;

    public void initialize() {
        btnIniciar.setOnMouseClicked(event -> {
            loadFXML("BebidaTable");
        });
        btnHome.setOnMouseClicked(event -> {
            loadFXML("Menu");
        });
        btnSucursal.setOnMouseClicked(event -> {
            loadFXML("SucursalTable");
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
        btnUsuario.setOnMouseClicked(event -> {
            loadFXML("EmpleadoTable");
        });
        btnSave.setOnMouseClicked(event -> {
            save();
        });
        btnLimpiarFormulario.setOnMouseClicked(event -> {
            cleanForm();
        });
        cmbCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria != null ? categoria.getDescripcionCategoria() : "";
            }
            @Override
            public Categoria fromString(String descripcion) {
                return null;
            }
        });
        loadCategoria();
        cmbCategoria.setItems(categorias);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void save() {
        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() || txtDescripcion.getText().isEmpty()) {
            mostrarAlerta("Error", "Favor de llenar todos los campos", Alert.AlertType.ERROR);
            return;
        }
        //crea un nuevo objeto
        Bebida bebida = new Bebida();
        bebida.setIdBebida(txtIdBebida.getText().isEmpty() ? 0 : Integer.parseInt(txtIdBebida.getText()));
        //insercion de los datos en el nuevo objeto
        bebida.setProducto(new Producto());
        bebida.getProducto().setNombreProducto(txtNombre.getText());
        bebida.getProducto().setPrecioProducto(Double.parseDouble(txtPrecio.getText()));
        bebida.getProducto().setDescripcionProducto(txtDescripcion.getText());
        bebida.getProducto().setFotoProducto(txtaFoto.getText());
        Categoria categoriaSeleccionada = cmbCategoria.getSelectionModel().getSelectedItem();
        bebida.getProducto().setCategoria(categoriaSeleccionada);

       /* if (!txtIdProducto.getText().trim().isEmpty()) {
            int idProducto = Integer.parseInt(txtIdProducto.getText().trim());

            //actualiza si es la misma
            if (bebidaSelected != null && bebidaSelected.getProducto().getIdProducto() == idProducto) {
                bebida.getProducto().setIdProducto(idProducto);

            } else {
                // si no es la misma, registra nuevo
                bebida.getProducto().setIdProducto(idProducto);
*/
        //transforma los datos a formato JSON
        Gson gson = new Gson();
        String datosBebida = gson.toJson(bebida);

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "bebida/save")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosBebida", datosBebida)
                    .asString();
            Platform.runLater(() -> {
                if (response.getStatus() == 200) {
                    mostrarAlerta("Éxito", "Bebida guardada con éxito", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "Hubo un error al registrar bebida", Alert.AlertType.ERROR);
                }
            });
        }).start();
    }


    private void loadCategoria() {
        Globals globals = new Globals();
        new Thread(() ->{
            HttpResponse<String> response = Unirest.get(globals.BASE_URL+"categoria/getAllByTipo?tipo=B").asString();
            //System.out.println(response.getBody());
            Platform.runLater(() ->{
                //txtRespuesta.setText(response.getBody());
                Gson gson = new Gson();
                categorias = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Categoria[].class)));
                cmbCategoria.setItems(categorias);
            });
        }).start();
    }
    public void cleanForm(){
        txtNombre.setText("");
        txtPrecio.setText("");
        txtDescripcion.setText("");
        txtaFoto.setText("");
        txtIdBebida.setText("");
        txtIdProducto.setText("");
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