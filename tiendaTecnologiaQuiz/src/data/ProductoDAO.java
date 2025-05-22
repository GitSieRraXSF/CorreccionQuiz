package data;

import java.sql.CallableStatement;
import oracle.jdbc.OracleTypes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import application.Main;
import javafx.scene.control.Alert;
import model.Producto;

public class ProductoDAO {
	private Connection connection;

	public ProductoDAO(Connection connection) {
		this.connection = connection;
	}

	public void save(Producto producto) {
		String sql = "{call PROGRAMMINGII.InsertProducto(?, ?, ?, ?)}";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.setInt(1, producto.getReferencia());
			stmt.setString(2, producto.getNombre());
			stmt.setDouble(3, producto.getPrecio());
			stmt.setInt(4, producto.getCantidad());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			Main.showAlert("Error...!", "Proceso invalido!", e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public ArrayList<Producto> fetch() {
		ArrayList<Producto> productos = new ArrayList<>();
		//String sequel = "SELECT * FROM PROGRAMMINGII.Producto";
		String sql = "{? = call PROGRAMMINGII.FetchProducts()}";
		try (CallableStatement cs = connection.prepareCall(sql)) {
			cs.registerOutParameter(1, OracleTypes.CURSOR);
			cs.execute();
			try (ResultSet rs = (ResultSet) cs.getObject(1)){
				while (rs.next()) {
					int referencia = rs.getInt("referencia");
					String nombre = rs.getString("nombre");
					double precio = rs.getDouble("precio");
					int cantidad = rs.getInt("cantidad");
					Producto producto = new Producto(referencia, nombre, precio, cantidad);
					if (precio > 0 && productos.size() < 10) {
						productos.add(producto);
					} else {
						Main.showAlert("Error...!", "Proceso invalido!", "Ingreso del precio invalido o no superes la cantidad de productos.", Alert.AlertType.WARNING);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Main.showAlert("Error...!", "Proceso invalido!", e.getMessage(), Alert.AlertType.ERROR);
		}
		return productos;
	}
	
	public void update(Producto producto) {
		String sql = "{call = PROGRAMMINGII.UpdateProducts(?, ?, ?, ?)}";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.setString(1, producto.getNombre());
			stmt.setDouble(2, producto.getPrecio());
			stmt.setInt(3, producto.getCantidad());
			stmt.setInt(4, producto.getReferencia());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			Main.showAlert("Error...!", "Proceso invalido!", e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void delete(int referencia) {
		String sql = "{? = call PROGRAMMINGII.AuthenticateProducto(?)}";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.setInt(1, referencia);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			Main.showAlert("Error...!", "Proceso invalido!", e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public boolean authenticate(int referencia) {
		String sql = "{? = call PROGRAMMINGII.AuthenticateProducto(?)}";
		try (CallableStatement stmt = connection.prepareCall(sql)) {
			stmt.registerOutParameter(1, java.sql.Types.INTEGER);
			stmt.setInt(2, referencia);
			stmt.execute();
			int result = stmt.getInt(1);
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			Main.showAlert("Error...!", "Proceso invalido!", e.getMessage(), Alert.AlertType.ERROR);
		}
		return false;
	}
}