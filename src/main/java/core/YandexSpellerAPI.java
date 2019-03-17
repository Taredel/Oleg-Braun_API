package core;

import bean.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.Language;
import enums.Options;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static core.YandexSpellerConstants.*;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerAPI {

    private HashMap<String, String> params = new HashMap<>();
    private List<String> texts = new ArrayList<>();
    private Method method = Method.GET;

    private YandexSpellerAPI() {
    }

    public static ApiBuilder with() {
        YandexSpellerAPI api = new YandexSpellerAPI();
        return new ApiBuilder(api);
    }

    public static class ApiBuilder {
        YandexSpellerAPI spellerApi;

        private ApiBuilder(YandexSpellerAPI gcApi) {
            spellerApi = gcApi;
        }

        public ApiBuilder text(String... texts) {
            spellerApi.texts.addAll(Arrays.asList(texts));
            return this;
        }

        public ApiBuilder options(int options) {
            spellerApi.params.put(PARAM_OPTIONS, String.valueOf(options));
            return this;
        }

        public ApiBuilder format(String format) {
            spellerApi.params.put(PARAM_FORMAT, format);
            return this;
        }

        public ApiBuilder language(Language language) {
            spellerApi.params.put(PARAM_LANG, language.langCode());
            return this;
        }

        public ApiBuilder httpMethod(Method method) {
            spellerApi.method = method;
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParam(PARAM_TEXT, spellerApi.texts)
                    .queryParams(spellerApi.params)
                    .log().all()
                    .request(spellerApi.method, YANDEX_SPELLER_API_URI_TEXTS)
                    .prettyPeek();
        }
    }

    public static ResponseSpecification successResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .build();
    }

    public static ResponseSpecification failedResponse() {
        return new ResponseSpecBuilder()
                .expectHeader("Connection", "close")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_REQUEST_URI_TOO_LONG)
                .build();
    }

    public static ResponseSpecification badResponse() {
        return new ResponseSpecBuilder()
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }

    public static RequestSpecification baseRequestConfiguration() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .setRelaxedHTTPSValidation()
                .setBaseUri(YANDEX_SPELLER_API_URI_TEXTS)
                .build();
    }

    public static List<List<YandexSpellerAnswer>> getYandexSpellerAnswers(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List<List<YandexSpellerAnswer>>>() {
        }.getType());
    }
}
