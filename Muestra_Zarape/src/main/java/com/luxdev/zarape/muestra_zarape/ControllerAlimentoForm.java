package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

public class ControllerAlimentoForm {

    @FXML
    private AnchorPane mainContainer;

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
    private TextArea txtDescripcion;

    @FXML
    private TextField txtIdAlimento;

    @FXML
    private TextField txtIdProducto;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtaFoto;

    ObservableList<Categoria> categorias;

    public void initialize(){
        btnDelete.setOnMouseClicked(event -> {
            delete();
        });


        loadCategoria();

        cmbCategoria.setItems(categorias);


        cmbCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria != null ? categoria.getDescripcionCategoria() : "";
            }

            @Override
            public Categoria fromString(String nombre) {
                return null;
            }
        });

        btnLimpiarFormulario.setOnMouseClicked(event -> {
            cleanForm();
        });
        btnSave.setOnMouseClicked(event -> {
            save();
        });
        btnIniciar.setOnMouseClicked(event -> {
            loadFXML("AlimentoTable");
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

        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() || txtDescripcion.getText().isEmpty()) {
            mostrarAlerta("Error", "Debe llenar todos los campos", Alert.AlertType.ERROR);
            return;
        }

        Alimento alimento = new Alimento();
        alimento.setIdAlimento(txtIdAlimento.getText().isEmpty() ? 0 : Integer.parseInt(txtIdAlimento.getText()));

        alimento.setProducto(new Producto());
        alimento.getProducto().setNombreProducto(txtNombre.getText());
        alimento.getProducto().setPrecioProducto(Double.parseDouble(txtPrecio.getText()));
        alimento.getProducto().setDescripcionProducto(txtDescripcion.getText());
        Categoria categoriaSeleccionada = cmbCategoria.getSelectionModel().getSelectedItem();
        alimento.getProducto().setCategoria(categoriaSeleccionada);

        if (!txtIdProducto.getText().trim().isEmpty()) {
            int idProducto = Integer.parseInt(txtIdProducto.getText().trim());

            // Si la ID de la persona es la misma, actualiza
            if (alimento.getIdAlimento() == alimento.getIdAlimento() && alimento.getProducto().getIdProducto() == idProducto) {
                alimento.getProducto().setIdProducto(idProducto);
            } else {
                // Si la ID no es la misma, asignar nueva persona o crear una nueva
                alimento.getProducto().setIdProducto(idProducto);
            }
        }

        Gson gson = new Gson();
        String datosAlimento = gson.toJson(alimento);

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "alimento/save")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("datosAlimento", datosAlimento)
                    .asString();

            txtIdAlimento.setText(String.valueOf(alimento.getIdAlimento()));
            txtIdProducto.setText(String.valueOf(alimento.getProducto().getIdProducto()));

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

        Producto producto = new Producto();

        producto.setIdProducto(Integer.parseInt(txtIdProducto.getText()));

        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.post(globals.BASE_URL + "alimento/delete")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .field("idProducto", producto.getIdProducto())
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


    private void loadCategoria() {
        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.get(globals.BASE_URL + "categoria/getAllByTipo?tipo=B").asString();
            //System.out.println(response.getBody());
            Platform.runLater(() -> {
                //txtRespuesta.setText(response.getBody());
                Gson gson = new Gson();
                categorias = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Categoria[].class)));
                cmbCategoria.setItems(categorias);
            });
        }).start();
    }

    private void cleanForm() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtDescripcion.setText("");

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


    public void setData(Alimento alimento) {
        txtIdAlimento.setText(String.valueOf(alimento.getIdAlimento()));
        txtIdProducto.setText(String.valueOf(alimento.getProducto().getIdProducto()));
        txtNombre.setText(alimento.getProducto().getNombreProducto());
        txtPrecio.setText(String.valueOf(alimento.getProducto().getPrecioProducto()));
        txtDescripcion.setText(alimento.getProducto().getDescripcionProducto());

    }


}
