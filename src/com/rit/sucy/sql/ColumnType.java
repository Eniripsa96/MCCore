package com.rit.sucy.sql;

/**
 * Types of columns supported
 */
public enum ColumnType {

    /**
     * <p>A string that can be up to 16 characters long</p>
     */
    STRING_16 ("VARCHAR(16)"),

    /**
     * A string that can be up to 32 characters long
     */
    STRING_32 ("VARCHAR(32)"),

    /**
     * <p>A string that can be up to 64 characters long</p>
     */
    STRING_64 ("VARCHAR(64)"),

    /**
     * <p>A string that can be up to 128 characters long</p>
     */
    STRING_128 ("VARCHAR(128)"),

    /**
     * <p>A string that can be up to 256 characters long</p>
     */
    STRING_255 ("VARCHAR(255)"),

    /**
     * <p>A string that can be up to 1000 characters long</p>
     */
    STRING_1000 ("VARCHAR(1000)"),

    /**
     * <p>A string that can be up to 4000 characters long</p>
     */
    STRING_4000 ("VARCHAR(4000)"),

    /**
     * <p>A string that can be up to 8000 characters long</p>
     */
    STRING_8000 ("VARCHAR(8000)"),

    /**
     * A standard integer
     */
    INT ("INT"),

    /**
     * A standard long
     */
    LONG ("BIGINT"),

    /**
     * <p>A standard 4-byte float value</p>
     */
    FLOAT ("FLOAT(24)"),

    /**
     * <p>A standard 8-byte double value</p>
     */
    DOUBLE ("FLOAT(53)"),

    /**
     * <p>A integer increment to put a number ID on each entry</p>
     */
    INCREMENT("INT KEY AUTO_INCREMENT"),

    /**
     * <p>A date/time value in the format YYYY-MM-DD HH:MM:SS</p>
     */
    DATE_TIME("DATETIME"),

    ;

    private final String key;

    /**
     * Enum constructor
     *
     * @param key type key
     */
    private ColumnType(String key) {
        this.key = key;
    }

    /**
     * @return type key
     */
    @Override
    public String toString() {
        return this.key;
    }

    /**
     * <p>Retrieves a column type by name and size values.</p>
     * <p>If no supported type matches the data, this will return
     * null instead.</p>
     *
     * @param type type of the column
     * @param size size of the column
     * @return     column type representation
     */
    public static ColumnType getByValues(String type, int size) {
        if (type.equalsIgnoreCase("VARCHAR")) {
            switch (size) {
                case 16: return STRING_16;
                case 32: return STRING_32;
                case 64: return STRING_64;
                case 128: return STRING_128;
                case 255: return STRING_255;
                case 1000: return STRING_1000;
                case 4000: return STRING_4000;
                case 8000: return STRING_8000;
                default:
                    if (size > 4000) return STRING_8000;
                    if (size > 1000) return STRING_4000;
                    if (size > 255) return STRING_1000;
                    if (size > 128) return STRING_255;
                    if (size > 64) return STRING_128;
                    if (size > 32) return STRING_64;
                    if (size > 16) return STRING_32;
                    else return STRING_16;
            }
        }
        else if (type.equalsIgnoreCase("INT")) {
            return INT;
        }
        else if (type.equals("FLOAT")) {
            return FLOAT;
        }
        else if (type.equals("DOUBLE")) {
            return DOUBLE;
        }
        else if (type.equals("DATETIME")) {
            return DATE_TIME;
        }
        else if (type.equals("BIGINT")) {
            return LONG;
        }

        return null;
    }
}
