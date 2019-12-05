package sample;

import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Savings {

  @FXML
  private TextField depositAmt;

  @FXML
  private Button openSavingsBttn;

  public void openSavingsBttnClicked() {
    String sql = "insert into SAVINGS(AccountID,Balance) values ('" +
        Controller.currentUser.getAccountID() + "',?)";
    try {
      int val = Integer.parseInt(depositAmt.getText());
      if (val < 500) {
        Alert notEnough = new Alert(AlertType.ERROR, "You must deposit at least $500 "
            + "in order to open up a Savings Account.");
        notEnough.setTitle("Not enough deposit");
        notEnough.showAndWait();
        return;
      }
      PreparedStatement prepstmt = DatabaseConn.conn.prepareStatement(sql);
      prepstmt.setInt(1, val);
      prepstmt.executeUpdate();
      Alert alert = new Alert(AlertType.CONFIRMATION, "You successfully created "
          + "a checking account.");
      alert.setTitle("Thank you!");
      alert.showAndWait();
      Stage s = (Stage) openSavingsBttn.getScene().getWindow();
      s.close();
    } catch (NumberFormatException n) {
      //didnt enter a good value
      Alert alert = new Alert(AlertType.ERROR, "Bad value entered. Please Try again");
      alert.setTitle("Bad value");
      alert.showAndWait();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
