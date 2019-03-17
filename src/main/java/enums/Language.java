package enums;

public enum Language {

    RU("ru"),
    UKR("ukr"),
    EN("en"),
    INCORRECT_LANGUAGE("sum");

    private String languageCode;
    public String langCode(){return languageCode;}

    private Language(String lang) {
            this.languageCode = lang;
        }
}
