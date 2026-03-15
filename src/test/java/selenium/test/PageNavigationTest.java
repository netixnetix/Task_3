package selenium.test;

import api.StepCrudUser;
import api.StepLoginUser;
import api.config.ApiConfig;
import api.models.User;
import browser.BaseBrowser;
import browser.BrowserFactory;
import data.FakerData;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.MainPage;
import pages.ProfilePage;

import java.util.ArrayList;
import java.util.List;

public class PageNavigationTest {

    private final List<Response> authResponsesForCleanup = new ArrayList<>();

    private BaseBrowser browser;
    private User user;
    private LoginPage loginPage;
    private MainPage mainPage;
    private ProfilePage profilePage;

    @BeforeEach
    void setUp() {
        browser = BrowserFactory.createBrowser();
        loginPage = new LoginPage(browser.getDriver());
        mainPage = new MainPage(browser.getDriver());
        profilePage = new ProfilePage(browser.getDriver());

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
    
    @Test
    @DisplayName("Пользователь может перейти с главной страницы в Личный Кабинет")
    void navigateMainPageToProfilePage() {
        browser.navigateTo("login");
        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        mainPage.waitForPageLoaded();
        mainPage.clickUserProfileChapter();

        profilePage.waitForPageLoaded();
        Assertions.assertEquals(browser.getBaseUrl() + "account/profile", browser.getCurrentUrl());
    }

    @Test
    @DisplayName("Пользователь может перейти из Личного Кабинета в конструктор")
    void navigateProfilePageToMainPage() {
        browser.navigateTo("login");
        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        mainPage.waitForPageLoaded();
        browser.navigateTo("account");
        profilePage.waitForPageLoaded();
        mainPage.clickConstructorChapter();
        mainPage.waitForPageLoaded();
        Assertions.assertEquals(browser.getBaseUrl(), browser.getCurrentUrl());
    }

    @Test
    @DisplayName("Пользователь может перейти из Личного Кабинета в конструктор кликнув на Логотип в заголовке")
    void clickHeaderLogoNavigateToMainPage() {
        browser.navigateTo("login");
        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        mainPage.waitForPageLoaded();
        browser.navigateTo("account");
        profilePage.waitForPageLoaded();

        mainPage.clickHeaderLogo();
        mainPage.waitForPageLoaded();
        Assertions.assertEquals(browser.getBaseUrl(), browser.getCurrentUrl());
    }
}
