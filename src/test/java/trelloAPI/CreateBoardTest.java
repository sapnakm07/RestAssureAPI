package trelloAPI;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.*;


public class CreateBoardTest extends BaseClassTest {

    @Test(priority = 1)
    public void ViewCreatedList()
    {
        RequestSpecification requestSpecification=
                given()
                    .queryParam("key",keyid)
                    .queryParam("token",tokenid)
                    .pathParam("boardid", boardid).log().all()
                        .contentType(ContentType.JSON);

        Response response =requestSpecification
                .when()
                .get("/boards/{boardid}/lists");

        List<String> expectedList = new ArrayList<>();
        expectedList.add("To Do");
        expectedList.add("Doing");
        expectedList.add("Done");

        List<Map<String,?>> mapList = response.jsonPath().get();

        for (int i=0;i<mapList.size();i++)
        {
            Assert.assertTrue(mapList.get(i).containsValue(expectedList.get(i)));
        }

    }

    @Test(priority = 2)
    public void CreateListWithinBoard()
    {
        RequestSpecification requestSpecification=
                given()
                        .queryParam("key",keyid)
                        .queryParam("token",tokenid)
                        .pathParam("boardid", boardid)
                        .queryParam("name",boardname)
                        .contentType(ContentType.JSON)
                .log().all();

        Response response =requestSpecification
                .when()
                .post("/boards/{boardid}/lists");
    }

    @Test(priority = 3)
    public void GetCreatedList()
    {
        RequestSpecification requestSpecification=
                given()
                        .queryParam("key",keyid)
                        .queryParam("token",tokenid)
                        .pathParam("boardid", boardid).log().all()
                        .contentType(ContentType.JSON);

        Response response =requestSpecification
                .when()
                .get("/boards/{boardid}/lists");

        List<Map<String,?>> mapList = response.jsonPath().get();
        listid = mapList.get(0).get("id").toString();

        System.out.println(boardname);
        for (int i=0;i<mapList.size();i++)
        {
            System.out.println(mapList.get(i));
        }

    }

    @Test(priority = 4)
    public void CreateCardWithinList()
    {
        RequestSpecification requestSpecification=
                given()
                        .queryParam("key",keyid)
                        .queryParam("token",tokenid)
                        .queryParam("idList", listid)
                        .queryParam("name","Card1")
                        .contentType(ContentType.JSON)
                        .log().all();

        Response response =requestSpecification
                .when()
                .post("cards");

        Map<Object, Object> map = response.jsonPath().getMap("$");

        cardid = map.get("id").toString();
    }

    @Test(priority = 5)
    public void UpdateCardWithinList()
    {
        RequestSpecification requestSpecification=
                given()
                        .pathParam("id",cardid)
                        .queryParam("key",keyid)
                        .queryParam("token",tokenid)
                        .queryParam("name", "Card001")
                        .queryParam("desc","Updated Card Name")
                        .contentType(ContentType.JSON)
                        .log().all();

        Response response =requestSpecification
                .when()
                .put("cards/{id}");

        Map<Object, Object> map = response.jsonPath().getMap("$");

        cardid = map.get("id").toString();

    }
}