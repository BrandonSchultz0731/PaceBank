package sample;

import java.util.Scanner;

public class CountEven {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter a number: ");
    int num = scanner.nextInt();

    int countEven = 0;
    int countOdd = 0;

    int n = num;
    while(n >= 1){
      int temp = n % 10;
      if(temp % 2 == 0){
        countEven++;
      }
      else{
        countOdd++;
      }
      n = n / 10;
    }

    System.out.println("In the number " + num + " there are " + countEven + " even "
        + "and " + countOdd + " odd");
  }


}
