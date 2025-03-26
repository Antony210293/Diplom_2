import pojo.UserCreateAccount;
import pojo.UserLogin;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserChangeTest {

    public static String email = "bry4n24@yandex.ru";
    public static String password = "asdf1234";
    public static String name = "Антон";

    public static String newEmail = "bryan_24@mail.ru";
    public static String newPassword = "qwer1234";
    public static String newName = "Алексей";

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLogin userLogin = new UserLogin(email, password);
        userSteps.userDeleteAfterLogin(userLogin);
    }

    @Test
    @DisplayName("Обновление email с авторизацией")
    @Description("Проверка возможности обновления поля email с авторизацией")
    public void userChangeEmailWithAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserCreateAccount userChangeAccount = new UserCreateAccount(newEmail, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserLogin newUserLogin = new UserLogin(newEmail, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userDeleteAfterLogin(userLogin)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.email", equalTo(newEmail))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Обновление email без авторизации")
    @Description("Проверка не возможности обновления поля email без авторизации")
    public void userEditEmailWithoutAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserCreateAccount userChangeAccount = new UserCreateAccount(newEmail, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userEdit(userChangeAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Обновление password с авторизацией")
    @Description("Проверка возможности обновления поля password с авторизацией")
    public void userEditPasswordWithAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserCreateAccount userChangeAccount = new UserCreateAccount(email, newPassword, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserLogin newUserLogin = new UserLogin(email, newPassword);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userChangeAfterLogin(userLogin, userChangeAccount)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        userSteps.userLogin(newUserLogin)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Обновление password без авторизации")
    @Description("Проверка не возможности обновления поля password без авторизации")
    public void userEditPasswordWithoutAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserCreateAccount userChangeAccount = new UserCreateAccount(email, newPassword, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userEdit(userChangeAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Обновление name с авторизацией")
    @Description("Проверка возможности обновления поля name с авторизацией")
    public void userEditNameWithAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserCreateAccount userChangeAccount = new UserCreateAccount(email, password, newName);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userChangeAfterLogin(userLogin, userChangeAccount)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.name", equalTo(newName))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Обновление name без авторизации")
    @Description("Проверка не возможности обновления поля name без авторизации")
    public void userEditNameWithoutAuthorization() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserCreateAccount userChangeAccount = new UserCreateAccount(email, password, newName);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userEdit(userChangeAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
