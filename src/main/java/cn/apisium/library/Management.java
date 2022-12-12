package cn.apisium.library;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 图书馆管理系统
 */
public final class Management {
    private static final Long WEEK = 1000L * 60 * 60 * 24 * 7;
    private static final String USERS_FILE = "USERS.csv", ITEMS_FILE = "ITEMS.csv", LOANS_FILES = "LOANS.csv";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final CsvMapper mapper = new CsvMapper();
    private static final CsvSchema userSchema = mapper.schemaFor(User.class).withHeader();
    private static final CsvSchema itemSchema = mapper.schemaFor(Item.class).withHeader();
    private static final CsvSchema loanSchema = mapper.schemaFor(Loan.class).withHeader();
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();
    private final ArrayList<Loan> loans = new ArrayList<>();

    /**
     * 从文件中读取数据
     */
    public Management() throws IOException {
        var file = new File(USERS_FILE);
        if (file.exists()) try (MappingIterator<User> reader = mapper.readerFor(User.class).with(userSchema).readValues(file)) {
            reader.readAll().forEach(it -> users.put(it.getUserId(), it));
        }
        file = new File(ITEMS_FILE);
        if (file.exists()) try (MappingIterator<Item> reader = mapper.readerFor(Item.class).with(itemSchema).readValues(file)) {
            reader.readAll().forEach(it -> items.put(it.getBarcode(), it.isBook() ? new Book(it) : new Multimedia(it)));
        }
        file = new File(LOANS_FILES);
        if (file.exists()) try (MappingIterator<Loan> reader = mapper.readerFor(Loan.class).with(loanSchema).readValues(file)) {
            loans.addAll(reader.readAll());
        }
    }

    /**
     * 从现成的数据创建
     *
     * @param users 用户列表
     * @param items 图书列表
     * @param loans 借书列表
     */
    public Management(List<User> users, List<Item> items, List<Loan> loans) {
        users.forEach(it -> this.users.put(it.getUserId(), it));
        items.forEach(it -> this.items.put(it.getBarcode(), it));
        this.loans.addAll(loans);
    }

    /**
     * 创建借书条目
     * @param barcode 条形码
     * @param userId 用户 ID
     * @throws IllegalArgumentException 如果用户不存在或图书不存在
     */
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

    /**
     * 延期借书时间
     * @param barcode 条形码
     * @param userId 用户 ID
     * @throws IllegalArgumentException 如果用户不存在或图书不存在
     */
    public void renewLoan(String barcode, String userId) throws IllegalArgumentException {
        final Loan loan = getLoan(barcode, userId);
        if (loan.getNumRenews() >= 3) throw new IllegalArgumentException("Cannot renew more than 3 times!");
        final Date now = new Date();
        loan.setDueDate(dateFormat.format(new Date(now.getTime() + WEEK * (items.get(barcode).isBook() ? 2 : 1))));
        loan.setNumRenews(loan.getNumRenews() + 1);
    }

    /**
     * 归还图书
     * @param barcode 条形码
     * @param userId 用户 ID
     * @throws IllegalArgumentException 如果用户不存在或图书不存在
     */
    public void returnItem(String barcode, String userId) throws IllegalArgumentException {
        loans.remove(getLoan(barcode, userId));
    }

    /**
     * 保存数据
     */
    public void save() {
        try {
            try (var writer = mapper.writerFor(Loan.class).with(loanSchema).writeValues(new File(USERS_FILE))) {
                writer.writeAll(loans);
            }
            try (var writer = mapper.writerFor(User.class).with(userSchema).writeValues(new File(USERS_FILE))) {
                writer.writeAll(users.values());
            }
            try (var writer = mapper.writerFor(Item.class).with(itemSchema).writeValues(new File(ITEMS_FILE))) {
                writer.writeAll(items.values());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户
     * @param userId 用户 ID
     * @return 用户
     */
    public User getUser(String userId) {
        return users.get(userId);
    }

    /**
     * 获取图书
     * @param barcode 条形码
     * @return 图书
     */
    public Item getItem(String barcode) {
        return items.get(barcode);
    }

    /**
     * 获取全部借书记录
     * @return 借书记录
     */
    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    /**
     * 添加用户
     * @param user 用户
     */
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    /**
     * 移除用户
     * @param userId 用户 ID
     */
    public void removeUser(String userId) {
        if (users.remove(userId) == null) throw new IllegalArgumentException("No such user!");
    }

    /**
     * 添加图书
     * @param item 图书
     */
    public void addItem(Item item) {
        items.put(item.getBarcode(), item);
    }

    /**
     * 移除图书
     * @param barcode 条形码
     * @throws IllegalArgumentException 如果图书不存在
     */
    public void removeItem(String barcode) throws IllegalArgumentException {
        if (items.remove(barcode) == null) throw new IllegalArgumentException("No such item!");
    }

    /**
     * 显示全部的图书条目
     */
    public void viewItems() {
        System.out.println("Items:");
        items.values().forEach(System.out::println);
    }

    /**
     * 显示全部的借书条目
     */
    public void viewLoans() {
        System.out.println("Loans:");
        loans.forEach(System.out::println);
    }

    /**
     * 显示全部的用户
     */
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
