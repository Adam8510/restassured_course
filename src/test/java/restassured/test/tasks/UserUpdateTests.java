package restassured.test.tasks;

import org.testng.annotations.Test;
import pl.javastart.main.pojo.User;

import static io.restassured.RestAssured.given;

public class UserUpdateTests extends TestBase {
    @Test
    public void givenCorrectUserDataWhenFirstNameLastNameAreUpdatedThenUserDataIsUpdatedTest() {

        User user = new User();
        user.setId(445);
        user.setUsername("firstuser");
        user.setFirstName("Krzysztof");
        user.setLastName("Kowalski");
        user.setEmail("krzysztof@test.com");
        user.setPassword("password");
        user.setPhone("+123456789");
        user.setUserStatus(123);

        //1. Stworzyć użytkownika metodą POST
        given()
                .contentType("application/json")
                .body(user)
                .when().post("user")
                .then().statusCode(200);

        //2. Zaktualizować, użytkownika. Aktualizacji powinny ulec tylko firstname oraz lastname (dowolne poprawne wartości
        user.setFirstName("Adam");
        user.setLastName("Malinowski");

        given()
                .contentType("application/json")
                .pathParam("username", user.getUsername())
                .body(user)
                .when().put("user/{username}")
                .then().statusCode(200);

        //3. Sprawdzić, że użytkownik istnieje po aktualizacji, metodą GET
        given()
                .pathParam("username", user.getUsername())
                .when().get("user/{username}")
                .then().statusCode(200);
    }
}
