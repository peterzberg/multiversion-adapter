package dev.zberg.quarkus.multiversion.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MultiversionResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/multiversion")
                .then()
                .statusCode(200)
                .body(is("Hello multiversion. I am version v2"));
    }
}
