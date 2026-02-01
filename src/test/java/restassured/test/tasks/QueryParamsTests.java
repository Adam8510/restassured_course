package restassured.test.tasks;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import pl.javastart.main.pojo.Category;
import pl.javastart.main.pojo.Pet;
import pl.javastart.main.pojo.Tag;

import static org.testng.Assert.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class QueryParamsTests extends TestBase{
    @Test
    public void givenExistingPetWithStatusSoldWhenGetPetWithSoldStatusThenPetWithStatusIsReturnedTest() {
        Category category = new Category();
        category.setId(666);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setId(777);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("sold");

        given().log().all().body(pet).contentType("application/json")
                .when().post("pet")
                .then().log().all().statusCode(200);

        Pet[] pets = given().log().all().body(pet).contentType("application/json")
                .queryParam("status", "sold")
                .when().get("pet/findByStatus")
                .then().log().all().statusCode(200).extract().as(Pet[].class);

        assertTrue(Arrays.asList(pets).size() > 0, "List of pets");
        System.out.printf("aaa number of pets = " + Arrays.asList(pet).size());

        String actualCategoryName = given().log().method().log().uri()
                .pathParam("petId", pet.getId())
                .when().get("pet/{petId}")
                .then().log().all().statusCode(200)
                .extract().jsonPath().getString("category.name");

        List<Pet> pets1 = given().log().all().body(pet).contentType("application/json")
                .queryParam("status", "sold")
                .when().get("pet/findByStatus")
                .then().log().all().statusCode(200).extract().jsonPath().getList("", Pet.class);

        assertTrue(pets1.size() > 0, "List of pets");
    }

    @Test
    public void givenPetWhenPostPetThenPetIsCreatedTest() {
        Category category = new Category();
        category.setId(1);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setName("Burek");
        pet.setId(123);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("available");


        given().log().all().body(pet).contentType("application/json")
                .when().post("pet")
                .then().log().all().statusCode(200);

        JsonPath jsonPathResponse = given().log().method().log().uri()
                .pathParam("petId", pet.getId())
                .when().get("pet/{petId}")
                .then().log().all().statusCode(200)
                .extract().jsonPath();

        String petName = jsonPathResponse.getString("name");
        String actualCategoryName = jsonPathResponse.getString("category.name");
        String tagName = jsonPathResponse.getString("tags[0].name");

        assertEquals(petName, pet.getName(), "Pet name");
        assertEquals(actualCategoryName, category.getName(), "Category name");
        assertEquals(tagName, pet.getTags().get(0).getName(), "Pet tag name");

        System.out.println("aaa tag " +tagName);
    }
}