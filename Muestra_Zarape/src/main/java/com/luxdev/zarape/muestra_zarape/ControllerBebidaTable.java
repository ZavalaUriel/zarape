package com.luxdev.zarape.muestra_zarape;

import com.google.gson.Gson;
import com.luxdev.zarape.muestra_zarape.Model.Bebida;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

public class ControllerBebidaTable {
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
    private TableColumn<Bebida, Integer> colActivo;

    @FXML
    private TableColumn<Bebida, String> colCategoria;

    @FXML
    private TableColumn<Bebida, Integer> colIdBebida;

    @FXML
    private TableColumn<Bebida, String> colNombreProducto;

    @FXML
    private TableColumn<Bebida, String> colPrecioProducto;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TableView<Bebida> tblBebida;

    //lista de arreglos
    ObservableList<Bebida> bebidas;
    // Producto productoSelected = null;

    public void initialize() {

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
        initColumns();
        loadBebida();
        tblBebida.setItems(bebidas);
        //tblBebida.setOnMouseClicked(event -> {
        // showBebidaSelected();
        // });
    }

    private void initColumns() {
        colIdBebida.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getIdBebida()));
        colNombreProducto.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getNombreProducto()));
        colPrecioProducto.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getPrecioProducto()).asString());
        colCategoria.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getCategoria().getDescripcionCategoria()));
        colActivo.setCellValueFactory(col -> new SimpleObjectProperty<>(col.getValue().getProducto().getActivoProducto()));
    }

    private void loadBebida() {
        Globals globals = new Globals();
        new Thread(() -> {
            HttpResponse<String> response = Unirest.get(globals.BASE_URL + "bebida/getAll").asString();
            Platform.runLater(() -> {
                Gson gson = new Gson();
                bebidas = FXCollections.observableArrayList(List.of(gson.fromJson(response.getBody(), Bebida[].class)));
                bebidas.forEach(bebida -> System.out.println("ID " + bebida.getProducto().getCategoria().getIdCategoria()));
                tblBebida.setItems(bebidas);
                tblBebida.refresh();
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
    /*
    public void showBebidaSelected(){
        productoSelected = tblBebida.getSelectionModel().getSelectedItem();
        txtNombre.setText(productoSelected.getNombreProducto());
        txtColonia.setText(sucursalSelected.getColonia());
        txtFoto.setText(sucursalSelected.getFoto());
        txtHorarios.setText(sucursalSelected.getHorarios());
        txtIdSucursal.setText(String.valueOf(sucursalSelected.getIdSucursal()));
        txtLatitud.setText(sucursalSelected.getLatitud().toString());
        txtLongitud.setText(sucursalSelected.getLongitud());
        txtNombre.setText(sucursalSelected.getNombre());
        txtNumCalle.setText(sucursalSelected.getNumCalle());
        txtUrl.setText(sucursalSelected.getUrlWeb());
        //txtCiudad.getSelectionModel().select(findCiudadById(sucursalSelected.getCiudad().getIdCiudad()));
        txtCiudad.getSelectionModel().select(findCiudadById(sucursalSelected.getCiudad().getIdCiudad()));
        txtEstatus.setSelected(sucursalSelected.getActivo() == 1);
        btnGuardar.setText("Modificar");
        System.out.println(sucursalSelected.getCiudad().getIdCiudad());
}
}


*/
}
