package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPassword {


    WebDriver driver;

    public ForgotPassword(WebDriver driver) {
        this.driver = driver;
    }

    private final By singInButton = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");
    private final By resetButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Восстановить']");

    @Step("Нажать кнопку входа")
    public void clickSingInButton() {
        driver.findElement(singInButton).click();
    }

    @Step("Дождаться загрузки страницы восстановления пароля")
    public void waitForPageLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(resetButton));
    }

}


