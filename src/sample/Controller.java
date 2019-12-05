package sample;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {

  @FXML
  private TextField userNameField;

  @FXML
  private Text forgotUser;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Text forgotPass;

  @FXML
  private Button loginBttn;

  @FXML
  private Button createAcctBttn;

  static DatabaseConn db = new DatabaseConn();
  //static String user;
  static Person currentUser;

  public void createAcctBttnClicked(){

//    String username = userNameField.getText();
//    String password = passwordField.getText();
//    if(username.equals("") || password.equals("")){
//      Alert missingInfo = new Alert(AlertType.ERROR,"Did not enter Username "
//          + "and/or Password");
//      missingInfo.setTitle("Incomplete Account");
//      missingInfo.showAndWait();
//      return;
//    }
//    db.insertValues(username,password);
//    Alert accountCreated = new Alert(AlertType.CONFIRMATION,"You have "
//        + "successfully created and account.");
//    accountCreated.setTitle("Account Created!");
//    accountCreated.showAndWait();
//
    Stage stage = Main.getPrimaryStage();

    try {
      Parent root = FXMLLoader.load(getClass().getResource("createAccount.fxml"));
      stage.setScene(new Scene(root,600,430));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void loginBttnClicked(){
    String username = userNameField.getText();
    String password = passwordField.getText();

    try{
      Statement statement = DatabaseConn.conn.createStatement();
      ResultSet res = statement.executeQuery("SELECT * from ACCOUNTS");
      while (res.next()){
        String rightUser = res.getString("USERNAME");
        String rightPass = res.getString("PASSWORD");
        //System.out.println(rightUser + " " + rightPass);
        if(rightPass.equals(password) && rightUser.equals(username)){
          System.out.println("Signing in!");
          //we have access to the row of all the data about the Person
          //user = username;
          currentUser = new Person(
              res.getString("USERNAME"),
              res.getString("FIRSTNAME"),
              res.getString("LASTNAME"),
              res.getDate("DOB"),
              res.getString("SOCIALSECNUM"),
              res.getString("EMAIL"),
              res.getString("ADDRESS"),
              res.getInt("ZIP"),
              res.getString("CITY"),
              res.getString("STATE"),
              res.getString("LICENSE"),
              res.getInt("BALANCE"),
              res.getInt("ACCOUNTID"),
              res.getBytes("IDFORM")
          );
          Stage stage = Main.getPrimaryStage();
          Parent root = FXMLLoader.load(getClass().getResource("loggedIn.fxml"));

          stage.setScene(new Scene(root, 1000, 933));
          stage.show();
          return;
        }

      }
      System.out.println("Did not enter right...");
      Alert error = new Alert(AlertType.ERROR,"Invalid credentials...");
      error.setTitle("Could not sign in");
      error.showAndWait();
    } catch (SQLException e){
      e.printStackTrace();
    } catch (IOException f){
      f.printStackTrace();
    }

  }


}
