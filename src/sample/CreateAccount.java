package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateAccount {

  @FXML
  private TextField firstNameField;

  @FXML
  private TextField middleInitialField;

  @FXML
  private TextField lastNameField;

  @FXML
  private TextField userNameField;

  @FXML
  private TextField passwordField;

  @FXML
  private DatePicker dateOfBirthField;

  @FXML
  private TextField socialSecField;

  @FXML
  private TextField emailField;

  @FXML
  private TextField phoneField;

  @FXML
  private TextField mainAddressField;

  @FXML
  private TextField zipField;

  @FXML
  private TextField cityField;

  @FXML
  private TextField stateField;

  @FXML
  private MenuButton menuButton;

  @FXML
  private TextField licenseField;

  @FXML
  private Label enterLicenseLbl;

  @FXML
  private Label uploadIdLbl;

  @FXML
  private Button fileBttn;

  @FXML
  private Button finishedButton;

  public static byte[] fileArray;

  public void yesSelected(){
    //System.out.println("selected yes");
    menuButton.setText("Yes");
    enterLicenseLbl.setVisible(true);
    licenseField.setVisible(true);
    uploadIdLbl.setVisible(false);
    fileBttn.setVisible(false);
  }
  public void noSelected(){
    //System.out.println("selceted no");
    menuButton.setText("No");
    enterLicenseLbl.setVisible(false);
    licenseField.setVisible(false);
    uploadIdLbl.setVisible(true);
    fileBttn.setVisible(true);
  }
  public void fileBttnClicked(){
    FileChooser fileChooser = new FileChooser();
    try{
      FileChooser.ExtensionFilter imageFilter
          = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
      fileChooser.getExtensionFilters().add(imageFilter);
      File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
      if(file != null){
        System.out.println("File chosen! " + file.getAbsolutePath());
        fileArray = readFileToByteArray(file);

      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private byte[] readFileToByteArray(File file) {
    FileInputStream fis = null;
    // Creating a byte array using the length of the file
    // file.length returns long which is cast to int
    byte[] bArray = new byte[(int) file.length()];
    try{
      fis = new FileInputStream(file);
      fis.read(bArray);
      fis.close();

    }catch(IOException ioExp){
      ioExp.printStackTrace();
    }
    return bArray;
  }

  public void finishClicked(){
    //check to make sure they entered ID
    if(licenseField.getText().equals("") && fileArray == null){
      //they did not select an ID
      Alert error = new Alert(AlertType.ERROR,"You did not enter "
          + "any form of Identification. Please choose 'Yes' or 'No' and enter "
          + "one of the following");
      error.setTitle("No ID Selected");
      error.showAndWait();
      return;
    }
    String sql = "insert into ACCOUNTS(Username, Password, FirstName, MiddleInitial, LastName, DOB,\n"
        + "SocialSecNum, Email, Address, Zip, City, State, License, BALANCE,IDFORM)\n"
        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try {
      PreparedStatement prepstmt = DatabaseConn.conn.prepareStatement(sql);
      prepstmt.setString(1,userNameField.getText());
      prepstmt.setString(2,passwordField.getText());
      prepstmt.setString(3,firstNameField.getText());
      prepstmt.setString(4,middleInitialField.getText().substring(0,1));
      prepstmt.setString(5,lastNameField.getText());
      prepstmt.setDate(6, Date.valueOf(dateOfBirthField.getValue()));
      prepstmt.setString(7,socialSecField.getText());
      prepstmt.setString(8,emailField.getText());
      prepstmt.setString(9,mainAddressField.getText());
      prepstmt.setInt(10,Integer.parseInt(zipField.getText()));
      prepstmt.setString(11,cityField.getText());
      prepstmt.setString(12,stateField.getText());
      prepstmt.setString(13,licenseField.getText());
      prepstmt.setInt(14,0);
      prepstmt.setBytes(15,fileArray);


      prepstmt.executeUpdate();

      Alert success = new Alert(AlertType.CONFIRMATION,"Thank you for creating"
          + " your account. Please sign in using the account you just created.");
      success.setTitle("Account Created");
      success.showAndWait();

      Stage stage = Main.getPrimaryStage();
      Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
      stage.setScene(new Scene(root,800,600));
      stage.show();

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
