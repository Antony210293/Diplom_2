import POJO.OrderCreate;
import POJO.UserCreateAccount;
import POJO.UserLogin;
import Steps.OrderSteps;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class OrderCreateTest {

    public static String email = "bry4n24@yandex.ru";
    public static String password = "asdf1234";
    public static String name = "Антон";
    public static List<String> ingredients = new ArrayList<>();
    private boolean skipDeleteUser = false;

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLogin userLogin = new UserLogin(email, password);
        userSteps.userDeleteAfterLogin(userLogin);
    }

    @After
    public void ingredientsClean() {
        if (!skipDeleteUser) {
            ingredients.clear();
        }
    }

    @Test
    @DisplayName("Создание заказа после авторизации")
    @Description("Проверка возможности создания заказа после авторизации")
    public void orderCreateWithAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLoginRequest = new UserLogin(email, password);
        OrderCreate orderCreate = new OrderCreate(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(userCreateAccount);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreate)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.owner.email", equalTo(email))
                .and()
                .statusCode(200);;
    }

    @Test
    @DisplayName("Создание заказа после авторизации")
    @Description("Проверка возможности создания заказа после авторизации")
    public void orderCreateWithAuthorizationIngredients() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLoginRequest = new UserLogin(email, password);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        OrderCreate orderCreate = new OrderCreate(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(userCreateAccount);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreate)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.owner.email", equalTo(email))
                .and()
                .statusCode(200);;
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка возможности создания заказа без авторизации")
    public void orderCreateWithoutAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        OrderCreate orderCreateRequest = new OrderCreate(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(userCreateAccount);
        orderSteps.orderCreate(orderCreateRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.number", isA(Integer.class))
                .and()
                .statusCode(200);;
    }

    @Test
    @DisplayName("Создание заказа после авторизации без ингредиентов")
    @Description("Проверка не возможности создания заказа после авторизации без ингредиентов")
    public void orderCreateWithAuthorizationWithoutIngredients() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        OrderCreate orderCreate = new OrderCreate(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(userCreateAccount);
        orderSteps.orderCreateAfterLogin(userLogin, orderCreate)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(400);;
    }

    @Test
    @DisplayName("Создание заказа после авторизации с неверным ингредиентом")
    @Description("Проверка не возможности создания заказа после авторизации без ингредиентов")
    public void orderCreateWithAuthorizationWithWrongIngredients() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("wrongIngredients");
        OrderCreate orderCreate = new OrderCreate(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(userCreateAccount);
        orderSteps.orderCreateAfterLogin(userLogin, orderCreate)
                .assertThat().statusCode(500);
    }
}
