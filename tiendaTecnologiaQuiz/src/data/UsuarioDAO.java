package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import model.UserSession;

public class UsuarioDAO {
	
	private Connection connection;
	public UserSession usersession;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean authenticate(String nickname, String contraseña, String rol) {
		//String sequel = "SELECT * FROM PROGRAMMINGII.Usuario WHERE nickname=? AND password=? AND role=?";
		String sql = "{? = call PROGRAMMINGII.AuthenticateUsuario(?, ?, ?)}";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.registerOutParameter(1, java.sql.Types.INTEGER); 
			stmt.setString(2, nickname);
			stmt.setString(3, contraseña);
			stmt.setString(4, rol);
			stmt.execute();
			int result = stmt.getInt(1);
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}