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
    private static boolean end = false;//stop the main menu loop

    Scanner infoProcessMenuChoice =new Scanner (System.in);
    public static void main(String[] args) {

        initialize();
        while(!end){
          mainmenu();
        }
        //System.out.println("Your choice:");
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
      else if (choiceA==4) {
        System.out.printf("Thank you, Goodbye!");
        end=true;
      }
      else{
        otherNumber();
      }
    }

    private static void initialize(){

      System.out.printf("Initializing...\n");
      dropTables();
      createTables();
      insertHotelTable();
      insertRoomTable();
      insertCustomerTable();
      insertBillingInfo();
      insertServices();
      insertstaff();
      insertcheckin();

      //check whether all data have been loaded successfully
      try{
        statement.executeUpdate("use information_schema;");
        ResultSet rs=statement.executeQuery("select sum(table_rows) from tables where TABLE_SCHEMA = 'zsun12';");
        statement.executeUpdate("use zsun12;");
        rs.next();
        if(rs.getInt(1)==34) {
          System.out.printf("All data have been loaded successfully!\n");
        }
        else {
          System.out.printf("Failed to load data, please check!\n");
          System.exit(0);
        }
        } catch (SQLException e) {
            e.printStackTrace();
          }
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
                    "ON UPDATE CASCADE " +
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
        statement.executeUpdate("INSERT INTO room VALUES (3,2,'Economy',1,100,'Yes')");
        statement.executeUpdate("INSERT INTO room VALUES (2,3,'Executive',3,1000,'No')");
        statement.executeUpdate("INSERT INTO room VALUES (1,4,'Presidential',4,5000,'Yes')");
        statement.executeUpdate("INSERT INTO room VALUES (5,1,'Deluxe',2,200,'Yes')");
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
      System.out.printf("4.roomRequest\n");
      System.out.printf("5.Assign and realease Room\n");
      System.out.printf("6.Back to main menu\n");
      System.out.printf("7.Exit\n");
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
        roomRequest();
      }
      else if (choiceB==5) {
        assignAndRealeaseRoom();
      }
      else if (choiceB==6) {
        mainmenu();
      }
      else if (choiceB==7) {
        end = true;
      }
      else{
        otherNumber();
      }
    }
    private  static void maintainingServiceRecords(){

      System.out.printf("Choose what you want to do with maintainingServiceRecords:\n");
      System.out.printf("1.enterInfo\n");
      System.out.printf("2.updateInfo\n");
      int choiceF;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceF = secondMenuChoice.nextInt();

      if (choiceF == 1){
        Scanner thirdMenuChoice =new Scanner (System.in);
        try{
          connectToDatabase();
          Statement stmt = connection.createStatement();
          ResultSet rs = stmt.executeQuery("select customerID, hotelID, roomnumber, servicesoffered from checkin  ");
          while (rs.next()) {
          int customerID = rs.getInt("customerID");
          int hotelID = rs.getInt("hotelID");
          String servicesoffered = rs.getString("servicesoffered");
          String roomnumber = rs.getString("roomnumber");
          System.out.println("customerID:"+customerID+" hotelID:"+hotelID+" roomnumber:"+roomnumber+" servicesoffered:"+servicesoffered); 
          }

          System.out.println("choose the customerID you want to add service info ");
          String ID = thirdMenuChoice.nextLine();  
          Statement stmt1 = connection.createStatement();
          ResultSet rs1 = stmt1.executeQuery("select servicesoffered from checkin where customerID = "+ID+";");
          while (rs1.next()) {
          String existservice = rs1.getString("servicesoffered");

          Scanner fourthMenuChoice =new Scanner (System.in);  
          System.out.println("enter the info you want to add in the service ");
          String service = thirdMenuChoice.nextLine();    
          statement.executeUpdate("UPDATE checkin  SET servicesoffered = '"+existservice+", "+service+"' where customerID = "+ID+" ;");
          System.out.println("add successfully ");
          }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      if (choiceF == 2){
        Scanner thirdMenuChoice =new Scanner (System.in);
        try{
          connectToDatabase();
          Statement stmt = connection.createStatement();
          ResultSet rs = stmt.executeQuery("select customerID, hotelID, roomnumber, servicesoffered from checkin  ");
          while (rs.next()) {
          int customerID = rs.getInt("customerID");
          int hotelID = rs.getInt("hotelID");
          String servicesoffered = rs.getString("servicesoffered");
          String roomnumber = rs.getString("roomnumber");
          System.out.println("customerID:"+customerID+" hotelID:"+hotelID+" roomnumber:"+roomnumber+" servicesoffered:"+servicesoffered); 
          }

          System.out.println("choose the customerID you want to update service info ");
          String ID = thirdMenuChoice.nextLine();  
          Scanner fourthMenuChoice =new Scanner (System.in);  
          System.out.println("enter the info you want to update in the service ");
          String service = thirdMenuChoice.nextLine();    
          statement.executeUpdate("UPDATE checkin  SET servicesoffered = '"+service+"' where customerID = "+ID+" ;");
          System.out.println("update successfully ");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
    }

    
    private  static void reports(){
      System.out.println("Choose what you want to do with reports:");
      System.out.println("1.Report occupancy by hotel, room type, date range, and city");
      System.out.println("2.Report total occupancy and percentage of rooms occupied");
      System.out.println("3.Return information on staff grouped by their role");
      System.out.println("4.For each customer stay, return information on all the staff members serving the customer during the stay");
      System.out.println("5.Generate revenue earned by a given hotel during a given date range");
      int choiceC;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceC = secondMenuChoice.nextInt();
      
      if (choiceC==1){
       try{
	        connectToDatabase();
	        Statement stmt = connection.createStatement();

	        ResultSet rs = stmt.executeQuery("select D.name as hotelname, if(C.B is null, 0, C.B) as occupancy from "+
	                                         "(select hotelID,count(*) as B from room where availability = 'No' group by hotelID) C "+
	                                         "right join (select * from hotel) D on C.hotelID=D.hotelID;");
	        System.out.println("************Report occupancy by hotel************"); 
	        while (rs.next()) {
	        	String hotelname = rs.getString("hotelname");
	        	int occupancy = rs.getInt("occupancy");
	        	System.out.println(hotelname+" :"+occupancy); 
	        }
	  
	        rs = stmt.executeQuery("select D.A as roomcategory, if(C.B is null, 0, C.B) as occupancy from "+
	                                         "(select roomcategory as A,count(*) as B from room where availability = 'No' group by roomcategory) C "+
																		       "right join (select distinct roomcategory as A from room) D on C.A=D.A;");
	        System.out.println("************Report occupancy by room type************"); 
	        while (rs.next()) {
	        	String roomcategory = rs.getString("roomcategory");
	        	int occupancy = rs.getInt("occupancy");
	        	System.out.println(roomcategory+" :"+occupancy); 
	        }

	        rs = stmt.executeQuery("select selected_date, count(*) as occupancy from (select selected_date from  "+
	                              "(select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) selected_date from  "+
	                              "(select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union "+
	                              "select 6 union select 7 union select 8 union select 9) t0,  (select 0 t1 union select 1 union "+
	                              "select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union "+
	                              "select 8 union select 9) t1,  (select 0 t2 union select 1 union select 2 union select 3 union "+
	                              "select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,  (select 0 t3 union "+
	                              "select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union "
	                              +"select 8 union select 9) t3,  (select 0 t4 union select 1 union select 2 union select 3 union select 4 union "+
	                              "select 5 union select 6 union select 7 union select 8 union select 9) t4) v, "+
	                              "checkin where selected_date between checkin.startdate and checkin.enddate) X group by selected_date;");
	        System.out.println("************Report occupancy by daterange************"); 
	        while (rs.next()) {
	        	String selected_date = rs.getString("selected_date");
	        	int occupancy = rs.getInt("occupancy");
	        	System.out.println(selected_date+" :"+occupancy); 
	        }
	        
	        rs = stmt.executeQuery("select D.address as city, if(C.B is null, 0, C.B) as occupancy from  "+
	                              "(select hotelID,count(*) as B from room where availability = 'No' group by hotelID) C  "+
	                              "right join (select * from hotel) D on C.hotelID=D.hotelID;");
	        System.out.println("************Report occupancy by city************"); 
	        while (rs.next()) {
	        	String city = rs.getString("city");
	        	int occupancy = rs.getInt("occupancy");
	        	System.out.println(city+" :"+occupancy); 
	        }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

      }

      else if (choiceC==2) {
        try{
	        connectToDatabase();
	        Statement stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery("select count(*) as occupancy, (count(*)/(select count(*)from room))*100 as percentage from room where availability = 'No';");
	        while (rs.next()) {
	        	int occupancy = rs.getInt("occupancy");
	        	Float percentage = rs.getFloat("percentage");
	        	System.out.println("occupancy:"+occupancy+" percentage:"+percentage); 
	        }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      else if (choiceC==3) {
        try{
	        connectToDatabase();
	        Statement stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery("select * from staff order by jobtitle;");
	        System.out.println("staffID name age jobtitle hotelID department phonenumber address"); 
	        while (rs.next()) {
	        	int staffID = rs.getInt("staffID");
	        	String name = rs.getString("name");
	        	int age = rs.getInt("age");
	        	String jobtitle = rs.getString("jobtitle");
	        	int hotelID = rs.getInt("hotelID");
	        	String department = rs.getString("department");
	        	int phonenumber = rs.getInt("phonenumber");
	        	String address = rs.getString("address");
	        	System.out.println(""+staffID+" "+name+" "+age+" "+jobtitle+" "+hotelID+" "+department+" "+phonenumber+" "+address); 
	        }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
      
      else if (choiceC==4) {
      	Scanner thirdMenuChoice =new Scanner (System.in);
      	System.out.println("For each customer stay, return information on all the staff members serving the customer during the stay");
        try{
          connectToDatabase();
          statement.executeUpdate(";");
          System.out.println("successfully!");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      else if (choiceC==5) {
        try{
        	Scanner thirdMenuChoice =new Scanner (System.in);
	        System.out.printf("Input hotelID:");
	        int hotelID=thirdMenuChoice.nextInt();
	        System.out.printf("Input startdate:");
	        Scanner fourthMenuChoice =new Scanner (System.in);
	        String startdate =fourthMenuChoice.nextLine();
	        System.out.printf("Input enddate:");
	        String enddate =fourthMenuChoice.nextLine();
	        	        
	        connectToDatabase();
	        Statement stmt = connection.createStatement();
	        ResultSet rs1 = stmt.executeQuery("select sum((c.enddate-c.startdate)*r.nightlyrate) as roomfee from checkin c, room r "+
	                                          "where c.roomnumber=r.roomnumber and c.hotelID=r.hotelID and c.startdate>'"+startdate+"' and c.enddate<'"+enddate+"' "+
	                                          "and c.hotelID="+hotelID+";");
	        ResultSet rs2 = stmt.executeQuery("select sum(s.fees) as servicesfee from checkin c, services s "+
	                                          "where c.servicesoffered like concat('%',s.servicename,'%') and c.startdate>'"+startdate+"' and c.enddate<'"+enddate+"' "+
	                                          "and c.hotelID="+hotelID+";");
	        rs1.next();
	        int roomfee=rs1.getInt("roomfee");
	        rs2.next();
	        int servicesfee=rs2.getInt("servicesfee");
	        int totalfee=roomfee+servicesfee;
	        System.out.println("The revenue earned by hotel "+hotelID+" during "+startdate+" and "+enddate+" is: "+totalfee); 

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
    private  static void otherNumber(){
      System.out.printf("The wrong choice, please do again\n");
    }

    private static void enterInfo(){


      System.out.println("Choose which table are you going to modify:");
      System.out.println("1.Hotel");
      System.out.println("2.room");
      System.out.println("3.staff");
      System.out.println("4.customer");
      int choiceC;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceC = secondMenuChoice.nextInt();
      if (choiceC==1){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input hotelID:");
        int hotelID=thirdMenuChoice.nextInt();
        System.out.printf("Input phonenumber:");
        int phonenumber=thirdMenuChoice.nextInt();
        System.out.printf("Input managerID:");
        int managerID=thirdMenuChoice.nextInt();
        System.out.printf("Input name:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String name =fourthMenuChoice.nextLine();
        System.out.printf("Input address:");
        String address =fourthMenuChoice.nextLine();
        try{
          connectToDatabase();
          statement.executeUpdate("INSERT INTO hotel VALUES"+" ('"+hotelID+"','"+name+"','"+address+"','"+phonenumber+"','"+managerID+"');");
          System.out.println("Hotel "+name+" has been added successfully!");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

      }

      else if (choiceC==2) {
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input roomnumber:");
        int roomnumber=thirdMenuChoice.nextInt();
        System.out.printf("Input hotelID:");
        int hotelID=thirdMenuChoice.nextInt();
        System.out.printf("Input roomcategory:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String roomcategory =fourthMenuChoice.nextLine();
        System.out.printf("Input maxcapacityallowed:");
        int maxcapacityallowed=thirdMenuChoice.nextInt();
        System.out.printf("Input nightlyrate:");
        float nightlyrate=thirdMenuChoice.nextFloat();
        System.out.printf("Input availability:");
        String availability =fourthMenuChoice.nextLine();
        try{
          connectToDatabase();
          statement.executeUpdate("INSERT INTO room VALUES"+" ('"+roomnumber+"','"+hotelID+"','"+roomcategory+"','"+maxcapacityallowed+"','"+nightlyrate+"','"+availability+"');");
          System.out.println("Room "+roomnumber+" has been added successfully!");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }


    else if (choiceC==3) {
      Scanner thirdMenuChoice =new Scanner (System.in);
      System.out.printf("Input staffID:");
      int staffID=thirdMenuChoice.nextInt();
      System.out.printf("Input name:");
      Scanner fourthMenuChoice =new Scanner (System.in);
      String name =fourthMenuChoice.nextLine();
      System.out.printf("Input age:");
      int age=thirdMenuChoice.nextInt();
      System.out.printf("Input jobtitle:");
      String jobtitle =fourthMenuChoice.nextLine();
      System.out.printf("Input hotelID:");
      int hotelID=thirdMenuChoice.nextInt();
      System.out.printf("Input department:");
      String department =fourthMenuChoice.nextLine();
      System.out.printf("Input phonenumber:");
      int phonenumber=thirdMenuChoice.nextInt();
      System.out.printf("Input address:");
      String address =fourthMenuChoice.nextLine();
      try{
        connectToDatabase();
        statement.executeUpdate("INSERT INTO staff VALUES"+" ('"+staffID+"','"+name+"','"+age+"','"+jobtitle+"','"+hotelID+"','"+department+"','"+phonenumber+"','"+address+"');");
        System.out.println("Staff "+name+" has been added successfully!");
      }catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }

      else if (choiceC==4) {
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input customerID:");
        int customerID=thirdMenuChoice.nextInt();
        System.out.printf("Input name:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String name =fourthMenuChoice.nextLine();
        System.out.printf("Input birthday:");
        String birthday =fourthMenuChoice.nextLine();
        System.out.printf("Input phonenumber:");
        String phonenumber =fourthMenuChoice.nextLine();
        System.out.printf("Input email:");
        String email =fourthMenuChoice.nextLine();
        try{
          connectToDatabase();
          statement.executeUpdate("INSERT INTO customer VALUES"+" ('"+customerID+"','"+name+"','"+birthday+"','"+phonenumber+"','"+email+"');");
          System.out.println("Customer "+name+" has been added successfully!");
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

      System.out.println("Choose which table are you going to update:");
      System.out.println("1.Hotel");
      System.out.println("2.room");
      System.out.println("3.staff");
      System.out.println("4.customer");
      int choiceC;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceC = secondMenuChoice.nextInt();
      //update hotel
      if (choiceC==1){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input hotelID:");
        int hotelID=thirdMenuChoice.nextInt();
        System.out.printf("Input phonenumber:");
        int phonenumber=thirdMenuChoice.nextInt();
        System.out.printf("Input managerID:");
        int managerID=thirdMenuChoice.nextInt();
        System.out.printf("Input name:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String name =fourthMenuChoice.nextLine();
        System.out.printf("Input address:");
        String address =fourthMenuChoice.nextLine();
        try{
          connectToDatabase();
          statement.executeUpdate("UPDATE hotel SET name ='"+name+"', address ='"+address+"', phonenumber ='"+phonenumber+"', managerID ='"+managerID+"' WHERE hotelID ="+hotelID+";");
          System.out.println("HotelID "+hotelID+" has been updated successfully!");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
      //update room
      if (choiceC==2){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input roomnumber:");
        int roomnumber=thirdMenuChoice.nextInt();
        System.out.printf("Input hotelID:");
        int hotelID=thirdMenuChoice.nextInt();
        System.out.printf("Input roomcategory:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String roomcategory =fourthMenuChoice.nextLine();
        System.out.printf("Input maxcapacityallowed:");
        int maxcapacityallowed=thirdMenuChoice.nextInt();
        System.out.printf("Input nightlyrate:");
        float nightlyrate=thirdMenuChoice.nextFloat();
        System.out.printf("Input availability:");
        String availability =fourthMenuChoice.nextLine();
        try{
          connectToDatabase();
          statement.executeUpdate("UPDATE room SET roomcategory ='"+roomcategory+"', maxcapacityallowed ="+maxcapacityallowed+", nightlyrate ="+nightlyrate+", availability ='"+availability+"' WHERE roomnumber ="+roomnumber+" AND hotelID ="+hotelID+";");
          System.out.println("Roomnumber "+roomnumber+" has been updated successfully!");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      //update staff
      if (choiceC==3){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input staffID:");
        int staffID=thirdMenuChoice.nextInt();
        System.out.printf("Input name:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String name =fourthMenuChoice.nextLine();
        System.out.printf("Input age:");
        int age=thirdMenuChoice.nextInt();
        System.out.printf("Input jobtitle:");
        String jobtitle =fourthMenuChoice.nextLine();
        System.out.printf("Input hotelID:");
        int hotelID=thirdMenuChoice.nextInt();
        System.out.printf("Input department:");
        String department =fourthMenuChoice.nextLine();
        System.out.printf("Input phonenumber:");
        int phonenumber=thirdMenuChoice.nextInt();
        System.out.printf("Input address:");
        String address =fourthMenuChoice.nextLine();

        try{
          connectToDatabase();
          statement.executeUpdate("UPDATE staff SET name ='"+name+"', age ="+age+", jobtitle ='"+jobtitle+"', hotelID ="+hotelID+", department ='"+department+"', phonenumber ="+phonenumber+", address ='"+address+"' WHERE staffID ="+staffID+";");
          System.out.println("staffID "+staffID+" has been updated successfully!");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      //update customer
      if (choiceC==4){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.printf("Input customerID:");
        int customerID=thirdMenuChoice.nextInt();
        System.out.printf("Input name:");
        Scanner fourthMenuChoice =new Scanner (System.in);
        String name =fourthMenuChoice.nextLine();
        System.out.printf("Input birthday:");
        String birthday =fourthMenuChoice.nextLine();
        System.out.printf("Input phonenumber:");
        String phonenumber =fourthMenuChoice.nextLine();
        System.out.printf("Input email:");
        String email =fourthMenuChoice.nextLine();

        try{
          connectToDatabase();
          statement.executeUpdate("UPDATE customer SET name ='"+name+"', birthday ='"+birthday+"', phonenumber ='"+phonenumber+"', email ='"+email+"' WHERE customerID ="+customerID+";");
          System.out.println("customerID "+customerID+" has been updated successfully!");
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


private static void deleteInfo(){

      System.out.println("Choose which table are you going to delete info on:");
      System.out.println("1.hotel");
      System.out.println("2.room");
      System.out.println("3.staff");
      System.out.println("4.customer");
      int choiceD;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceD = secondMenuChoice.nextInt();
        if (choiceD == 1){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.println("enter the hotelID to delete its info");
        int hotelID=thirdMenuChoice.nextInt();
        try{
          connectToDatabase();
          Statement stmt = connection.createStatement();
          stmt.execute("SET FOREIGN_KEY_CHECKS=0");
          statement.executeUpdate("delete from hotel where hotelID ="+hotelID+"; ");
          statement.executeUpdate("delete from room where hotelID ="+hotelID+"; ");
          statement.executeUpdate("delete from staff where hotelID ="+hotelID+"; ");
          System.out.println("info of hotel hotelid "+hotelID+" has been deleted successfully");
          System.out.println("info of room and staff with hotelid "+hotelID+" has also been deleted ");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      if (choiceD == 2){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.println("enter the roomID to delete its info");
        int roomID=thirdMenuChoice.nextInt();
        try{
          connectToDatabase();
          Statement stmt = connection.createStatement();
          stmt.execute("SET FOREIGN_KEY_CHECKS=0");
          statement.executeUpdate("delete from hotel where roomID ="+roomID+"; ");
          System.out.println("info of roomID "+roomID+" has been deleted successfully");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }

      if (choiceD == 3){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.println("enter the staffID to delete its info");
        int staffID=thirdMenuChoice.nextInt();
        try{
          connectToDatabase();
          Statement stmt = connection.createStatement();
          stmt.execute("SET FOREIGN_KEY_CHECKS=0");
          statement.executeUpdate("delete from staff where staffID ="+staffID+"; ");
          System.out.println("info of staffID "+staffID+" has been deleted successfully");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }


      if (choiceD == 4){
        Scanner thirdMenuChoice =new Scanner (System.in);
        System.out.println("enter the customerID to delete its info");
        int customerID=thirdMenuChoice.nextInt();
        try{
          connectToDatabase();
          Statement stmt = connection.createStatement();
          stmt.execute("SET FOREIGN_KEY_CHECKS=0");
          statement.executeUpdate("delete from customer where customerID ="+customerID+"; ");
          System.out.println("info of customerID "+customerID+" has been deleted successfully");
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

    private static void roomRequest(){
      System.out.println("1.Choose a roomnumber");
      System.out.println("2.Choose a roomtype");
      int choiceD;
      Scanner secondMenuChoice =new Scanner (System.in);
      choiceD = secondMenuChoice.nextInt();
      if(choiceD==1){
          System.out.println("roomnumber?:\n");
          Scanner thirdMenuChoice =new Scanner (System.in);
          int roomID=thirdMenuChoice.nextInt();
          System.out.println("Which hotel?:\n");
          int hotelID=thirdMenuChoice.nextInt();
          try{
          connectToDatabase();
          ResultSet result =statement.executeQuery("select count(*) from room where roomnumber='"+roomID+"'and hotelID='"+hotelID+"';");
          while(result.next())
          {
            String a=result.getString(1);
            System.out.println("The number of available room is "+a);
          }
          mainmenu();
      }catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }
      else if (choiceD==2) {
        System.out.println("roomtype?:\n");
        Scanner thirdMenuChoice =new Scanner (System.in);
        String roomcategory=thirdMenuChoice.next();
        try{
          connectToDatabase();
          ResultSet result=statement.executeQuery("select count(*) from room where roomcategory='"+roomcategory+"'and availability='Yes';");
          while(result.next())
          {
            String a=result.getString(1);
            System.out.println("The number of available room is "+a);
          }
          mainmenu();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
    }

    private static void assignAndRealeaseRoom(){
      Scanner secondMenuChoice =new Scanner (System.in);
      Scanner thirdMenuChoice =new Scanner (System.in);
      System.out.println("Please enter the customerID:");
      int customerID=secondMenuChoice.nextInt();
      System.out.println("Please enter the roomnumber the customer request:");
      int roomnumber =secondMenuChoice.nextInt();
      System.out.println("Please enter the hotelID the customer is in:");
      int hotelID=secondMenuChoice.nextInt();
      System.out.println("Please enter the number of guests:");
      int numberofguests=secondMenuChoice.nextInt();
      System.out.println("Please enter the checkin date:");
      String startdate=thirdMenuChoice.nextLine();
      System.out.println("Please enter the checkout date:");
      String enddate=thirdMenuChoice.nextLine();
      System.out.println("Please enter the checkin time:");
      String checkintime=thirdMenuChoice.nextLine();
      System.out.println("Please enter the checkout time:");
      String checkouttime=thirdMenuChoice.nextLine();
      System.out.println("Please enter service offered:");
      String servicesoffered=thirdMenuChoice.nextLine();

      try{
        connectToDatabase();
        statement.executeUpdate("INSERT INTO checkin VALUES"+" ('"+customerID+"','"+hotelID+"','"+roomnumber+"','"+numberofguests+"','"+startdate+"','"+enddate+"','"+checkintime+"','"+checkouttime+"','"+servicesoffered+"');");
        System.out.println("checkinInfo been added successfully!");
        statement.executeUpdate("UPDATE room SET availability ='No' WHERE roomnumber ='"+roomnumber+"';");
        System.out.println("Room"+roomnumber+"released!");
      }catch (ClassNotFoundException e) {
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
