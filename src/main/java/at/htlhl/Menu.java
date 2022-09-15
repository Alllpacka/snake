package at.htlhl;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Menu implements Runnable {

    private final String logo = "  ____              _        _ \n" +
            " / ___| _ __   __ _| | _____| |\n" +
            " \\___ \\| '_ \\ / _` | |/ / _ \\ |\n" +
            "  ___) | | | | (_| |   <  __/_|\n" +
            " |____/|_| |_|\\__,_|_|\\_\\___(_)\n" +
            "                               ";

    public Menu(){
        printMenu();
        System.out.println();
    }

    public void printMenu(){
        for (char c : logo.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
        var scan = new java.util.Scanner(System.in);
        char input = ' ';
        do {
            System.out.println("Hast du bereits ein Konto? (y/n)");
            input = scan.nextLine().charAt(0);
            if (input == 'n') {
                newUser();
            } else if (input == 'y') {
                login();
            }
        } while (!(input == 'n' || input == 'y'));
    }

    private void newUser(){
        var scan = new java.util.Scanner(System.in);
        String username;
        String password = "";
        boolean valid;
        do {
            System.out.println("Gib einen neuen Benutzernamen ein: ");
            username = scan.nextLine();
            valid = true;
            if (username.length() > 15) {
                System.out.println("Benutzername zu lang. ");
                valid = false;
            }
            if (username.length() < 1) {
                System.out.println("Benutzername zu kurz. ");
                valid = false;
            }
        } while (!valid);
        do {
            System.out.println("Gib ein neues Passwort ein: ");
            try {

                if (java.lang.System.console() != null) {
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(StandardCharsets.UTF_8.encode(CharBuffer.wrap(System.console().readPassword())).array()));
                } else {
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(scan.nextLine().getBytes()));
                }
                System.out.println(password);
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Bestätige dein Passwort: ");
            String repeat;
            try {
                if (java.lang.System.console() != null) {
                    repeat = bytesToHex(MessageDigest.getInstance("SHA-256").digest(StandardCharsets.UTF_8.encode(CharBuffer.wrap(System.console().readPassword())).array()));
                } else {
                    repeat = bytesToHex(MessageDigest.getInstance("SHA-256").digest(scan.nextLine().getBytes()));
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            valid = true;
            if (password.equals("")) {
                System.out.println("Passwort zu kurz. ");
                valid = false;
            }
            if (password.equals("") || !password.equals(repeat)) {
                System.out.println("Passwörter stimmen nicht überein. ");
                valid = false;
            }
        } while (!valid);
        login();
    }

    private void login(){
        System.out.format("+---------------------+%n");
        System.out.format("| Login               |%n");
        System.out.format("+---------------------+%n");
        System.out.println("Benutzername: ");
        System.out.println("Passwort: ");
        System.out.println("Login speichern? (y/n)");
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void getUserInput(){
        var scan = new java.util.Scanner(System.in);
    }

    @Override
    public void run() {

    }
}
