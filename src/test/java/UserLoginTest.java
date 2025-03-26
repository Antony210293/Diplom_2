import pojo.UserCreateAccount;
import pojo.UserLogin;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest {

    public static String email = "bry4n24@yandex.ru";
    public static String password = "asdf1234";
    public static String name = "Антон";

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLogin userLogin = new UserLogin (email, password);
        userSteps.userDeleteAfterLogin(userLogin);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка возможности логина под существующим пользователем")
    public void userLogin() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userLogin(userLogin)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверка не возможности логина с неверным email")
    public void userLoginWithWrongEmail() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userWrongLogin = new UserLogin ("55555", password);
        UserLogin userRightLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userLogin(userWrongLogin)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("Логин с неверным password")
    @Description("Проверка не возможности логина с неверным password")
    public void userLoginWithWrongPassword() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userWrongLogin = new UserLogin(email, "88888888");
        UserLogin userRightLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userLogin(userWrongLogin)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
