package sample;

public class ModelProductTable {

  int productID;
  int accountID;
  int Balance;

  public ModelProductTable(int productID, int accountID, int balance) {
    this.productID = productID;
    this.accountID = accountID;
    Balance = balance;
  }

  public int getProductID() {
    return productID;
  }

  public void setProductID(int productID) {
    this.productID = productID;
  }

  public int getAccountID() {
    return accountID;
  }

  public void setAccountID(int accountID) {
    this.accountID = accountID;
  }

  public int getBalance() {
    return Balance;
  }

  public void setBalance(int balance) {
    Balance = balance;
  }
}
