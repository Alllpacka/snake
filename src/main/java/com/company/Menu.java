package com.company;

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
    }

    public void getUserInput(){
        var scan = new java.util.Scanner(System.in);
    }

    @Override
    public void run() {

    }
}
