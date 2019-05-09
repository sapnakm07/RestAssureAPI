package trelloAPI;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.*;
//import sun.rmi.transport.Endpoint;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BaseClassTest {

    public String keyid;
    public String tokenid;
    public String boardid;
    public String boardname;
    public String listid;
    public String cardid;

    @BeforeSuite
    public void Setup()
    {
        baseURI = "https://api.trello.com/1/";
        keyid ="0697ace29da135af1009cc535346c753";
        tokenid = "93b636a0325403bc17358cfbb9ddcd35bd9b8d21823743b4accd14d3465c13a6";
        boardname = "T1";
        createBoard();
    }

    @Test
    public String createBoard()
    {
        RequestSpecification requestSpecification =
                given()
                        .queryParam("key", keyid)
                        .queryParam("token", tokenid)
                        .queryParam("name", boardname)
                        .contentType(ContentType.JSON)
                        .log().all();

        Response response =requestSpecification
                .when()
                .post("boards/");

        response
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("createBoard.json"))
                .body("[0].name",equalTo("boardname"))
                .extract()
                .response()
                .jsonPath()
                .getMap("$");

        Map<Object,Object> map = response.jsonPath().getMap("$");

        boardid= map.get("id").toString();

        return boardid;
    }

    @AfterSuite
    public void TearDown()
   {
       DeleteBoard(boardid);
   }

    public void DeleteBoard(String boardid)
    {
        RequestSpecification requestSpecification =
                given().queryParam("key", keyid)
                        .queryParam("token", tokenid)
                        .pathParam("id",boardid)
                        .log().all().contentType(ContentType.JSON);

        Response response =requestSpecification
                .when()
                .delete("/boards/{id}");
    }
}
