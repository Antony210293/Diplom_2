package Steps;
import Andpoints.ApiEndpoint;
import pojo.UserCreateAccount;
import pojo.UserLogin;
import pojo.UserLoginResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static Andpoints.ApiEndpoint.*;
import static io.restassured.RestAssured.given;

public class UserSteps {

    public static RequestSpecification requestSpecification() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(ApiEndpoint.BASE_URL);
    }
    @Step("Создание нового пользователя")
    public ValidatableResponse userCreate(UserCreateAccount userCreateAccount) {
        return requestSpecification()
                .body(userCreateAccount)
                .post(USER_CREATE)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userLogin(UserLogin userLogin) {
        return requestSpecification()
                .body(userLogin)
                .post(USER_LOGIN)
                .then();
    }

    @Step("Удаление пользователя без авторизации")
    public ValidatableResponse userDelete(String accessToken) {
        return requestSpecification()
                .header("Authorization", accessToken)
                .delete(USER)
                .then();
    }

    @Step("Удаление пользователя после авторизации")
    public ValidatableResponse userDeleteAfterLogin(UserLogin userLogin) {
        Response response = userLogin(userLogin)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return userDelete(accessToken);
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse userEdit(UserCreateAccount userCreateAccount) {
        return requestSpecification()
                .body(userCreateAccount)
                .patch(USER)
                .then();
    }

    @Step("Изменение данных пользователя после авторизации")
    public ValidatableResponse userChangeAfterLogin(UserLogin userLogin, UserCreateAccount userCreateAccount) {
        Response response = userLogin(userLogin)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .body(userCreateAccount)
                .patch(USER)
                .then();
    }
}
