package Steps;

import io.restassured.http.ContentType;
import pojo.OrderCreate;
import pojo.UserLogin;
import pojo.UserLoginResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static Andpoints.ApiEndpoint.*;
import static Steps.UserSteps.requestSpecification;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание нового заказа без авторизации")
    public ValidatableResponse orderCreate(OrderCreate orderCreate) {
        return requestSpecification()
                .body(orderCreate)
                .post(ORDERS)
                .then();
    }
    @Step("Создание нового заказа после авторизации")
    public ValidatableResponse orderCreateAfterLogin(UserLogin userLoginRequest, OrderCreate orderCreateRequest) {
        UserSteps userSteps = new UserSteps();
        Response response = userSteps.userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .post(ORDERS)
                .then();
    }
    @Step("Получение заказов без авторизации")
    public ValidatableResponse orderList() {
        return requestSpecification()
                .get(ORDERS)
                .then();
    }
    @Step("Получение заказов после авторизации")
    public ValidatableResponse orderListAfterLogin(UserLogin userLoginRequest) {
        UserSteps userSteps = new UserSteps();
        Response response = userSteps.userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .get(ORDERS)
                .then();
    }

    public List<String> getIngredients() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .when()
                .get(INGREDIENTS)
                .then().log().all()
                .extract().jsonPath().getList("data._id");
    }
}
