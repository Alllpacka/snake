package at.htlhl;

import java.io.Console;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
        var scan = new java.util.Scanner(System.in);

        System.out.format("+---------------------+%n");
        System.out.format("| Login               |%n");
        System.out.format("+---------------------+%n");

        /*String username;
        boolean valid;
        do {
            System.out.println("Benutzername: ");
            username = scan.nextLine();
            valid = true;
            if (!(username.equals(sqlPull("SELECT FROM users WHERE username = '" + username + "'", 2)))) {
                System.out.println("Benutzer existiert nicht. ");
                valid = false;
            }
        } while (!valid);

        String password = "";
        valid = false;
        do {
            System.out.println("Passwort: ");
            try {
                if (java.lang.System.console() != null) {
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(StandardCharsets.UTF_8.encode(CharBuffer.wrap(System.console().readPassword())).array()));
                } else {
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(scan.nextLine().getBytes()));
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
            valid = true;
            if (!(password.equals(sqlPull("SELECT FROM users WHERE username = '" + username + "' " + "passsword = '" + password + "'", 3)))) {
                System.out.println("Passwort ist ungültig. ");
                valid = false;
            }
        } while (!valid);

        char remember;
        do {
            System.out.println("Login speichern? (y/n)");
            remember = scan.nextLine().charAt(0);
            valid = true;
            if (remember != 'y' && remember != 'n') {
                System.out.println("Ungültige Eingabe. ");
                valid = false;
            }
        } while (!valid);*/
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

    private String sqlPull(String sql, int i){
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://branmark.ddns.net:3306/snake", "snake", "python");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            con.close();
            return rs.getString(i);
        } catch (Exception e) {
            return null;
        }
    }
    private void sqlPush(){

    }

    @Override
    public void run() {

    }
}
