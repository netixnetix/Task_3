package selenium.test;

import api.StepCrudUser;
import api.StepLoginUser;
import api.models.User;
import browser.BaseBrowser;
import browser.BrowserFactory;
import data.FakerData;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.RegisterPage;
import api.config.ApiConfig;

import java.util.ArrayList;
import java.util.List;

public class RegistrationTest {

    private final List<Response> authResponsesForCleanup = new ArrayList<>();

    private RegisterPage registerPage;
    private BaseBrowser browser;

    @BeforeEach
    void setUp() {
        browser = BrowserFactory.createBrowser();
        ApiConfig.setUp();
        registerPage = new RegisterPage(browser.getDriver());
    }

    @AfterEach
    void tearDown() {
        for (Response authResponse : authResponsesForCleanup) {
            if (authResponse != null && authResponse.getStatusCode() == 200) {
                StepCrudUser.deleteUser(authResponse);
            }
        }
        authResponsesForCleanup.clear();
        browser.quit();
    }

    @Test
    @DisplayName("Пользователь может успешно пройти регистрацию на сайте, после регистрации учетная запись доступна для входа")
    public void validRegistration() {
        User user = new User(FakerData.email(), FakerData.name(), FakerData.pwd());

        browser.navigateTo("register");
        registerPage.setEmail(user.getEmail());
        registerPage.setName(user.getName());
        registerPage.setPassword(user.getPassword());
        registerPage.clickRegisterButton();

        StepLoginUser.checkSingInResponseUserData(user);
        authResponsesForCleanup.add(StepLoginUser.signIn(user));
    }

    @ParameterizedTest
    @CsvSource({"1", "Abcde"})
    @DisplayName("Пользователь не может завершить регистрацию на сайте, если укажет пароль менее чем 6 символов")
    public void cannotRegisterWithShortPassword(String password){
        User user = new User(FakerData.email(), FakerData.name(), password);

        browser.navigateTo("register");
        registerPage.setEmail(user.getEmail());
        registerPage.setName(user.getName());
        registerPage.setPassword(user.getPassword());
        registerPage.clickRegisterButton();
        registerPage.checkPwdErrorVisible();
        StepLoginUser.checkInCorrectSingIn(user);

    }

}