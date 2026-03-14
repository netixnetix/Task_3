package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }


    private final By emailInput = By.xpath("//label[text()='Email']/parent::div/input");
    private final By passwordInput = By.xpath("//label[text()='Пароль']/parent::div/input");
    private final By singInButton = By.xpath("//button[text()='Войти']");
    private final By passwordInputError = By.xpath("//p[contains(@class, 'input__error') and text()='Некорректный пароль']");

    @Step("Дождаться загрузки страницы входа")
    public void waitForPageLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(singInButton));
    }

    @Step("Ввести email на странице входа")
    public void setEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
    }

    @Step("Ввести пароль на странице входа")
    public void setPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    @Step("Нажать кнопку «Войти»")
    public void clickSingInButton() {
        driver.findElement(singInButton).click();
    }
}


