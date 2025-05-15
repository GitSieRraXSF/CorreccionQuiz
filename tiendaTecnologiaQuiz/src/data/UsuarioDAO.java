package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.UserSession;

public class UsuarioDAO {
	private Connection connection;
	public UserSession usersession;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean authenticate(String nickname, String contraseña, String rol) {
		String sql = "SELECT * FROM PROGRAMMINGII.Usuario WHERE nickname=? AND contraseña=? AND rol=?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, nickname);
			stmt.setString(2, contraseña);
			stmt.setString(3, rol);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				usersession = UserSession.getInstance(nickname, rol);
				return rs.getString("nickname").equals(nickname) && rs.getString("contraseña").equals(contraseña) && rs.getString("rol").equals(rol);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}