import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HotelTables {
	static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/zsun12";
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet result = null;
}

	public static void main(String[] args) {
		initialize();
	}

	private static void initialize(){
		try{
			connectToDatabase();
			
		}
	}
	
	private static void connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.mariadb.jdbc.Driver");

		String user = "$USER$";
		String password = "$PASSWORD$";

		connection = DriverManager.getConnection(jdbcURL, user, password);
		statement = connection.createStatement();

		try {
			statement.executeUpdate("DROP TABLE Students");
			statement.executeUpdate("DROP TABLE Schools");
		} catch (SQLException e) {
		}
	}