package ru.masha.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;
import static ru.masha.ApplicationProperty.getBaseUrl;

public class Specifications {

    public static RequestSpecification jsonRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setRelaxedHTTPSValidation()
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }

    public static RequestSpecification jsonRequestSpecificationEmpty() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setRelaxedHTTPSValidation()
                .setContentType(JSON)
                .build();
    }
}
