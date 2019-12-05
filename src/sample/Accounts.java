package sample;

import java.util.Scanner;

public class Accounts {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Please enter a starting number: ");

    int start = scanner.nextInt();

    System.out.println("Please enter a end number: ");

    int end = scanner.nextInt();

    int countEven = 0;
    int countOdd = 0;
    for(int i = start; i <= end; i++){
      if(i % 2 == 0){
        countEven++;
      }
      else{
        countOdd++;
      }
    }
    System.out.println("There are " + countEven + " even numbers and " + countOdd + " "
        + "odd numbers between " + start + " and " + end);

    String s = " 2";
    s += "jidnwfi";
    String mystring = new String("hello");
    mystring = "";
    System.out.println(mystring);

  }

}
