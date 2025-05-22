package controller;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

import application.Main;
import data.DBConnectionFactory;
import data.ProductoDAO;
import data.apachebook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

	@FXML
	private Button CATemplate;

	private Connection connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole())
			.getConnection();
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
			Main.showAlert("Ningun producto seleccionado O Acceso denegado", "Referencia repetida O Acceso denegado",
					"Debe registrar una referencia diferente O debes entrar al rol respectivo.",
					Alert.AlertType.WARNING);
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
				Main.showAlert("Funcion Invalida!", "Inventario lleno",
						"Deberas eliminar algunos productos antes para guardar otro.", Alert.AlertType.ERROR);
			}
		} else {
			Main.showAlert("Referencia repetida O Acceso denegado", "Referencia repetida O Acceso denegado",
					"Debe registrar una referencia diferente O debes entrar al rol respectivo.",
					Alert.AlertType.WARNING);
		}
	}

	@FXML
	void nuevoTemplate(ActionEvent event) {
		if (UserSession.getInstance().getRole().equals("admin")) {
			apachebook.createExcelFormat("Productos.xlsx");
		} else {
			Main.showAlert("Acceso denegado", "Rol no correspondiente",
					"Debes entrar al rol respectivo para crear los archivos.", Alert.AlertType.WARNING);
		}
	}

	@FXML
	void CargarTemplate(ActionEvent event) {
		if (UserSession.getInstance().getRole().equals("admin")) {
			FileChooser file = new FileChooser();
			file.setTitle("Seleccionar archivo de excel");
			file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arhivos Excel", "*.xlsx"));
			Stage stage = (Stage) CATemplate.getScene().getWindow();
			File archivoSeleccionado = file.showOpenDialog(stage);
			ArrayList<Producto> productosExcel = apachebook.fetchExcel(archivoSeleccionado);
			LoadTableView(productosExcel);
		} else {
			Main.showAlert("Acceso denegado", "Rol no correspondiente",
					"Debes entrar al rol respectivo para cargar archivos.", Alert.AlertType.WARNING);
		}
	}

	private void LoadTableView(ArrayList<Producto> productos) {
		columnPre1.setCellValueFactory(new PropertyValueFactory<>("precio"));
		columnRef1.setCellValueFactory(new PropertyValueFactory<>("referencia"));
		columnNom1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columnNom1.setCellFactory(TextFieldTableCell.forTableColumn());
		columnNom1.setOnEditCommit(event -> {
			Producto product = event.getRowValue();
			product.setNombre(event.getNewValue());
		});
		table1pro.getItems().setAll(productos);
		table1pro.setEditable(true);
	}

	@FXML
	void cerrarSesion(ActionEvent event) {
		UserSession.getInstance().destroy();
		Main.loadView("/view/Login.fxml");
	}
}