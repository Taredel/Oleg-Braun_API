package enums;

public enum Options {

    IGNORE_DIGITS(2),
    IGNORE_URLS(4),
    FIND_REPEAT_WORDS(8),
    IGNORE_CAPITALIZATION(512);

    public int option;

    Options(int option) {
        this.option = option;
    }
}
