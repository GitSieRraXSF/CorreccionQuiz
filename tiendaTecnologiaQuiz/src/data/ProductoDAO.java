package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Producto;

public class ProductoDAO {
	private Connection connection;

	public ProductoDAO(Connection connection) {
		this.connection = connection;
	}

	public void save(Producto producto) {
		String sql = "INSERT INTO PROGRAMMINGII.Producto (referencia, nombre, precio, cantidad) VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, producto.getReferencia());
			stmt.setString(2, producto.getNombre());
			stmt.setDouble(3, producto.getPrecio());
			stmt.setInt(4, producto.getCantidad());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Producto> fetch() {
		ArrayList<Producto> productos = new ArrayList<>();
		String sql = "SELECT * FROM PROGRAMMINGII.Producto";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				int referencia = rs.getInt("referencia");
				String nombre = rs.getString("nombre");
				double precio = rs.getDouble("precio");
				int cantidad = rs.getInt("cantidad");
				Producto producto = new Producto(referencia, nombre, precio, cantidad);
				productos.add(producto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productos;
	}
	
	public void update(Producto producto) {
		String sql = "UPDATE PROGRAMMINGII.Producto SET nombre = ?, precio = ?, cantidad = ? WHERE referencia = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, producto.getNombre());
			stmt.setDouble(2, producto.getPrecio());
			stmt.setInt(3, producto.getCantidad());
			stmt.setInt(4, producto.getReferencia());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int referencia) {
		String sql = "DELETE FROM PROGRAMMINGII.Producto WHERE referencia=?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, referencia);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean authenticate(int referencia) {
		String sql = "SELECT * FROM PROGRAMMINGII.Producto WHERE referencia=?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, referencia);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("referencia") == referencia;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}