package controller;

import java.sql.Connection;
import application.Main;
import data.UsuarioDAO;
import data.DBConnectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.UserSession;

public class LoginController {

	@FXML
	private PasswordField txtContraseña;

	@FXML
	private TextField txtUsuario;
	
	@FXML
	private ComboBox<String> rolComboBox;

	private Connection connection;
	private UsuarioDAO usuarioDAO;
	public UserSession userSession;
	@FXML
	void initialize() {
		rolComboBox.getItems().addAll("admin", "student", "teacher");
	}
	
	@FXML
	void iniciarSesion(ActionEvent event) {
		switch (rolComboBox.getSelectionModel().getSelectedItem()) {
			case "admin":
				connection = DBConnectionFactory.getConnectionByRole("admin").getConnection();
				usuarioDAO = new UsuarioDAO(connection);
				if (usuarioDAO.authenticate(txtUsuario.getText(), txtContraseña.getText(), "admin") && (!txtUsuario.getText().isBlank() || !txtContraseña.getText().isBlank())) {
					userSession = UserSession.getInstance(txtUsuario.getText(), "admin");
					Main.loadView("/view/RegistroProductos.fxml");
				} else {
					Main.showAlert("Usuario Invalida", "Usuario Invalido", "Digite un usuario valido.", Alert.AlertType.WARNING);
				}
				break;
			case "teacher":
				connection = DBConnectionFactory.getConnectionByRole("teacher").getConnection();
				usuarioDAO = new UsuarioDAO(connection);
				if (usuarioDAO.authenticate(txtUsuario.getText(), txtContraseña.getText(), "admin") && (!txtUsuario.getText().isBlank() || !txtContraseña.getText().isBlank())) {
					userSession = UserSession.getInstance(txtUsuario.getText(), "teacher");
					Main.loadView("/view/RegistroProductos.fxml");
				} else {
					Main.showAlert("Usuario Invalida", "Usuario Invalido", "Digite un usuario valido.", Alert.AlertType.WARNING);
				}
				break;
			case "student":
				connection = DBConnectionFactory.getConnectionByRole("student").getConnection();
				usuarioDAO = new UsuarioDAO(connection);
				if (usuarioDAO.authenticate(txtUsuario.getText(), txtContraseña.getText(), "admin") && (!txtUsuario.getText().isBlank() || !txtContraseña.getText().isBlank())) {
					userSession = UserSession.getInstance(txtUsuario.getText(), "student");
					Main.loadView("/view/RegistroProductos.fxml");
				} else {
					Main.showAlert("Usuario Invalida", "Usuario Invalido", "Digite un usuario valido.", Alert.AlertType.WARNING);
				}
				break;
			default:
				Main.showAlert("Error!", "Seleccion invalida", "Digite un usuario valido.", Alert.AlertType.ERROR);
		}
	}
}