package data;

public class DBConnectionFactory {

	public static DBConnection getConnectionByRole(String role) {
		switch (role.toLowerCase()) {
		case "admin":
			return AdminConnection.getInstance();
		case "student":
			return StudentConnection.getInstance();
		case "teacher":
			return TeacherConnection.getInstance();
		default:
			throw new IllegalArgumentException("Rol no válido: " + role);
		}
	}
}