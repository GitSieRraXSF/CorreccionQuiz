package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminConnection {
    private static AdminConnection instance; //Singleton
    private Connection connection;
	private final String username="adminPro";
	private final String password="adminPro";
	private final String host = "192.168.254.215";
	private final String port = "1521";
	private final String service = "orcl";

    private AdminConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static AdminConnection getInstance() {
        if (instance == null) instance = new AdminConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
	public String getConnectionString() {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
	}
}
