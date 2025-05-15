package controller;

import java.sql.Connection;
import application.Main;
import data.DBConnectionFactory;
import data.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Producto;
import model.UserSession;

public class RegistroProductosController {

	@FXML
	private TableColumn<Producto, Integer> columnCantidad;

	@FXML
	private TableColumn<Producto, String> columnNombre;

	@FXML
	private TableColumn<Producto, Double> columnPrecio;

	@FXML
	private TableView<Producto> tableProductos;

	@FXML
	private TextField txtCantidad;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtPrecio;

	@FXML
	private TextField txtReferencia;

	private Connection connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
	private ProductoDAO productoDAO = new ProductoDAO(connection);

	@FXML
	public void initialize() {

		ObservableList<Producto> availableProductos = FXCollections.observableArrayList();
		// Filter available books and add them to the availableBooks list
		for (Producto producto : productoDAO.fetch()) {
			availableProductos.add(producto);
		}

		// Bind only the columns you want to show
		columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		columnCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

		// Set data to TableView
		tableProductos.setItems(availableProductos);
	}

	@FXML
	void eliminar(ActionEvent event) {

		if (!tableProductos.getSelectionModel().isEmpty()) {
			Producto producto = tableProductos.getSelectionModel().getSelectedItem();
			productoDAO.delete(producto.getReferencia());
			initialize();
		} else {
			Main.showAlert("Seleccione un registro", "Seleccione un registro", "Debe seleccionar un dato de la tabla", Alert.AlertType.WARNING);
		}
		initialize();
	}

	@FXML
	void registrar(ActionEvent event) {
		int referencia = Integer.parseInt(txtReferencia.getText());
		double precio = Double.parseDouble(txtPrecio.getText());
		String nombre = txtNombre.getText();
		int cantidad = Integer.parseInt(txtCantidad.getText());
		if (!productoDAO.authenticate(referencia)) {
			Producto producto = new Producto(referencia, nombre, precio, cantidad);
			if (productoDAO.fetch().size() <= 100) {
				productoDAO.save(producto);
				initialize();
			} else {
				Main.showAlert("Funcion Invalida!", "Inventario lleno", "Deberas eliminar algunos productos antes para guardar otro.", Alert.AlertType.ERROR);
			}
		} else {
			Main.showAlert("Referencia repetida", "Referencia repetida", "Debe registrar una referencia diferente", Alert.AlertType.WARNING);
		}
	}

	@FXML
	void cerrarSesion(ActionEvent event) {
		Main.loadView("/view/Login.fxml");
	}
}