package com.amadana.utils;


import java.util.Random;
import java.util.Scanner;

public class Exam {

    public static void  main(String[] args) {
        playGame();
    }


    public static void playGame() {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();
        int m = r.nextInt(100);
        int n = r.nextInt(100);
        System.out.println(m + " " + n);
        int count = 0;
        for (int i=1;i<=n;i++) {
            int x = sc.nextInt();
            if (x == m) {
                if (count == 0) {
                    System.out.println("Bingo");
                    System.exit(0);
                }else if (count != 0 && count <= 2) {
                    System.out.println("Lucky You!");
                    System.exit(0);
                }else if (count > 2 && count<=n) {
                    System.out.println("Good Guess!");
                    System.exit(0);
                }
            }else if (x > m) {
                System.out.println("Too big");
                count++;
            }else if (x < m) {
                System.out.println("Too small");
                count++;
            }
            if (x < 0) {
                System.exit(0);
            }
        }
        System.out.println("Game over!");
    }
}
