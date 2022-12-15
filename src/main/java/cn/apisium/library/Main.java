package cn.apisium.library;

import java.util.Scanner;

/**
 * @author Shirasawa
 * @version 0.0.0
 * @see <a href="https://github.com/ShirasawaSama/LibraryManagement.git">https://github.com/ShirasawaSama/LibraryManagement.git</a>
 *
 * Main entry point
 */
public final class Main {
    /**
     * Main entry point
     */
    public static void main(String[] args) {
        Management management = null;
        try {
            var in = new Scanner(System.in);
            management = new Management();
            while (true) {
                System.out.println("Please input an action: (0-CreateLoan, 1-RenewLoan, 2-ReturnItem, 3-CreateItem, " +
                        "4-DeleteItem, 5-CreateUser, 6-DeleteUser, 7-ShowLoans, 8-ShowItems, 9-ShowUsers, 10-Exit)");
                try {
                    var action = in.nextInt();
                    if (action < 7 && action >= 0) in.nextLine();
                    switch (action) {
                        case 0, 1, 2 -> {
                            System.out.println("Please input barcode:");
                            var barcode = in.nextLine();
                            System.out.println("Please input user id:");
                            var userId = in.nextLine();
                            switch (action) {
                                case 0 -> management.createLoan(barcode, userId);
                                case 1 -> management.renewLoan(barcode, userId);
                                case 2 -> management.returnItem(barcode, userId);
                            }
                            System.out.println("Success!");
                        }
                        case 3 -> {
                            var item = new Item(in);
                            management.addItem(item.isBook() ? new Book(item) : new Multimedia(item));
                        }
                        case 4 -> {
                            System.out.println("Barcode to delete: ");
                            management.removeItem(in.nextLine());
                        }
                        case 5 -> management.addUser(new User(in));
                        case 6 -> {
                            System.out.println("User id to delete: ");
                            management.removeUser(in.nextLine());
                        }
                        case 7 -> management.viewLoans();
                        case 8 -> management.viewItems();
                        case 9 -> management.viewUsers();
                        case 10 -> {
                            System.out.println("Goodbye and data saved!");
                            return;
                        }
                        default -> System.out.println("Unknown action!");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (management != null) management.save();
        }
    }
}