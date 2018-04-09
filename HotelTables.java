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

            statement.executeUpdate("CREATE TABLE hotel (" +
                    "hotelID INT PRIMARY KEY AUTO_INCREMENT," +
                    "hotelName VARCHAR(128) NOT NULL," +
                    "hotelAddress VARCHAR(128) NOT NULL," +
                    "hotelPhoneNumber VARCHAR(128) NOT NULL," +
                    "hotelManagerID VARCHAR(128) NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE room (" +
                    "roomId INT PRIMARY KEY AUTO_INCREMENT," +
                    "availability VARCHAR(128) NOT NULL," +
                    "roomCategory VARCHAR(128) NOT NULL," +
                    "nightlyRate FLOAT(7,2) NOT NULL," +
                    "maxAllowedOccupancy INT NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "CONSTRAINT hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE staff (" +
                    "staffID INT PRIMARY KEY AUTO_INCREMENT," +
                    "age INT not NULL," +
                    "name VARCHAR(128) NOT NULL," +
                    "department VARCHAR(128) NOT NULL," +
                    "contactInformation INT NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "jobtitle VARCHAR(128) NOT NULL," +
                    "CONSTRAINT staff_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE manager (" +
                    "staffID INT PRIMARY KEY," +
                    "CONSTRAINT manager_fk FOREIGN KEY(staffID) REFERENCES staff(staffID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE representative (" +
                    "staffID INT PRIMARY KEY," +
                    "CONSTRAINT representative_fk FOREIGN KEY(staffID) REFERENCES staff(staffID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE customerInfo (" +
                    "customerID INT PRIMARY KEY AUTO_INCREMENT," +
                    "customerName VARCHAR(128) NOT NULL," +
                    "birthday VARCHAR(128)," +
                    "phoneNumber VARCHAR(128)," +
                    "emailAddress VARCHAR(128) NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE manage (" +
                    "managerID INT," +
                    "representativeID INT," +
                    "PRIMARY KEY(managerID,representativeID)," +
                    "CONSTRAINT manage_ma_fk FOREIGN KEY(managerID) REFERENCES manager(staffID)" +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT manage_re_fk FOREIGN KEY(representativeID) REFERENCES representative(staffID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE register (" +
                    "representativeID INT," +
                    "customerID INT," +
                    "PRIMARY KEY(representativeID,customerID)," +
                    "CONSTRAINT register_re_fk FOREIGN KEY(representativeID) REFERENCES representative(staffID)" +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT register_cus_fk FOREIGN KEY(customerID) REFERENCES customerInfo(customerID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE manageHotel (" +
                    "managerID INT," +
                    "hotelID INT," +
                    "PRIMARY KEY(managerID, hotelID)," +
                    "CONSTRAINT manageHotel_manager_fk FOREIGN KEY(managerID) REFERENCES manager(staffID)" +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT manageHotel_hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID) " +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE checkinInfo (" +
                    "checkinID INT PRIMARY KEY AUTO_INCREMENT," +
                    "hotelId INT NOT NULL," +
                    "CONSTRAINT checkinInfo_hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID) " +
                    "ON UPDATE CASCADE," +
                    "customerName VARCHAR(128) NOT NULL, " +
                    "roomID INT NOT NULL, " +
                    "guestsNumber VARCHAR(128) NOT NULL, customerBirthday DATE, " +
                    "startDate DATE NOT NULL, " +
                    "endDate DATE NOT NULL, " +
                    "checkinTime DATE NOT NULL, " +
                    "checkoutTime DATE NOT NULL, " +
                    "serviceOffered VARCHAR(128) " +
                    ")");
            statement.executeUpdate("CREATE TABLE assign (" +
                    "checkinID INT," +
                    "representativeID INT NOT NULL," +
                    "roomID INT NOT NULL," +
                    "customerID INT NOT NULL," +
                    "PRIMARY KEY(checkinID)," +
                    "CONSTRAINT assign_checkin_fk FOREIGN KEY(checkinID) REFERENCES checkinInfo(checkinID) " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT assign_representative_fk FOREIGN KEY(representativeID) REFERENCES representative(staffID) " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT assign_room_fk FOREIGN KEY(roomID) REFERENCES room(roomID) " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT assign_customer_fk FOREIGN KEY(customerID) REFERENCES customerInfo(customerID) " +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE serviceOffered (" +
                    "serviceOfferedID INT PRIMARY KEY AUTO_INCREMENT," +
                    "checkinID INT NOT NULL," +
                    "staffID INT NOT NULL," +
                    "price FLOAT NOT NULL," +
                    "CONSTRAINT serviceOffered_checkin_fk FOREIGN KEY(checkinID) REFERENCES checkinInfo(checkinID) " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT serviceOffered_staff_fk FOREIGN KEY(staffID) REFERENCES staff(staffID) " +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE input (" +
                    "representativeID INT, " +
                    "checkinID INT, " +
                    "PRIMARY KEY(representativeID, checkinID)," +
                    "CONSTRAINT input_representative_fk FOREIGN KEY(representativeID) REFERENCES representative(staffID) " +
                    "ON UPDATE CASCADE," +
                    "CONSTRAINT input_checkinInfo_fk FOREIGN KEY(checkinID) REFERENCES checkinInfo(checkinID) " +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE customerHasCheckin (" +
                    "customerID INT," +
                    "checkinID INT," +
                    "CONSTRAINT customerHasCheckin_pk PRIMARY KEY(customerID, checkinID)," +
                    "CONSTRAINT customerHasCheckin_customer_fk FOREIGN KEY(customerID) REFERENCES customerInfo(customerID) ON UPDATE CASCADE," +
                    "CONSTRAINT customerHasCheckin_checkin_fk FOREIGN KEY(checkinID) REFERENCES checkinInfo(checkinID) ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE billingInfo (" +
                    "billingID INT PRIMARY KEY AUTO_INCREMENT," +
                    "billingAddress VARCHAR(512) NOT NULL," +
                    "paymentSSN VARCHAR(128) NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE paymentInfo (" +
                    "paymentID INT PRIMARY KEY AUTO_INCREMENT," +
                    "paymentMethod VARCHAR(128) NOT NULL," +
                    "cardNumber VARCHAR(128) NOT NULL," +
                    "amount FLOAT NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE billingHasPayment (" +
                    "billingID INT," +
                    "paymentID INT," +
                    "CONSTRAINT billingHasPayment_pk PRIMARY KEY(billingID, paymentID)," +
                    "CONSTRAINT billingHasPayment_billing_fk FOREIGN KEY(billingID) REFERENCES billingInfo(billingID) ON UPDATE CASCADE," +
                    "CONSTRAINT billingHasPayment_payment_fk FOREIGN KEY(paymentID) REFERENCES paymentInfo(paymentID) ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE calculate (" +
                    "representativeID INT," +
                    "billingID INT," +
                    "CONSTRAINT calculate_pk PRIMARY KEY(representativeID, billingID)," +
                    "CONSTRAINT calculate_representative_fk FOREIGN KEY(representativeID) REFERENCES representative(staffID) ON UPDATE CASCADE," +
                    "CONSTRAINT calculate_billing_fk FOREIGN KEY(billingID) REFERENCES billingInfo(billingID) ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE receive (" +
                    "customerID INT," +
                    "billingID INT," +
                    "CONSTRAINT receive_pk PRIMARY KEY(customerID, billingID)," +
                    "CONSTRAINT receive_customer_fk FOREIGN KEY(customerID) REFERENCES customerInfo(customerID) ON UPDATE CASCADE," +
                    "CONSTRAINT receive_billing_fk FOREIGN KEY(billingID) REFERENCES billingInfo(billingID) ON UPDATE CASCADE" +
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

        statement.executeUpdate("INSERT INTO room (roomID,roomCategory,hotelID,nightlyRate,maxAllowedOccupancy,availability) VALUES (1,'Economy',1,100,1,'YES')");
        statement.executeUpdate("INSERT INTO room (roomID,roomCategory,hotelID,nightlyRate,maxAllowedOccupancy,availability) VALUES (2,'Deluxe',1,200,2,'YES')");
        statement.executeUpdate("INSERT INTO room (roomID,roomCategory,hotelID,nightlyRate,maxAllowedOccupancy,availability) VALUES (3,'Economy',2,100,1,'YES')");
        statement.executeUpdate("INSERT INTO room (roomID,roomCategory,hotelID,nightlyRate,maxAllowedOccupancy,availability) VALUES (4,'Executive',3,100,3,'No')");
        statement.executeUpdate("INSERT INTO room (roomID,roomCategory,hotelID,nightlyRate,maxAllowedOccupancy,availability) VALUES (5,'Presidential',4,500,4,'YES')");
        statement.executeUpdate("INSERT INTO room (roomID,roomCategory,hotelID,nightlyRate,maxAllowedOccupancy,availability) VALUES (6,'Deluxe',1,200,2,'YES')");
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

        statement.executeUpdate("INSERT INTO customerInfo VALUES (1,'David','19800130','123','david@gmail.com')");
        statement.executeUpdate("INSERT INTO customerInfo VALUES (2,'Sarah','19710130','456','sarah@gmail.com')");
        statement.executeUpdate("INSERT INTO customerInfo VALUES (3,'Joseph','19870130','789','joseph@gmail.com')");
        statement.executeUpdate("INSERT INTO customerInfo VALUES (4,'Lucy','19850130','213','lucy@gmail.com')");
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
