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

    public static void main(String[] args) {
        dropTables();
        createTables();
        insertstaff();
        insertcheckin();

        close();
    }

    private static void dropTables(){
      try{
        connectToDatabase();

        statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
        statement.executeUpdate("DROP TABLE IF EXISTS hotel;");
        statement.executeUpdate("DROP TABLE IF EXISTS staff;");
        statement.executeUpdate("DROP TABLE IF EXISTS checkin;");
        statement.executeUpdate("DROP TABLE IF EXISTS manager;");
        statement.executeUpdate("DROP TABLE IF EXISTS representative;");
        statement.executeUpdate("DROP TABLE IF EXISTS customerInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS manage;");
        statement.executeUpdate("DROP TABLE IF EXISTS register;");
        statement.executeUpdate("DROP TABLE IF EXISTS manageHotel;");
        statement.executeUpdate("DROP TABLE IF EXISTS checkinInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS assign;");
        statement.executeUpdate("DROP TABLE IF EXISTS serviceOffered;");
        statement.executeUpdate("DROP TABLE IF EXISTS input;");
        statement.executeUpdate("DROP TABLE IF EXISTS customerHasCheckin;");
        statement.executeUpdate("DROP TABLE IF EXISTS billingInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS paymentInfo;");
        statement.executeUpdate("DROP TABLE IF EXISTS billingHasPayment;");
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

            statement.executeUpdate("CREATE TABLE staff (" +
                    "staffID INT PRIMARY KEY," +
                    "name VARCHAR(128) NOT NULL," +
                    "age INT not NULL," +
                    "jobtitle VARCHAR(128) NOT NULL," +
                    "hotelID VARCHAR(128) NOT NULL," +
                    "department VARCHAR(128) NOT NULL," +
                    "phonenumber INT NOT NULL," +
                    "address VARCHAR(128) NOT NULL" +
                   /* "CONSTRAINT staff_hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID) " +
                    "ON UPDATE CASCADE, " +                */
                    ")");

            statement.executeUpdate("CREATE TABLE checkin (" +
                    "customerID INT NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "roomnumber INT NOT NULL," +
                    "numberofguests INT not NULL," +
                    "startdate DATE NOT NULL, " +
                    "enddate DATE NOT NULL, " +
                    "checkintime DATETIME NOT NULL, " +
                    "checkouttime DATETIME NOT NULL, " +
                    "servicesoffered VARCHAR(128) NOT NULL" +
                    /*"CONSTRAINT checkin_customer_fk FOREIGN KEY(customerID) REFERENCES customer(customerID) " +
                    "ON UPDATE CASCADE, " +
                    "CONSTRAINT checkin_hotel1_fk FOREIGN KEY(hotelID) REFERENCES hotel(chotelID) " +
                    "ON UPDATE CASCADE, " +
                    "CONSTRAINT checkin_hotel2_fk FOREIGN KEY(roomnumber) REFERENCES hotel(roomnumber) " +
                    "ON UPDATE CASCADE" + */
                    ")");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertstaff(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO staff  VALUES (100,'Mary',40,'Manager',1, 'Management', 654, '90 ABC St , Raleigh NC 27')");
        statement.executeUpdate("INSERT INTO staff  VALUES (101,'Carol',55, 'Manager', 3, 'Management', 546, '351 MH St , Greensboro NC 27')");
        statement.executeUpdate("INSERT INTO staff  VALUES (102,'John',45, 'Manager', 2, 'Management', 564, '798 XYZ St , Rochester NY 54')");
        statement.executeUpdate("INSERT INTO staff  VALUES (103,'Emma', 55,'Front Desk Staff', 1, 'Management', 546, '49 ABC St , Raleigh NC 27')");
        statement.executeUpdate("INSERT INTO staff  VALUES (104,'Ava', 55, 'Catering Staff', 1, 'Catering', 777, '425 RG St , Raleigh NC 27')");
        statement.executeUpdate("INSERT INTO staff  VALUES (105,'Peter', 52, 'Manager', 4, 'Manager', 724, '475 RG St , Raleigh NC 27')");
        statement.executeUpdate("INSERT INTO staff  VALUES (106,'Olivia', 27,'Front Desk Staff', 4, 'Management', 799, '325 PD St , Raleigh NC 27')");
      }
      catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }

    }

    private static void insertcheckin(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO checkin VALUES (1001, 1, 1, 1, '2017-5-10', '2017-5-13', '2017-5-10 15:17:00', '2017-5-13 10:22:00', 'dry cleaning, gyms')");
        statement.executeUpdate("INSERT INTO checkin VALUES (1002, 1, 2, 2, '2017-5-10', '2017-5-13', '2017-5-10 16:11:00', '2017-5-13 9:27:00', 'gyms')");
        statement.executeUpdate("INSERT INTO checkin VALUES (1003, 2, 3, 1, '2016-5-10', '2016-5-14', '2016-5-10 15:45:00', '2016-5-14 11:10:00', 'room service')");
        statement.executeUpdate("INSERT INTO checkin VALUES (1004, 3, 2, 2, '2018-5-10', '2018-5-12', '2018-5-10 14:30:00', '2018-5-12 10:00:00', 'phone bills')");
      }
      catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }

    }


    private static void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        String user = "zsun12";
        String password = "5211258";

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
