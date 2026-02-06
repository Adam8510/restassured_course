package restassured.test.tasks;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import pl.javastart.main.pojo.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTests extends TestBase {
    @Test
    public void givenCorrectUserDataWhenFirstNameLastNameAreUpdatedThenUserDataIsUpdatedTest() {

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType("application/json");
        RequestSpecification defaultRequestSpecification = requestSpecBuilder.build();

        User user = new User();
        user.setId(445);
        user.setUsername("firstuser");
        user.setFirstName("Krzysztof");
        user.setLastName("Kowalski");
        user.setEmail("krzysztof@test.com");
        user.setPassword("password");
        user.setPhone("+123456789");
        user.setUserStatus(123);

        ResponseSpecBuilder postResponseSpecBuilder = new ResponseSpecBuilder();
        postResponseSpecBuilder
                .expectBody("code", equalTo(200))
                .expectBody("type", equalTo("unknown"))
                .expectBody("message", equalTo("445"))
                .expectStatusCode(200);
        ResponseSpecification userCreationResponseSpecification = postResponseSpecBuilder.build();


        //1. Stworzyć użytkownika metodą POST
        given()
                //.contentType("application/json")
                .body(user)
                .when().post("user")
                .then().assertThat().spec(userCreationResponseSpecification);

        //2. Zaktualizować, użytkownika. Aktualizacji powinny ulec tylko firstname oraz lastname (dowolne poprawne wartości
        user.setFirstName("Adam");
        user.setLastName("Malinowski");

        given()
                .contentType("application/json")
                .pathParam("username", user.getUsername())
                //.body(user)
                .when().put("user/{username}")
                .then().assertThat().spec(userCreationResponseSpecification);

        //3. Sprawdzić, że użytkownik istnieje po aktualizacji, metodą GET
        given()
                .pathParam("username", user.getUsername())
                .when().get("user/{username}")
                .then().assertThat().spec(userCreationResponseSpecification);
    }
}
