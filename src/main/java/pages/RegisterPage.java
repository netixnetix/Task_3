package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {

    WebDriver driver;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By nameInput = By.xpath("//label[text()='Имя']/parent::div/input");
    private final By emailInput = By.xpath("//label[text()='Email']/parent::div/input");
    private final By passwordInput = By.xpath("//label[text()='Пароль']/parent::div/input");
    private final By registerButton = By.xpath("//button[text()='Зарегистрироваться']");
    private final By passwordInputError = By.xpath("//p[contains(@class, 'input__error') and text()='Некорректный пароль']");
    private final By singInButton = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");


    @Step("Нажать ссылку «Войти»")
    public void clickSingInButton() {
        driver.findElement(singInButton).click();
    }

    @Step("Проверить отображение ошибки пароля")
    public void checkPwdErrorVisible() {
        driver.findElement(passwordInputError).isDisplayed();
    }

    @Step("Ввести имя в поле регистрации")
    public void setName(String name) {
        driver.findElement(nameInput).clear();
        driver.findElement(nameInput).sendKeys(name);
    }

    @Step("Ввести email в поле регистрации")
    public void setEmail(String name) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(name);
    }

    @Step("Ввести пароль в поле регистрации")
    public void setPassword(String name) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(name);
    }

    @Step("Нажать кнопку «Зарегистрироваться»")
    public void clickRegisterButton() {
        driver.findElement(registerButton).click();
    }

}
