package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Usuario;

public class UsuarioDAO {
	private Connection connection;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean authenticate(String nickname, String contraseña) {
		String sql = "SELECT * FROM Usuario WHERE nickname=? AND contraseña=?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, nickname);
			stmt.setString(2, contraseña);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("nickname").equals(nickname) && rs.getString("contraseña").equals(contraseña);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}