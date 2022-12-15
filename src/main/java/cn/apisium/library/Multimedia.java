package cn.apisium.library;

/**
 * Multimedia
 */
public final class Multimedia extends Item {
    public Multimedia(Item other) {
        super(other);
    }

    @Override
    public int getRenewTime() {
        return 1; // one week
    }

    @Override
    public int getBorrowTime() {
        return 1; // one week
    }
}
