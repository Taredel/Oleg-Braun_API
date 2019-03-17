package enums;

public enum ErrorCodes {

    ERROR_UNKNOWN_WORD("\"code\":1"),
    ERROR_REPEAT_WORD("\"code\":2"),
    ERROR_CAPITALIZATION("\"code\":3"),
    ERROR_TOO_MANY_ERRORS("\"code\":4");

    public String value;

    ErrorCodes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
