package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class LabThree {

    private static final String baseUrl = "https://e8020285-4bc5-40eb-8815-95a4472302af.mock.pstmn.io";

    @BeforeClass
    public void setup(){
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test()
    public void verifyGetSuccess(){
        given().get("/ownerName/success")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test(dependsOnMethods = "verifyGetSuccess")
    public void verifyGetUnsuccess(){
        given().get("/ownerName/unsuccess")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test(dependsOnMethods = "verifyGetUnsuccess")
    public void verifyPost200(){
        Response response = given().post("/createSomething?permission=yes");
        response.then().statusCode(HttpStatus.SC_OK);
        System.out.println(response.jsonPath().get("result").toString());
    }

    @Test(dependsOnMethods = "verifyPost200")
    public void verifyPost400(){
        Response response = given().post("/createSomething");
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        System.out.println(response.jsonPath().get("result").toString());
    }

    @Test(dependsOnMethods = "verifyPost400")
    public void verifyPut(){

        Map<String, ?> body = Map.of(
                "name", "Oleh",
                "surname", "Zaeits"
        );

        given().body(body)
                .header("Content-Type", "application/json")
                .put("/updateMe")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(dependsOnMethods = "verifyPut")
    public void verifyDelete(){
        Response response = given().header("SessionID", "123456789").delete("/deleteWorld");
        response.then().statusCode(HttpStatus.SC_GONE);
        System.out.println(response.jsonPath().get("world").toString());
    }


}
