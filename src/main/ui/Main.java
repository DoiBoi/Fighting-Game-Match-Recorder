package main.ui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new RecorderApp();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
