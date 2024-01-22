package ru.masha.tests.post.task5;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.masha.models.AdminObject;
import ru.masha.models.AdminObjectDeleteRequest;
import ru.masha.models.ApiError;
import ru.masha.models.ErrorResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.masha.ParamsCode.CODE_CONFLICT;
import static ru.masha.ParamsCode.TITLE_CONFLICT;
import static ru.masha.UrlPaths.ADMIN_OBJECTS;
import static ru.masha.specifications.Specifications.jsonRequestSpecification;
import static ru.masha.specifications.Specifications.jsonRequestSpecificationEmpty;

public class ConflictTests {

    public AdminObject createAdmin(AdminObject admin) {
        return RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .body()
                .as(AdminObject.class);
    }

    public void deleteAdmin(AdminObject admin) {
        RestAssured.given()
                .spec(jsonRequestSpecificationEmpty())
                .body(new AdminObjectDeleteRequest(admin.getId(), admin.getSystemCode()))
                .delete(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    public List<ApiError> getConflictErrorOrFail(AdminObject admin) {
        return RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .log()
                .all()
                .extract()
                .as(ErrorResponse.class)
                .getErrors();
    }

    //5.1
    @Test
    @DisplayName("Объект не создан, возник \"codeConflict\"")
    void checkCodeConflict() {
        AdminObject admin = new AdminObject("code5", "title5", "HOP", "LOL");

        AdminObject adminObject = createAdmin(admin);
        admin.setTitle("titlehophey");

        List<ApiError> errors = getConflictErrorOrFail(admin);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(CODE_CONFLICT.getParamCode(), errors.get(0).getParamCode());

        deleteAdmin(adminObject);
    }

    //5.2
    @Test
    @DisplayName("Объект не создан, возник \"titleConflict\"")
    void checkTitleConflict() {
        AdminObject admin = new AdminObject("code5", "title5", "HOP", "LOL");

        AdminObject adminObject = createAdmin(admin);
        admin.setCode("titlehophey");

        List<ApiError> errors = getConflictErrorOrFail(admin);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(TITLE_CONFLICT.getParamCode(), errors.get(0).getParamCode());

        deleteAdmin(adminObject);
    }

    //5.3
    @Test
    @DisplayName("Объект не создан, возник \"codeConflict\" + \"titleConflict\"")
    void checkTitleAndCodeConflict() {
        AdminObject admin = new AdminObject("code5", "title5", "HOP", "LOL");

        AdminObject adminObject = createAdmin(admin);

        List<ApiError> errors = getConflictErrorOrFail(admin);

        Set<String> errorParamCodes = errors.stream()
                .map(ApiError::getParamCode)
                .collect(Collectors.toSet());

        Assertions.assertEquals(Set.of(TITLE_CONFLICT.getParamCode(), CODE_CONFLICT.getParamCode()), errorParamCodes);

        deleteAdmin(adminObject);
    }
}
