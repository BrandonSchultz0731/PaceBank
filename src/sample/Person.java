package sample;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class Person {
  private String username;
  private String firstName;
  private String lastName;
  private Date DOB;
  private String socialSecNum;
  private String emailAddress;
  private String mainAddress;
  private int zipCode;
  private String city;
  private String state;
  private String licenseNum;
  private int userBalance;
  private int accountID;
  private byte[] image;




  public Person(String username, String firstName, String lastName, Date DOB,
      String socialSecNum, String emailAddress, String mainAddress, int zipCode,
      String city, String state, String licenseNum, int userBalance, int accountID, byte[] image) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.DOB = DOB;
    this.socialSecNum = socialSecNum;
    this.emailAddress = emailAddress;
    this.mainAddress = mainAddress;
    this.zipCode = zipCode;
    this.city = city;
    this.state = state;
    this.licenseNum = licenseNum;
    this.userBalance = userBalance;
    this.accountID = accountID;
    this.image = image;
  }
  public int calcAge(){
    LocalDate bday = new java.sql.Date(DOB.getTime()).toLocalDate();
    LocalDate now = LocalDate.now();
    Period age = Period.between(bday,now);
    return age.getYears();

  }
  public byte[] getImage() {
    return image;
  }
  public int getUserBalance() {
    return userBalance;
  }

  public int getAccountID() {
    return accountID;
  }

  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Date getDOB() {
    return DOB;
  }

  public String getSocialSecNum() {
    return socialSecNum;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getMainAddress() {
    return mainAddress;
  }

  public int getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getLicenseNum() {
    return licenseNum;
  }
}
