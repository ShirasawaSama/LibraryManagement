package cn.apisium.library;

/**
 * Book
 */
public final class Book extends Item {
    public Book(Item other) {
        super(other);
    }

    @Override
    public int getRenewTime() {
        return 2; // two weeks
    }

    @Override
    public int getBorrowTime() {
        return 4; // four weeks
    }
}
