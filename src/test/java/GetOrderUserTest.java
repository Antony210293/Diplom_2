import POJO.UserCreateAccount;
import POJO.UserLogin;
import Steps.OrderSteps;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

public class GetOrderUserTest {

    public static String email = "bry4n24@yandex.ru";
    public static String password = "asdf1234";
    public static String name = "Антон";
    private boolean skipDeleteUser = false;

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLogin userLogin = new UserLogin(email, password);
        userSteps.userDeleteAfterLogin(userLogin);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Проверка не возможности получения списка заказов без авторизации")
    public void orderListWithoutAuthorization() {
        skipDeleteUser = true;
        OrderSteps orderSteps = new OrderSteps();
        orderSteps.orderList()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);;
    }

    @Test
    @DisplayName("Получение списка заказов после авторизации")
    @Description("Проверка не возможности получения списка заказов после авторизации")
    public void orderListWithAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLoginRequest = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();
        userSteps.userCreate(userCreateAccount);
        orderSteps.orderListAfterLogin(userLoginRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("orders",instanceOf(List.class))
                .and()
                .statusCode(200);;
    }
}
