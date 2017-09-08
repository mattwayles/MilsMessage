package main.Mils;

import java.util.Random;

public class MilsMessageGenerator {
  public static void main(String args[]) {
    generateMils();
  }

  private static void generateMils() {
    Random r;
    StringBuilder result;

    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@#$%^&*()_-+={[}]|':;?/>.<,\\ ";
    //String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    int count = 0;
    while (count <= 50) {
      result = new StringBuilder();
      r = new Random();
      for (int i = 0; i < 80; i++) {
        //result += alphaNumeric.charAt(r.nextInt(alphaNumeric.length()));
        result.append(alphabet.charAt(r.nextInt(alphabet.length())));
      }
      System.out.println(result);
      count++;
    }
  }
}
