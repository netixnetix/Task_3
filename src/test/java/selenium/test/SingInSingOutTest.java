package selenium.test;

import api.StepCrudUser;
import api.StepLoginUser;
import api.models.User;
import browser.BaseBrowser;
import browser.BrowserFactory;
import browser.LocalStoragesUtils;
import data.FakerData;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.*;
import api.config.ApiConfig;

import java.util.ArrayList;
import java.util.List;

public class SingInSingOutTest {

    private final List<Response> authResponsesForCleanup = new ArrayList<>();

    private BaseBrowser browser;
    private User user;
    private MainPage mainPage;
    private LoginPage loginPage;
    private ForgotPassword forgotPasswordPages;
    private RegisterPage registerPage;
    private ProfilePage profilePage;

    @BeforeEach
    void setUp() {
        browser = BrowserFactory.createBrowser();
        mainPage = new MainPage(browser.getDriver());
        loginPage = new LoginPage(browser.getDriver());
        registerPage = new RegisterPage(browser.getDriver());
        forgotPasswordPages = new ForgotPassword(browser.getDriver());
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
    @DisplayName("Пользователь может успешно пройти авторизацию, через главную страницу, пользователю устанавливают токены в LocalStorages")
    public void validSingInFromMainPage(){

        browser.navigateToMain();
        mainPage.clickSingInButton();
        LocalStoragesUtils.verifyNoTokensInLocalStorage(browser.getDriver());
        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();
        LocalStoragesUtils.verifyTokensInLocalStorage(browser.getDriver());

        mainPage.isOnMainPage();
        mainPage.verifyUserAuthorized();

    }

    @Test
    @DisplayName("Пользователь может успешно пройти авторизацию через Личный Кабинет, пользователю устанавливают токены в LocalStorages")
    public void validSingInFromPersonaProfile() throws InterruptedException {

        browser.navigateTo("account");
        LocalStoragesUtils.verifyNoTokensInLocalStorage(browser.getDriver());
        loginPage.waitForPageLoaded();

        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        LocalStoragesUtils.verifyTokensInLocalStorage(browser.getDriver());
        mainPage.isOnMainPage();
        mainPage.verifyUserAuthorized();

    }

    @Test
    @DisplayName("Пользователь может успешно пройти авторизацию из раздела восстановления пароля, пользователю устанавливают токены в LocalStorages")
    public void validSingInFromResetPasswordChapter() {

        browser.navigateTo("forgot-password");
        LocalStoragesUtils.verifyNoTokensInLocalStorage(browser.getDriver());

        forgotPasswordPages.clickSingInButton();
        loginPage.waitForPageLoaded();

        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        LocalStoragesUtils.verifyTokensInLocalStorage(browser.getDriver());
        mainPage.isOnMainPage();
        mainPage.verifyUserAuthorized();

    }

    @Test
    @DisplayName("Пользователь может успешно пройти авторизацию из раздела регистрации, пользователю устанавливают токены в LocalStorages")
    public void validSingInFromRegisterChapter() {

        browser.navigateTo("register");
        LocalStoragesUtils.verifyNoTokensInLocalStorage(browser.getDriver());

        registerPage.clickSingInButton();
        loginPage.waitForPageLoaded();

        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();

        LocalStoragesUtils.verifyTokensInLocalStorage(browser.getDriver());
        mainPage.isOnMainPage();
        mainPage.verifyUserAuthorized();

    }

    @Test
    @DisplayName("Пользователь может выйти из учетной записи в разделе профиля пользователя")
    public void SingOutFromProfilePage(){

        browser.navigateTo("account");
        LocalStoragesUtils.verifyNoTokensInLocalStorage(browser.getDriver());
        loginPage.waitForPageLoaded();

        loginPage.setPassword(user.getPassword());
        loginPage.setEmail(user.getEmail());
        loginPage.clickSingInButton();
        LocalStoragesUtils.verifyTokensInLocalStorage(browser.getDriver());

        browser.navigateTo("account");
        profilePage.waitForPageLoaded();
        profilePage.clickSingOutButton();

        loginPage.waitForPageLoaded();
        Assertions.assertEquals(browser.getBaseUrl()+"login", browser.getCurrentUrl());
        LocalStoragesUtils.verifyNoTokensInLocalStorage(browser.getDriver());






    }

}