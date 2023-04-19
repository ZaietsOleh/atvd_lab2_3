package org.example.lab2;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PetTest {

    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String PET = "/pet";
    private static final String apiKey = "special-key";
    private static final int PET_ID = 122201;

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyPostNewPet() {
        Map<String, ?> body = Map.of(
                "id", PET_ID,
                "category", Map.of(
                        "id", 0,
                        "name", "122-20ск-1"
                ),
                "name", "Zaiets Oleh",
                "photoUrls", List.of("photoUrls"),
                "tags", List.of(
                        Map.of(
                                "id", 0,
                                "name", Faker.instance().dog().gender()
                        )
                ),
                "status", "available"
        );

        given().contentType(ContentType.JSON)
                .body(body)
                .header("api_key", apiKey)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyPostNewPet")
    public void verifyGetPet() {
        given().pathParams("petId", PET_ID)
                .header("api_key", apiKey)
                .get(PET + "/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyGetPet", priority = 3)
    public void verifyPutPet() {
        Map<String, ?> body = Map.of(
                "id", PET_ID,
                "category", Map.of(
                        "id", 0,
                        "name", "122-20ск-1"
                ),
                "name", "Zaiets Oleh",
                "photoUrls", List.of("no photo"),
                "tags", List.of(
                        Map.of(
                                "id", 0,
                                "name", Faker.instance().dog().gender()
                        )
                ),
                "status", "on vacation"
        );

        given().contentType(ContentType.JSON)
                .body(body)
                .header("api_key", apiKey)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyPutPet")
    public void verifyDeletePet() {
        given().pathParams("petId", PET_ID)
                .header("api_key", apiKey)
                .delete(PET + "/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
