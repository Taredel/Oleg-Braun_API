import bean.YandexSpellerAnswer;
import core.YandexSpellerAPI;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utils.Utils.*;

import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerAPI.*;
import static core.YandexSpellerConstants.PARAM_FORMAT;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static enums.ErrorCodes.*;
import static enums.Language.*;
import static enums.Options.*;
import static enums.TextsData.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.GET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Utils.fillTextArray;


public class TestYandexSpeller {

    @Test(description = "Simple request with mistake")
    public void checkRequestWithMistakes() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerAPI.getYandexSpellerAnswers(
                with()
                        .language(EN)
                        .text(TEXT_WITH_ERRORS.textsIncorrect())
                        .callApi());
        assertThat(answers.get(0).get(0).getS(), hasItem(TEXT_WITH_ERRORS.textsCorrect()));
    }

    // this test falls because of incorrect work of Yandex Speller
    @Test(description = "Check repeated words error code")
    public void checkRepeatErrorCode() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerAPI.getYandexSpellerAnswers(
                YandexSpellerAPI.with()
                        .text(TEXT_WITH_REPEATED_WORD.textsIncorrect(), TEXT_WITH_REPEATED_WORD.textsCorrect())
                        .callApi());
        assertThat("Expected error \"Repeat words.\"", "\"code\":" + answers.get(0).get(0).code,
                equalTo(ERROR_REPEAT_WORD.toString()));
    }

    @Test(description = "Simple test using array of text")
    public void simpleCheckArrayTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerAPI.getYandexSpellerAnswers(
                YandexSpellerAPI.with()
                        .text(TEXT_ARRAY_WITH_MISTAKES)
                        .callApi());
        assertThat(answers.get(0).get(0).s, hasItem(TEXT_ARRAY_CORRECT[0]));
        assertThat(answers.get(1).get(0).s, hasItem(TEXT_ARRAY_CORRECT[1]));
        assertThat(answers.get(2).get(0).s, hasItem(TEXT_ARRAY_CORRECT[2]));
    }

    @Test(description = "Request with wrong language")
    public void wrongLanguageTest() {
        YandexSpellerAPI.with()
                .text(TEXT_UKR.textsIncorrect())
                .language(RU)
                .callApi()
                .then()
                .specification(successResponse())
                .assertThat()
                .body(Matchers.equalTo("[[]]"));
    }

    @Test(description = "Request with ignore digits option")
    public void checkIgnoreDigitsOption() {
        YandexSpellerAPI.with()
                .options(IGNORE_DIGITS.option)
                .text(TEXT_WITH_DIGITS.textsIncorrect())
                .callApi()
                .then()
                .specification(successResponse())
                .assertThat()
                .body(Matchers.equalTo("[[]]"));
    }

    @Test(description = "Request with incorrect language option")
    public void incorrectLanguageTest() {
        YandexSpellerAPI.with()
                .language(INCORRECT_LANGUAGE)
                .text(TEXT_WITH_ERRORS.textsIncorrect())
                .callApi()
                .then()
                .specification(badResponse());
    }

    @Test(description = "GET request with several options")
    public void severalOptionsTest() {
        with()
                .text(TEXT_WITH_CAPITAL.textsIncorrect(), TEXT_WITH_URL.textsIncorrect())
                .options(IGNORE_CAPITALIZATION.option + IGNORE_URLS.option)
                .httpMethod(GET)
                .callApi()
                .then().specification(successResponse())
                .assertThat()
                .body(Matchers.equalTo("[[],[]]"));
    }

    @Test(description = "GET request with incorrect format parameter")
    public void incorrectFormatTest() {
        with().text(TEXT_WITH_ERRORS.textsIncorrect())
                .format(PARAM_FORMAT)
                .httpMethod(GET)
                .callApi()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'format'"));
    }

    @Test(description = "Check if service GET responce succeed")
    public void checkResponceSuccessTest() {
        RestAssured
                .given(YandexSpellerAPI.baseRequestConfiguration())
                .params(PARAM_TEXT, Arrays.asList(TEXT_WITH_ERRORS.textsIncorrect(), TEXT_WITH_DIGITS.textsIncorrect()))
                .log().all()
                .get().prettyPeek()
                .then().specification(successResponse());
    }

    @Test(description = "Check if service POST responce fails due to long text with 414 error")
    public void checkPOSTResponceFails() {
        RestAssured
                .given(YandexSpellerAPI.baseRequestConfiguration())
                .params(PARAM_TEXT, fillTextArray(TEXT_ARRAY))
                .log().all()
                .post().prettyPeek()
                .then().specification(failedResponse());
    }
}