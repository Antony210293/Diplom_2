import POJO.UserCreateAccount;
import POJO.UserLogin;
import Steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {
    public static String email;
    public static String password;
    public static String name = "Антон";

    @Before
    public void setUp() {
        email = RandomStringUtils.random(10);
        password = RandomStringUtils.random(10);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка возможности создать нового уникального пользователя")
    public void createNewUser() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount)
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка не возможности создать пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, name);
        UserLogin userLogin = new UserLogin(email, password);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount);
        userSteps.userCreate(userCreateAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Проверка не возможности создать пользователя без поля email")
    public void createUserWithoutEmail() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(" ", password, name);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Проверка не возможности создать пользователя без поля password")
    public void createUserWithoutPassword() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, " ", name);
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Проверка не возможности создать пользователя без поля name")
    public void createUserWithoutName() {
        UserCreateAccount userCreateAccount = new UserCreateAccount(email, password, " ");
        UserSteps userSteps = new UserSteps();
        userSteps.userCreate(userCreateAccount)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
}
