package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;

public class DatabaseConn {

  public static final String JDBC_DRIVER = "org.h2.Driver";
  public static final String JDBC_URL = "jdbc:h2:./res/MyDB";

  static Connection conn = null;
  static Statement stmt = null;
  static PreparedStatement prepstmt;
  public DatabaseConn() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(JDBC_URL,"","");
      stmt = conn.createStatement();
      if (conn != null) {
        System.out.println("Connected!");
        //createTable(); //create table one time
        //createCheckingTable();
        //createSavingsTable();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void createTable(){
    String sql = "create table ACCOUNTS(\n"
        + "AccountID int NOT NULL AUTO_INCREMENT,\n"
        + "Username varchar(20),\n"
        + "Password varchar(20),\n"
        + "FirstName varchar(25),\n"
        + "MiddleInitial varchar(1),\n"
        + "LastName varchar(25),\n"
        + "DOB date,\n"
        + "SocialSecNum varchar(9),\n"
        + "Email varchar(40),\n"
        + "Address varchar(50),\n"
        + "Zip integer,\n"
        + "City varchar(20),\n"
        + "State varchar(15),\n"
        + "License varchar(13),\n"
        + "Balance integer,\n"
        + "IDForm varbinary,\n"
        + "PRIMARY KEY (AccountID)\n"
        + ");";
    try {
      conn.createStatement().execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void createCheckingTable(){
    String sql = "create table CHECKING (\n"
        + "  CheckID int NOT NULL AUTO_INCREMENT,\n"
        + "  AccountID int,\n"
        + "  Balance integer,\n"
        + "  primary key (CheckID),\n"
        + "  foreign key (AccountID) references ACCOUNTS(AccountID)\n"
        + ")";
    try {
      conn.createStatement().execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void createSavingsTable(){
    String sql = "create table SAVINGS (\n"
        + "  SaveID int NOT NULL AUTO_INCREMENT,\n"
        + "  AccountID int,\n"
        + "  Balance integer,\n"
        + "  primary key (SaveID),\n"
        + "  foreign key (AccountID) references ACCOUNTS(AccountID)\n"
        + ")";
    try {
      conn.createStatement().execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void insertValues(String user, String password){
    //String sql = "INSERT INTO Accounts(USERNAME, PASSWORDS) VALUES ('" + user + "','" + password + "')";
    String sql = "INSERT INTO Accounts(USERNAME, PASSWORDS) VALUES (?,?)";

    try {
      prepstmt = conn.prepareStatement(sql);
      prepstmt.setString(1,user);
      prepstmt.setString(2,password);
      prepstmt.executeUpdate();
//      System.out.println("trying to execute: " + sql);
//      conn.createStatement()
//          .execute(sql);
//      conn.createStatement().execute("SELECT * FROM ACCOUNTS");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


}
