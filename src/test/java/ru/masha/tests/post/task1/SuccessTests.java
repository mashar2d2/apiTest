package ru.masha.tests.post.task1;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.masha.models.AdminObject;
import ru.masha.models.AdminObjectDeleteRequest;

import static ru.masha.UrlPaths.ADMIN_OBJECTS;
import static ru.masha.specifications.Specifications.jsonRequestSpecification;
import static ru.masha.specifications.Specifications.jsonRequestSpecificationEmpty;

public class SuccessTests {

    @Test
    @DisplayName("Успешное добавление объекта")
    void checkStatusCodeOK() {
        AdminObject admin = new AdminObject("hip", "hat", "HOP", "LOL");
        AdminObject adminResponse = RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .body()
                .as(AdminObject.class);

        RestAssured.given()
                .spec(jsonRequestSpecificationEmpty())
                .body(new AdminObjectDeleteRequest(adminResponse.getId(), admin.getSystemCode()))
                .delete(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
