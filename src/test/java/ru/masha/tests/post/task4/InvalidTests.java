package ru.masha.tests.post.task4;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.masha.models.AdminObject;
import ru.masha.models.ApiError;
import ru.masha.models.ErrorResponse;

import java.util.List;

import static ru.masha.UrlPaths.ADMIN_OBJECTS;
import static ru.masha.specifications.Specifications.*;

public class InvalidTests {

    /*  В задании написано, что в поле code допустимые символы:
    Только латинские буквы, кириллица, цифры, «.», «,», «"», «/», «\», «$», «*», «-», «_».
    Но в "аналитике" сказано, что разрешённые символы: латиница, цифры, нижнее подчёркивание "_" (без пробелов).
    Сделала по "аналитике"
    */

    public List<ApiError> getConflictErrorOrFail(AdminObject admin) {
        return RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .log()
                .all()
                .extract()
                .as(ErrorResponse.class)
                .getErrors();
    }

    //4.1
    @Test
    @DisplayName("В поле \"code\" содержится недопустимый символ")
    void checkCodeInvalidContent() {
        String invalidCodeValue = "as$";
        AdminObject admin = new AdminObject(invalidCodeValue, "titleTest4", "HEY", "KEK");

        List<ApiError> errors = getConflictErrorOrFail(admin);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("PPRM_BAD_REQUEST_ERROR", errors.get(0).getCode());
    }

    //4.2
    @Test
    @DisplayName("В поле \"code\" превышен лимит символов")
    void checkCodeInvalidLength() {
        String invalidCodeValue = "ab".repeat(51);
        AdminObject admin = new AdminObject(invalidCodeValue, "titleTest4", "HEY", "KEK");

        List<ApiError> errors = getConflictErrorOrFail(admin);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("PPRM_BAD_REQUEST_ERROR", errors.get(0).getCode());
    }

    //4.3
    @Test
    @DisplayName("В поле \"title\" превышен допустимый лимит символов")
    void checkTitleInvalidLength() {
        String invalidTitleValue = "a".repeat(301);
        AdminObject admin = new AdminObject("code43", invalidTitleValue, "HEY", "KEK");

        List<ApiError> errors = getConflictErrorOrFail(admin);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("PPRM_BAD_REQUEST_ERROR", errors.get(0).getCode());
    }
}
