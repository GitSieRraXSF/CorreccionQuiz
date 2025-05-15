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
		System.out.print("Rol recurrente: ");
		System.out.println(UserSession.getInstance().getRole());
		
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
		if (!tableProductos.getSelectionModel().isEmpty() && UserSession.getInstance().getRole().equals("admin")) {
			Producto producto = tableProductos.getSelectionModel().getSelectedItem();
			productoDAO.delete(producto.getReferencia());
			initialize();
		} else {
			Main.showAlert("Ningun producto seleccionado O Acceso denegado", "Referencia repetida O Acceso denegado", "Debe registrar una referencia diferente O debes entrar al rol respectivo.", Alert.AlertType.WARNING);
		}
		initialize();
	}

	@FXML
	void registrar(ActionEvent event) {
		int referencia = Integer.parseInt(txtReferencia.getText());
		double precio = Double.parseDouble(txtPrecio.getText());
		String nombre = txtNombre.getText();
		int cantidad = Integer.parseInt(txtCantidad.getText());
		if (!productoDAO.authenticate(referencia) && UserSession.getInstance().getRole().equals("admin")) {
			Producto producto = new Producto(referencia, nombre, precio, cantidad);
			if (productoDAO.fetch().size() <= 100) {
				productoDAO.save(producto);
				initialize();
			} else {
				Main.showAlert("Funcion Invalida!", "Inventario lleno", "Deberas eliminar algunos productos antes para guardar otro.", Alert.AlertType.ERROR);
			}
		} else {
			Main.showAlert("Referencia repetida O Acceso denegado", "Referencia repetida O Acceso denegado", "Debe registrar una referencia diferente O debes entrar al rol respectivo.", Alert.AlertType.WARNING);
		}
	}
	
	@FXML
	void actualilzarP() {
		int referencia1 = Integer.parseInt(txtReferencia.getText());
		double precio1 = Double.parseDouble(txtPrecio.getText());
		String nombre1 = txtNombre.getText();
		int cantidad1 = Integer.parseInt(txtCantidad.getText());
		if (productoDAO.authenticate(referencia1) && UserSession.getInstance().getRole().equals("admin")) {
			Producto producto1 = new Producto(referencia1, nombre1, precio1, cantidad1);
			productoDAO.update(producto1);
			initialize();
		} else {
			Main.showAlert("Actualizacion Erronea O Acceso denegado", "Actualizacion Erronea O Acceso denegado", "Debe haber una actualizacion valida para un producto O debes entrar al rol respectivo.", Alert.AlertType.WARNING);
		}
	}

	@FXML
	void cerrarSesion(ActionEvent event) {
		UserSession.getInstance().destroy();
		Main.loadView("/view/Login.fxml");
	}
}