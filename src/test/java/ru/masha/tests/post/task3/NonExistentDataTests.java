package ru.masha.tests.post.task3;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.masha.models.AdminObject;

import static ru.masha.UrlPaths.ADMIN_OBJECTS;
import static ru.masha.specifications.Specifications.jsonRequestSpecification;

public class NonExistentDataTests {

    //3.1
    @Test
    @DisplayName("Объект не создан, передан несуществующий \"systemCode\"")
    void checkStatusCodeInvalidData() {
        AdminObject admin = new AdminObject("codeName3", "titleName3", "HOP", "sdfsdf");

        RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //3.2
    @Test
    @DisplayName("Объект не создан, передан несуществующий \"objectCode\"")
    void checkObjectTypeCodeInvalidData() {
        AdminObject admin = new AdminObject("codeName3", "titleName3", "sdfsdf", "KEK");

        RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
