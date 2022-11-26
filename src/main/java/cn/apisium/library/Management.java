package cn.apisium.library;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Management {
    private static final Long WEEK = 1000L * 60 * 60 * 24 * 7;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final CsvMapper mapper = new CsvMapper();
    private static final CsvSchema userSchema = mapper.schemaFor(User.class).withHeader();
    private static final CsvSchema itemSchema = mapper.schemaFor(Item.class).withHeader();
    private static final CsvSchema loanSchema = mapper.schemaFor(Loan.class).withHeader();
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();
    private final List<Loan> loans;

    public Management() throws IOException {
        try (MappingIterator<User> reader = mapper.readerFor(User.class).with(userSchema).readValues(new File("USERS.csv"))) {
            reader.readAll();
        }
        try (MappingIterator<Item> reader = mapper.readerFor(Item.class).with(itemSchema).readValues(new File("ITEMS.csv"))) {
            reader.readAll().forEach(it -> items.put(it.getBarcode(), it));
        }
        try (MappingIterator<Loan> reader = mapper.readerFor(Loan.class).with(loanSchema).readValues(new File("LOANS.csv"))) {
            loans = new ArrayList<>(reader.readAll());
        }
    }

    public Management(List<User> users, List<Item> items, List<Loan> loans) {
        users.forEach(it -> this.users.put(it.getUserId(), it));
        items.forEach(it -> this.items.put(it.getBarcode(), it));
        this.loans = new ArrayList<>(loans);
    }

    public void createLoan(String barcode, String userId) throws IllegalArgumentException {
        checkBarcodeAndUserId(barcode, userId);
        final Item item = items.get(barcode);
        final Loan loan = new Loan();
        loan.setBarcode(barcode);
        loan.setUserId(userId);
        final Date now = new Date();
        loan.setIssueDate(dateFormat.format(now));
        loan.setDueDate(dateFormat.format(new Date(now.getTime() + WEEK * (item.isBook() ? 4 : 1))));
        loans.add(loan);
    }

    public void renewLoan(String barcode, String userId) throws IllegalArgumentException {
        final Loan loan = getLoan(barcode, userId);
        if (loan.getNumRenews() >= 3) throw new IllegalArgumentException("Cannot renew more than 3 times!");
        final Date now = new Date();
        loan.setDueDate(dateFormat.format(new Date(now.getTime() + WEEK * (items.get(barcode).isBook() ? 2 : 1))));
        loan.setNumRenews(loan.getNumRenews() + 1);
    }

    public void returnItem(String barcode, String userId) throws IllegalArgumentException {
        loans.remove(getLoan(barcode, userId));
    }

    public void save() {
        try {
            try (var writer = mapper.writerFor(Loan.class).with(loanSchema).writeValues(new File("LOANS.csv"))) {
                writer.writeAll(loans);
            }
            try (var writer = mapper.writerFor(User.class).with(userSchema).writeValues(new File("USERS.csv"))) {
                writer.writeAll(users.values());
            }
            try (var writer = mapper.writerFor(Item.class).with(itemSchema).writeValues(new File("ITEMS.csv"))) {
                writer.writeAll(items.values());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public Item getItem(String barcode) {
        return items.get(barcode);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void removeUser(String userId) {
        if (users.remove(userId) == null) throw new IllegalArgumentException("No such user!");
    }

    public void addItem(Item item) {
        items.put(item.getBarcode(), item);
    }

    public void removeItem(String barcode) throws IllegalArgumentException {
        if (items.remove(barcode) == null) throw new IllegalArgumentException("No such item!");
    }

    public void viewItems() {
        System.out.println("Items:");
        items.values().forEach(System.out::println);
    }

    public void viewLoans() {
        System.out.println("Loans:");
        loans.forEach(System.out::println);
    }

    public void viewUsers() {
        System.out.println("Users:");
        users.values().forEach(System.out::println);
    }

    private void checkBarcodeAndUserId(String barcode, String userId) throws IllegalArgumentException {
        if (!items.containsKey(barcode)) throw new IllegalArgumentException("No such item!");
        if (!users.containsKey(userId)) throw new IllegalArgumentException("No such user!");
    }

    private Loan getLoan(String barcode, String userId) {
        checkBarcodeAndUserId(barcode, userId);
        return loans.stream()
                .filter(it -> it.getBarcode().equals(barcode) && it.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No such loan!"));
    }
}
