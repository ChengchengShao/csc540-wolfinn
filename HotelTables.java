import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelTables {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/zsun12";
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet result = null;

    Scanner infoProcessMenuChoice =new Scanner (System.in);
    public static void main(String[] args) {

        initialize();
        mainmenu();
        System.out.println("Your choice:");
        close();

    }


    private static void mainmenu(){
      System.out.printf("Welcome to Team F Wolf Inn! Please choose what you  want to do:\n");
      System.out.printf("1.Information Processing\n");
      System.out.printf("2.Maintaining Service Records\n");
      System.out.printf("3.Reports\n");
      System.out.printf("4.Exit\n");
      int choiceA;
      Scanner mainMenuChoice =new Scanner (System.in);
      choiceA = mainMenuChoice.nextInt();
      if(choiceA==1) {
        informationProcessing();
      }
      else if (choiceA ==2) {
        maintainingServiceRecords();
      }
      else if (choiceA==3) {
        reports();
      }
      else{
        otherNumber();
      }
    }

    private static void initialize(){

      dropTables();
      createTables();
      insertHotelTable();
      insertRoomTable();
      insertCustomerTable();
      insertBillingInfo();
      insertServices();
      insertstaff();
      insertcheckin();

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

            statement.executeUpdate("CREATE TABLE staff (" +
                    "staffID INT PRIMARY KEY," +
                    "name VARCHAR(128) NOT NULL," +
                    "age INT not NULL," +
                    "jobtitle VARCHAR(128) NOT NULL," +
                    "hotelID INT NOT NULL," +
                    "department VARCHAR(128) NOT NULL," +
                    "phonenumber INT NOT NULL," +
                    "address VARCHAR(128) NOT NULL, " +
                    "CONSTRAINT staff_hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID) " +
                    "ON UPDATE CASCADE " +
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
                    "servicesoffered VARCHAR(128) NOT NULL, " +
                    "CONSTRAINT checkin_customer_fk FOREIGN KEY(customerID) REFERENCES customer(customerID) " +
                    "ON UPDATE CASCADE, " +
                    "CONSTRAINT checkin_hotel_fk FOREIGN KEY(hotelID) REFERENCES hotel(hotelID) " +
                    "ON UPDATE CASCADE, " +
                    "CONSTRAINT checkin_room_fk FOREIGN KEY(roomnumber) REFERENCES room(roomnumber) " +
                    "ON UPDATE CASCADE" +
                    ")");

            statement.executeUpdate("CREATE TABLE billingInfo (" +
                    "customerID INT NOT NULL," +
                    "payerSSN VARCHAR(128) NOT NULL," +
                    "billingaddress VARCHAR(512) NOT NULL," +
                    "paymentmethod VARCHAR(128) NOT NULL," +
                    "cardnumber VARCHAR(128)," +
                    "CONSTRAINT customer_fk FOREIGN KEY(customerID) REFERENCES customer(customerID)" +
                    "ON UPDATE CASCADE" +
                    ")");
            statement.executeUpdate("CREATE TABLE services(" +
                    "servicename VARCHAR(128) PRIMARY KEY NOT NULL," +
                    "fees FLOAT(8,2) NOT NULL" +
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
        statement.executeUpdate("INSERT INTO room connectToDatabase();VALUES (3,2,'Economy',1,100,'YES')");
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

    private static void insertBillingInfo(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO billingInfo VALUES(1001,'593-9846','980 TRT St , Raleigh NC','credit','1052')");
        statement.executeUpdate("INSERT INTO billingInfo VALUES(1002,'777-8352','7720 MHT St , Greensboro NC','hotel credit','3020')");
        statement.executeUpdate("INSERT INTO billingInfo VALUES(1003,'858-9430','231 DRY St , Rochester NY 78','credit','2497')");
        statement.executeUpdate("INSERT INTO billingInfo VALUES(1004,'440-9328','24 BST Dr , Dallas TX 14','cash','')");
      }
      catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }

    }

    private static void insertServices(){
      try{
        connectToDatabase();

        statement.executeUpdate("INSERT INTO services VALUES('phone bills',5)");
        statement.executeUpdate("INSERT INTO services VALUES('dry cleaning',16)");
        statement.executeUpdate("INSERT INTO services VALUES('gyms',15)");
        statement.executeUpdate("INSERT INTO services VALUES('room service',10)");
        statement.executeUpdate("INSERT INTO services VALUES('special requests',20)");
      }
      catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }

    }

    private  static void informationProcessing(){
      System.out.printf("Choose what you want to do with informationProcessing:\n");
      System.out.printf("1.enterInfo\n");
      System.out.printf("2.updateInfo\n");
      System.out.printf("3.deleteInfo\n");
      System.out.printf("4.Back to main menu\n");
      System.out.printf("5.Exit\n");
      int choiceB;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceB = secondMenuChoice.nextInt();
      if (choiceB==1){
        enterInfo();
      }
      else if (choiceB==2) {
        updateInfo();
      }

    else if (choiceB==3) {
      deleteInfo();
    }
    else if (choiceB==4) {
      mainmenu();
    }
    else{
      otherNumber();
    }
    }
    private  static void maintainingServiceRecords(){

      System.out.printf("Choose what you want to do with maintainingServiceRecords:\n");
      System.out.printf("1.enterInfo\n");
      System.out.printf("2.updateInfo\n");
    }
    private  static void reports(){
      System.out.printf("Choose what you want to do with reports");
    }
    private  static void otherNumber(){
      System.out.printf("Goodbye!");
    }

    private static void enterInfo(){


      System.out.println("Choose which table are you going to modify:\n");
      System.out.println("1.Hotel:\n");
      System.out.println("2.room\n");
      System.out.println("3.staff\n");
      System.out.println("4.customer\n");
      int choiceC;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceC = secondMenuChoice.nextInt();
      if (choiceC==1){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.println("Input hotelID\n");
        int hotelID=thirdMenuChoice.nextInt();
        System.out.println("Input phonenumber\n");
        int phonenumber=thirdMenuChoice.nextInt();
        System.out.println("Input managerID:\n");
        int managerID=thirdMenuChoice.nextInt();
        System.out.println("Input name\n");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String name =fourthMenuChoice.nextLine();
        System.out.println("Input address\n");
        String address =fourthMenuChoice.nextLine();
        try{
          connectToDatabase();
        statement.executeUpdate("INSERT INTO hotel VALUES"+" ('"+hotelID+"','"+name+"','"+address+"','"+phonenumber+"','"+managerID+"');");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
      else{
        mainmenu();
      }
  }

    private static void updateInfo(){

    }


    private static void deleteInfo(){

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
