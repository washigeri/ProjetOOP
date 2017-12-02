package utils;

public class IntStringPair {
    private final Integer key;
    private final String value;

    public IntStringPair(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    @Override
    public String toString() {
        return value;
    }
}