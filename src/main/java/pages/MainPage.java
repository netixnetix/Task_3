package pages;

import api.config.ApiConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By singInButton = By.xpath("//button[text()='Войти в аккаунт']");
    private final By createBurgerTitle = By.xpath("//h1[text()='Соберите бургер']");
    private final By userProfileChapter = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va ml-2') and text()='Личный Кабинет']");
    private final By constructorChapter = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va ml-2') and text()='Конструктор']");
    private final By headerLogo = By.className("AppHeader_header__logo__2D0X2");
    private final By createOrder = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Оформить заказ']");


    @Step("Дождаться загрузки главной страницы")
    public void waitForPageLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(createBurgerTitle));
    }


    @Step("Выбрать таб раздела и проверить видимость секции")
    public void clickTabAndVerifySectionVisible(String sectionName) {
        By tabBy = By.xpath("//div[contains(@class,'noselect')]//span[text()='" + sectionName + "']/parent::div");
        By sectionHeader = By.xpath("//h2[text()='" + sectionName + "']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(tabBy).click();
        wait.until(ExpectedConditions.and(
                ExpectedConditions.attributeContains(tabBy, "class", "tab_tab_type_current"),
                ExpectedConditions.visibilityOfElementLocated(sectionHeader)));}


    @Step("Нажать кнопку «Войти в аккаунт»")
    public void clickSingInButton() {
        driver.findElement(singInButton).click();
    }

    @Step("Нажать на раздел Личный Кабинет в заголовке страницы")
    public void clickUserProfileChapter() {
        driver.findElement(userProfileChapter).click();
    }

    @Step("Нажать на раздел Конструктор в заголовке страницы")
    public void clickConstructorChapter() {
        driver.findElement(constructorChapter).click();
    }

    @Step("Нажать на логотип в заголовке страницы")
    public void clickHeaderLogo() {
        driver.findElement(headerLogo).click();
    }


    private boolean isOnMainUrl(WebDriver webDriver, String base) {
        String currentUrl = webDriver.getCurrentUrl();
        return currentUrl.equals(base) || currentUrl.equals(base + "/");
    }

    @Step("Проверить, что открыта главная страница")
    public boolean isOnMainPage() {
        String base = ApiConfig.getBaseUrl().replaceAll("/$", "");
        return isOnMainUrl(driver, base);
    }

    @Step("Проверить на главной странице наличие признаков авторизованного пользователя")
    public void verifyUserAuthorized() {
        String base = ApiConfig.getBaseUrl().replaceAll("/$", "");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if (isOnMainUrl(driver, base) && !driver.findElement(createOrder).isDisplayed())
            throw new AssertionError("На главной ожидалась кнопка 'Оформить заказ'");
        driver.get(base + "/login");
        wait.until(webDriver -> isOnMainUrl(webDriver, base));
        if (!driver.findElement(createOrder).isDisplayed())
            throw new AssertionError("На главной должна быть кнопка 'Оформить заказ'");
    }
}

