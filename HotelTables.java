import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HotelTables {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/tfei3";
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet result = null;

    public static void main(String[] args) {
        dropTables();
        createTables();
        insertHotelTable();
        insertRoomTable();
        insertCustomerTable();

        close();
    }

    private static void dropTables(){
      try{
        connectToDatabase();

        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
        statement.executeUpdate("DROP TABLE IF EXISTS hotel;");
        statement.executeUpdate("DROP TABLE IF EXISTS room;");
        statement.executeUpdate("DROP TABLE IF EXISTS staff;");
        statement.executeUpdate("DROP TABLE IF EXISTS manager;");
        statement.executeUpdate("DROP TABLE IF EXISTS representative;");
        statement.executeUpdate("DROP TABLE IF EXISTS customer;");
        statement.executeUpdate("DROP TABLE IF EXISTS manage;");
        statement.executeUpdate("DROP TABLE IF EXISTS register;");
        statement.executeUpdate("DROP TABLE IF EXISTS manageHotel;");
        statement.executeUpdate("DROP TABLE IF EXISTS checkin;");
        statement.executeUpdate("DROP TABLE IF EXISTS assign;");
        statement.executeUpdate("DROP TABLE IF EXISTS services;");
        statement.executeUpdate("DROP TABLE IF EXISTS input;");
        statement.executeUpdate("DROP TABLE IF EXISTS customerHasCheckin;");
        statement.executeUpdate("DROP TABLE IF EXISTS billingInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS paymentInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS billingInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS calculate;");
        statement.executeUpdate("DROP TABLE IF EXISTS receive;");
        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");


        } catch (ClassNotFoundException e) {
              e.printStackTrace();
          } catch (SQLException e) {
              e.printStackTrace();
            }
    }

    private static void createTables() {
        try {
            connectToDatabase();
            dropTables();

            statement.executeUpdate("CREATE TABLE hotel (" +
                    "hotelID INT PRIMARY KEY," +
                    "name VARCHAR(128) NOT NULL," +
                    "address VARCHAR(128) NOT NULL," +
                    "phonenumber VARCHAR(128) NOT NULL," +
                    "managerID VARCHAR(128) NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE room (" +
                    "roomnumber INT NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "roomcategory VARCHAR(128) NOT NULL," +
                    "maxcapacityallowed INT NOT NULL," +
                    "nightlyrate FLOAT(7,2) NOT NULL," +
                    "availability VARCHAR(128) NOT NULL," +
                    "PRIMARY KEY (roomnumber, hotelID),"+
                    "CONSTRAINT hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE customer (" +
                    "customerID INT PRIMARY KEY ," +
                    "name VARCHAR(128) NOT NULL," +
                    "birthday VARCHAR(128)," +
                    "phonenumber VARCHAR(128)," +
                    "email VARCHAR(128) NOT NULL" +
                    ")");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertHotelTable(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO hotel VALUES (1, 'Hotel A','21 ABC St,Raleigh NC 27',919,100)");
        statement.executeUpdate("INSERT INTO hotel VALUES (2, 'Hotel B','25 XYZ St,Rochester NY 54',718,101)");
        statement.executeUpdate("INSERT INTO hotel VALUES (3, 'Hotel C','29 PQR St,Greensboro NC 27',984,102)");
        statement.executeUpdate("INSERT INTO hotel VALUES (4, 'Hotel D','28 GHW St , Raleigh NC 32',920,105)");
      }catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }

    private static void insertRoomTable(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO room VALUES (1,1,'Economy',1,100,'Yes')");
        statement.executeUpdate("INSERT INTO room VALUES (2,1,'Deluxe',2,200,'Yes')");
        statement.executeUpdate("INSERT INTO room VALUES (3,2,'Economy',1,100,'YES')");
        statement.executeUpdate("INSERT INTO room VALUES (2,3,'Executive',3,1000,'No')");
        statement.executeUpdate("INSERT INTO room VALUES (1,4,'Presidential',4,5000,'YES')");
        statement.executeUpdate("INSERT INTO room VALUES (5,1,'Deluxe',2,200,'YES')");
      }
      catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }

    }

    private static void insertCustomerTable(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO customer VALUES (1001,'David','19800130','123','david@gmail.com')");
        statement.executeUpdate("INSERT INTO customer VALUES (1002,'Sarah','19710130','456','sarah@gmail.com')");
        statement.executeUpdate("INSERT INTO customer VALUES (1003,'Joseph','19870130','789','joseph@gmail.com')");
        statement.executeUpdate("INSERT INTO customer VALUES (1004,'Lucy','19850130','213','lucy@gmail.com')");
      }
      catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }

    }



    private static void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        String user = "tfei3";
        String password = "ft1993king";

        connection = DriverManager.getConnection(jdbcURL, user, password);
        statement = connection.createStatement();


    }


    private static void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
  }
