package enums;

public enum TextsData {

    TEXT_WITH_REPEATED_WORD("London is capital", "London London is capital"),
    TEXT_WITH_URL("yandex@uandex.ru", "www.yandex.ru"),
    TEXT_WITH_DIGITS("петербург 1703", "петербург1703"),
    TEXT_WITH_ERRORS("cucumber", "cukumber"),
    TEXT_UKR("огірок", "огирок"),
    TEXT_WITH_CAPITAL("Чингисхан", "чингисхан");

    public static String[] TEXT_ARRAY = new String[3000];
    public static String[] TEXT_ARRAY_WITH_MISTAKES = {"магила", "исскусство", "ленейка"};
    public static String[] TEXT_ARRAY_CORRECT = {"могила", "искусство", "линейка"};

    private String textsIncorrect;
    private String textsCorrect;

    public String textsCorrect() {
        return textsCorrect;
    }

    public String textsIncorrect() {
        return textsIncorrect;
    }

    TextsData(String textsCorrect, String textsIncorrect) {
        this.textsCorrect = textsCorrect;
        this.textsIncorrect = textsIncorrect;
    }
}