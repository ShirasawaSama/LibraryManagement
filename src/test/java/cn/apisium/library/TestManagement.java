package cn.apisium.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestManagement {
    private static Management setupManagement() {
        var management = new Management(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        var user = new User();
        user.setUserId("B00447489");
        user.setFirstName("Gray");
        user.setLastName("Shingles");
        user.setEmail("gshingles0@ucoz.ru");
        management.addUser(user);

        Item item = new Item();
        item.setBarcode("25832497");
        item.setAuthor("Gabe Scain");
        item.setTitle("enim in");
        item.setType("Book");
        item.setYear("2004");
        item.setIsbn("621791531-6");
        management.addItem(item);

        return management;
    }

    @Test
    public void testAddAndRemoveUser() {
        var management = setupManagement();
        Assertions.assertEquals(management.getUser("B00447489").getFirstName(), "Gray");
        management.removeUser("B00447489");
        Assertions.assertNull(management.getUser("B00447489"));
    }

    @Test
    public void testAddAndRemoveItem() {
        var management = setupManagement();
        Assertions.assertEquals(management.getItem("25832497").getAuthor(), "Gabe Scain");
        management.removeItem("25832497");
        Assertions.assertNull(management.getItem("25832497"));
    }

    @Test
    public void testAddRenewAndReturnLoan() {
        var management = setupManagement();
        management.createLoan("25832497", "B00447489");
        var loans = management.getLoans();

        Assertions.assertEquals(loans.size(), 1);

        var loan = loans.get(0);
        management.renewLoan("25832497", "B00447489");
        Assertions.assertEquals(loan.getNumRenews(), 1);

        management.returnItem("25832497", "B00447489");

        Assertions.assertEquals(management.getLoans().size(), 0);
    }
}
