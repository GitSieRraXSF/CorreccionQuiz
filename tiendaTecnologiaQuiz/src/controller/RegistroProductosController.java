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
    private TableColumn<Producto, Integer> columnRef1;
	
	@FXML
    private TableColumn<Producto, String> columnNom1;
	
	@FXML
    private TableColumn<Producto, Double> columnPre1;

	@FXML
	private TableView<Producto> tableProductos;
	
	@FXML
    private TableView<Producto> table1pro;

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
		// Filter available products and add them to the availableProducts list
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
    void nuevoTemplate(ActionEvent event) {
		
    }

	@FXML
	void cerrarSesion(ActionEvent event) {
		UserSession.getInstance().destroy();
		Main.loadView("/view/Login.fxml");
	}
}