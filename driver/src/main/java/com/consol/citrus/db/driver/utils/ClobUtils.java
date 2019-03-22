package com.consol.citrus.db.driver.utils;

public class ClobUtils {

    public boolean fitsInInt(final long value) {
        return (int)value == value;
    }
}
