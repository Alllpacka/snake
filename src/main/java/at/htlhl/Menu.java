package at.htlhl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Menu implements Runnable {

    private final String logo = "  ____              _        _ \n" +
            " / ___| _ __   __ _| | _____| |\n" +
            " \\___ \\| '_ \\ / _` | |/ / _ \\ |\n" +
            "  ___) | | | | (_| |   <  __/_|\n" +
            " |____/|_| |_|\\__,_|_|\\_\\___(_)\n" +
            "                               ";

    private String username;
    private String password = "";

    public Menu() {
        printMenu(false);
        System.out.println();
    }

    public void printMenu(boolean ignoreConfig) {
        if (!ignoreConfig) {
            for (char c : logo.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println();
        if (!checkConfig() || ignoreConfig) {
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
        } else {
            File file = new File(getClass().getResource("/login.conf").getFile());
            java.util.Scanner scan = null;
            try {
                scan = new java.util.Scanner(file);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
            ArrayList<String> lines = new ArrayList<>();
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            scan.close();

            if (!(lines.get(0).equals(sqlPull("SELECT username FROM users WHERE username='" + lines.get(0) + "'")))) {
                // Benutzer existiert nicht
                printMenu(true);
            } else if (lines.size() < 2) {
                printMenu(true);
            } else if (!(lines.get(1).equals(sqlPull("SELECT password FROM users WHERE username='" + lines.get(0) + "' AND " + "password='" + lines.get(1) + "'")))) {
                // Passwort ungültig
                printMenu(true);
            }
            System.out.println("Viel Spaß! ");
        }
    }

    private boolean checkConfig() {
        File file = new File(getClass().getResource("/login.conf").getFile());
        return file.exists();
    }

    private void newUser() {
        var scan = new java.util.Scanner(System.in);
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
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(StandardCharsets.US_ASCII.encode(CharBuffer.wrap(System.console().readPassword())).array()));
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
                    repeat = bytesToHex(MessageDigest.getInstance("SHA-256").digest(StandardCharsets.US_ASCII.encode(CharBuffer.wrap(System.console().readPassword())).array()));
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
        sqlPush("INSERT INTO users (username, password, score) VALUES ('" + username + "', '" + password + "', 0)");
        login();
    }

    private void login() {
        var scan = new java.util.Scanner(System.in);

        System.out.format("+---------------------+%n");
        System.out.format("| Login               |%n");
        System.out.format("+---------------------+%n");

        
        boolean valid;
        do {
            System.out.println("Benutzername: ");
            username = scan.nextLine();
            valid = true;
            if (!(username.equals(sqlPull("SELECT username FROM users WHERE username='" + username + "'")))) {
                System.out.println("Benutzer existiert nicht. ");
                valid = false;
            }
        } while (!valid);

        valid = false;
        do {
            System.out.println("Passwort: ");
            try {
                if (java.lang.System.console() != null) {
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(StandardCharsets.US_ASCII.encode(CharBuffer.wrap(System.console().readPassword())).array()));
                } else {
                    password = bytesToHex(MessageDigest.getInstance("SHA-256").digest(scan.nextLine().getBytes()));
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
            valid = true;
            if (!(password.equals(sqlPull("SELECT password FROM users WHERE username='" + username + "' AND " + "password='" + password + "'")))) {
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
            if (remember == 'y') {
                writeConfigFile();
            }
        } while (!valid);
    }

    private void writeConfigFile(){
        Path resources = null;
        try {
            resources = Paths.get(getClass().getResource("/").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Path file = Paths.get(resources.toAbsolutePath() + "/login.conf");
        try {
            var writer = Files.newBufferedWriter(file);
            writer.write(username + "\n" + password);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String sqlPull(String sql) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://branmark.ddns.net:3306/snake", "snake", "python");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void sqlPush(String sql) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://branmark.ddns.net:3306/snake", "snake", "python");
            Statement stmt = con.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {

    }
}
