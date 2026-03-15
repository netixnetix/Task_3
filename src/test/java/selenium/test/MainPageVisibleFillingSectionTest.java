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
import pages.LoginPage;
import pages.MainPage;
import api.config.ApiConfig;

import java.util.ArrayList;
import java.util.List;

public class MainPageVisibleFillingSectionTest {

    private final List<Response> authResponsesForCleanup = new ArrayList<>();

    private BaseBrowser browser;
    private User user;
    private LoginPage loginPage;
    private MainPage mainPage;

    @BeforeEach
    void setUp() {
        browser = BrowserFactory.createBrowser();
        loginPage = new LoginPage(browser.getDriver());
        mainPage = new MainPage(browser.getDriver());

        ApiConfig.setUp();
        user = new User(FakerData.email(), FakerData.name(), FakerData.pwd());
        StepCrudUser.create(user);
        authResponsesForCleanup.add(StepLoginUser.signIn(user));
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

    @ParameterizedTest
    @CsvSource({"Соусы", "Начинки"})
    @DisplayName("Главная страница: при выборе таба отображается соответствующий раздел (переход в невыбранные табы, скролл вниз)")
    void sectionVisibleAfterTabClick(String sectionName) {
        browser.navigateTo("login");
        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        mainPage.waitForPageLoaded();
        mainPage.clickTabAndVerifySectionVisible(sectionName);
    }

    @Test
    @DisplayName("Главная страница: возврат в ранее посещенный таб, при выборе 'Начинки >> Булки' происходит скролл вверх к разделу Булки")
    void baseSectionVisibleAfterTabClick() {
        browser.navigateTo("login");
        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        mainPage.waitForPageLoaded();
        mainPage.clickTabAndVerifySectionVisible("Начинки");
        mainPage.clickTabAndVerifySectionVisible("Булки");

    }

}
