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
        createTables();
    }


    private static void createTables() {
        try {
            connectToDatabase();


            statement.executeUpdate("CREATE TABLE hotel (" +
                    "hotelID INT NOT NULL," +
                    "hotelName VARCHAR(128) NOT NULL," +
                    "hotelAddress VARCHAR(128) NOT NULL," +
                    "hotelPhoneNumber VARCHAR(128) NOT NULL," +
                    "hotelManagerID VARCHAR(128) NOT NULL," +
                    "PRIMARY KEY (hotelID)" +
                    ")");

            statement.executeUpdate("CREATE TABLE room (" +
                    "roomID INT NOT NULL," +
                    "availability VARCHAR(128) NOT NULL," +
                    "roomCategory VARCHAR(128) NOT NULL," +
                    "nightlyRate FLOAT(5,2) NOT NULL," +
                    "maxAllowedOccupancy INT NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "PRIMARY KEY (roomID)" +
                    "CONSTRAINT FK_room FOREIGN KEY (hotelID) REFERENCES hotel(hotelID) ON UPDATE CASCADE" +
                    ")");


            statement.executeUpdate("CREATE TABLE staff (" +
                    "staffID INT NOT NULL," +
                    "age INT NOT NULL," +
                    "name VARCHAR(128) NOT NULL," +
                    "department VARCHAR(128) NOT NULL," +
                    "contactInformation INT NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "jobtitle VARCHAR(128) NOT NULL," +
                    "PRIMARY KEY (staffID)" +
                    "CONSTRAINT FK_staff FOREIGN KEY (hotelID) REFERENCES hotel(hotelID) ON UPDATE CASCADE" +
                    ")");

            statement.executeUpdate("CREATE TABLE manager (" +
                    "staffID INT NOT NULL," +
                    "PRIMARY KEY (staffID)" +
                    "CONSTRAINT FK_manager FOREIGN KEY (staffID) REFERENCES staff(staffID) ON UPDATE CASCADE" +
                    ")");

            statement.executeUpdate("CREATE TABLE representative (" +
                    "staffID INT NOT NULL," +
                    "PRIMARY KEY (staffID)" +
                    "CONSTRAINT FK_representativ FOREIGN KEY (staffID) REFERENCES staff(staffID) ON UPDATE CASCADE" +
                    ")");

            statement.executeUpdate("CREATE TABLE customerInfo (" +
                    "customerID INT NOT NULL," +
                    "customerName VARCHAR(128) NOT NULL," +
                    "birthday VARCHAR(128) NOT NULL," +
                    "phoneNumber VARCHAR(128) NOT NULL," +
                    "emailAddress VARCHAR(128) NOT NULL," +
                    "PRIMARY KEY (customerID)" +
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
                    "	representativeID INT," +
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

    private static void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        String user = "zsun12";
        String password = "5211258";

        connection = DriverManager.getConnection(jdbcURL, user, password);
        statement = connection.createStatement();

        try {


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


        } catch (SQLException e) {
        }
    }
  }
