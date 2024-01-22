package ru.masha.tests.post.task2;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.masha.models.AdminObject;
import ru.masha.models.AdminObjectDeleteRequest;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static ru.masha.UrlPaths.ADMIN_OBJECTS;
import static ru.masha.specifications.Specifications.jsonRequestSpecification;
import static ru.masha.specifications.Specifications.jsonRequestSpecificationEmpty;

public class RequiredFieldTests {

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(Named.of(AdminObject.Fields.title, (BiConsumer<AdminObject, String>) AdminObject::setTitle)),
                Arguments.of(Named.of(AdminObject.Fields.code, (BiConsumer<AdminObject, String>) AdminObject::setCode)),
                Arguments.of(Named.of(AdminObject.Fields.systemCode, (BiConsumer<AdminObject, String>) AdminObject::setSystemCode)),
                Arguments.of(Named.of(AdminObject.Fields.objectTypeCode, (BiConsumer<AdminObject, String>) AdminObject::setObjectTypeCode))
        );
    }

    @MethodSource("source")
    @DisplayName("Объект не создан, не заполнено обязательное поле")
    @ParameterizedTest
    void checkRequiredField(BiConsumer<AdminObject, ?> setter) {
        AdminObject admin = new AdminObject("codeNamE", "titleNamE", "HEY", "KEK");

        AdminObject adminResponse = RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log()
                .all()
                .extract()
                .response()
                .body()
                .as(AdminObject.class);

        Assertions.assertEquals(admin.getTitle(), adminResponse.getTitle());
        Assertions.assertEquals(admin.getCode(), adminResponse.getCode());

        RestAssured.given()
                .spec(jsonRequestSpecificationEmpty())
                .body(new AdminObjectDeleteRequest(adminResponse.getId(), admin.getSystemCode()))
                .delete(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        setter.accept(admin, null);

        RestAssured.given()
                .spec(jsonRequestSpecification())
                .body(admin)
                .post(ADMIN_OBJECTS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
