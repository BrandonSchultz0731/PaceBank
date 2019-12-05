package sample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoggedInController {

  @FXML
  private Text welcomeLabel;

  @FXML
  private Text fullNameText;
  @FXML
  private Text dobText;
  @FXML
  private Text fullAddressText;
  @FXML
  private Text numOfAcctText;
  @FXML
  private Text usernameText;
  @FXML
  private Text lastTransactionText;

  @FXML
  private Label currentBalanceLbl;

  @FXML
  private TextField depositAmtField;

  @FXML
  private Button depositBttn;

  @FXML
  private Button signOffBttn;

  @FXML
  private ImageView testImage;

  private ObservableList<ModelProductTable> data;
  @FXML
  private TableView<ModelProductTable> savingsTable;
  @FXML
  private TableColumn<ModelProductTable,Integer> savingsCol; //column for savings
  @FXML
  private TableColumn<ModelProductTable,Integer> accountCol; //column for savings
  @FXML
  private TableColumn<ModelProductTable,Integer> balanceCol; // column for savings
  @FXML
  private TableView<ModelProductTable> checkingTable;
  @FXML
  private TableColumn<ModelProductTable,Integer> checkingCol; //column for checking
  @FXML
  private TableColumn<ModelProductTable,Integer> accountCol1; //column for checking
  @FXML
  private TableColumn<ModelProductTable,Integer> balanceCol1; // column for checking

  static Person currentUser = Controller.currentUser;



  public void initialize(){
    currentUser = Controller.currentUser;
    loadData();
  }

  private void loadTable(String sql,String columnLabel) {
    data = FXCollections.observableArrayList();
    try {
      ResultSet rs = DatabaseConn.conn.createStatement().executeQuery(sql);
      while (rs.next()){
        data.add(new ModelProductTable(rs.getInt(columnLabel),
            rs.getInt("AccountID"),rs.getInt("Balance")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    switch (columnLabel){
      case "SAVEID":
        //we want to load the savings table with data
        savingsCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        accountCol.setCellValueFactory(new PropertyValueFactory<>("AccountID"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("Balance"));
        savingsTable.setItems(data);
        break;
      case "CHECKID":
        //load checking table with data
        checkingCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        accountCol1.setCellValueFactory(new PropertyValueFactory<>("AccountID"));
        balanceCol1.setCellValueFactory(new PropertyValueFactory<>("Balance"));
        checkingTable.setItems(data);
        break;
    }


  }

  public void loadData(){
    welcomeLabel.setText("Welcome: " + Controller.currentUser.getFirstName());
    //to get the balance, run a sql command that selects the balances from each
    //table where the AcctID = currentID and sum it up
    int sum = 0;
    String sumOfChecking = "select sum(Balance) from CHECKING "
        + "where AccountID = " + currentUser.getAccountID() + ";";
    String sumOfSavings = "select sum(Balance) from SAVINGS "
        + "where AccountID = " + currentUser.getAccountID() + ";";
    String sumOfMoneyMarket;

    try {
      PreparedStatement prepstmt = DatabaseConn.conn.prepareStatement(sumOfChecking);
      ResultSet rs = prepstmt.executeQuery();
      if(rs.next() && rs.getString("SUM(BALANCE)") != null){
        //System.out.println(rs.getString("SUM(BALANCE)"));
        sum += Integer.parseInt(rs.getString("SUM(BALANCE)"));
      }
      prepstmt = DatabaseConn.conn.prepareStatement(sumOfSavings);
      rs = prepstmt.executeQuery();
      if(rs.next() && rs.getString("SUM(BALANCE)") != null){
        sum += Integer.parseInt(rs.getString("SUM(BALANCE)"));
      }
      currentBalanceLbl.setText("$" + String.valueOf(sum));
      String updateBalance = "UPDATE ACCOUNTS\n"
          + "set Balance = " + sum + "\n"
          + "where AccountID = " + currentUser.getAccountID();
      prepstmt = DatabaseConn.conn.prepareStatement(updateBalance);
      prepstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    String sql = "select * from SAVINGS where AccountID = " + currentUser.getAccountID() + ";";
    loadTable(sql,"SAVEID");
    sql = "select * from CHECKING where AccountID = " + currentUser.getAccountID() + ";";
    loadTable(sql,"CHECKID");

    //currentBalanceLbl.setText("$" + currentUser.getUserBalance());
  }
  public void checkingClicked(){
    System.out.println("You chose checking!");

    //allow the user to deposit a certain amount into checking account
    Stage s = new Stage();
    try {
      Parent p = FXMLLoader.load(getClass().getResource("checking.fxml"));
      s.setScene(new Scene(p,600,600));
      s.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void savingsClicked(){
    System.out.println("You chose Savings!");

    Stage s = new Stage();
    try {
      Parent p = FXMLLoader.load(getClass().getResource("savings.fxml"));
      s.setScene(new Scene(p,600,600));
      s.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void depositClicked(){
    ModelProductTable c = checkingTable.getSelectionModel().getSelectedItem();
    ModelProductTable s = savingsTable.getSelectionModel().getSelectedItem();
    String sql;
    PreparedStatement prepstmt;
    String acctSelected = "";
    int acctID = 0;

    if(depositAmtField.getText().equals("")){
      Alert empty = new Alert(AlertType.ERROR,"Please enter a value to deposit");
      empty.setTitle("Deposit not entered");
      empty.showAndWait();
      return;
    }
    int depositAmt = 0;
    try{
      depositAmt = Integer.parseInt(depositAmtField.getText());
    } catch (NumberFormatException n){
      Alert badInput = new Alert(AlertType.ERROR,"You did not enter a number. Please "
          + "try again and enter a valid amount to deposit.");
      badInput.setTitle("Bad input");
      badInput.showAndWait();
      return;
    }
    if(s == null && c == null){
      Alert select = new Alert(AlertType.ERROR,"Please select an account from table "
          + "the table that you would like to make a deposit to.");
      select.setTitle("Select Account");
      select.showAndWait();
      return;
    }
    else if(s != null && c != null){
      Alert tooManyVal = new Alert(AlertType.ERROR,"Too many accounts have been selected."
          + " Please make sure you only have one account selected from one table only.");
      tooManyVal.setTitle("Too Many Accounts");
      tooManyVal.showAndWait();
      //something is selected, find out which one
      checkingTable.getSelectionModel().clearSelection();
      savingsTable.getSelectionModel().clearSelection();
      return;

    }

    else if(s != null){
      //they selected a savings account
      System.out.println("You selected a savings account wiht bal " + s.getBalance());
      acctSelected = "Savings Account";
      acctID = s.getProductID();
      int newBal = s.getBalance() + depositAmt;
      sql = "UPDATE SAVINGS\n"
          + "set Balance = " + newBal + "\n"
          + "where SAVEID = " + s.getProductID();
      try {
        prepstmt = DatabaseConn.conn.prepareStatement(sql);
        prepstmt.executeUpdate();

      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    else{
      //checking is slected
      acctSelected = "Checking Account";
      acctID = c.getProductID();
      int newBal = c.getBalance() + depositAmt;
      sql = "UPDATE CHECKING\n"
          + "set Balance = " + newBal + "\n"
          + "where CHECKID = " + c.getProductID();
      try {
        prepstmt = DatabaseConn.conn.prepareStatement(sql);
        prepstmt.executeUpdate();

      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    Alert deposit = new Alert(AlertType.CONFIRMATION,"You have successfully "
        + "deposited $" + depositAmt + " into your account.");
    deposit.setTitle("Deposit Made");
    deposit.show();
    lastTransactionText.setText("Your last transaction was:\nDeposited $" + depositAmtField.getText() +
        " into " + acctSelected + " " + "with ID " + acctID + " on " + dtf.format(now));
    loadData();
  }
  public void loadProfile() throws SQLException {
    PreparedStatement prepstm;
    ResultSet rs;
    fullNameText.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    fullAddressText.setText(currentUser.getMainAddress() + " " + currentUser.getCity() +
        ", " + currentUser.getState() + " " + currentUser.getZipCode());
    usernameText.setText(currentUser.getUsername());

    //get number of current open accounts
    String sql = "select count(SAVINGS.AccountID) as CountSavings from SAVINGS where ACCOUNTID = " +
        currentUser.getAccountID() + ";";
    prepstm = DatabaseConn.conn.prepareStatement(sql);
    rs = prepstm.executeQuery();
    int savingAccountCount = 0;
    if(rs.next()){
      savingAccountCount += rs.getInt("CountSavings");
    }
    sql = "select count(CHECKING.AccountID) as CountChecking from CHECKING where ACCOUNTID = " +
        currentUser.getAccountID() + ";";
    prepstm = DatabaseConn.conn.prepareStatement(sql);
    rs = prepstm.executeQuery();
    int checkingAccountCount = 0;
    if(rs.next()){
      checkingAccountCount += rs.getInt("CountChecking");
    }
    numOfAcctText.setText(savingAccountCount + " Savings Account(s) and " + checkingAccountCount +
        " Checking Account(s)");
    dobText.setText(currentUser.calcAge() + " years old");
    //ONLY WORKS IF IMAGE IS PNG,JPG,etc...
    sql = "select IDForm from ACCOUNTS where AccountID = "
        + currentUser.getAccountID() + ";";
    prepstm = DatabaseConn.conn.prepareStatement(sql);
    rs = prepstm.executeQuery();
    byte[] a = null;
    if(rs.next()){
      a = rs.getBytes(1);
    }
    Image image = new Image(new ByteArrayInputStream(a));
    testImage.setImage(image);
  }
  public void signOffBttnClicked(){
    Stage stage = Main.getPrimaryStage();
    Controller.currentUser = null;

    try {
      Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
      stage.setScene(new Scene(root,683,489));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
