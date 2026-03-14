package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {


    WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    private final By singOutButton = By.xpath("//button[contains(@class, 'Account_button__14Yp3') and text()='Выход']");
    private final By profileSectionText = By.xpath("//p[contains(@class, 'Account_text__fZAIn') and text()='В этом разделе вы можете изменить свои персональные данные']");

    @Step("Нажать кнопку выхода")
    public void clickSingOutButton() {
        driver.findElement(singOutButton).click();
    }

    @Step("Дождаться загрузки страницы профиля пользователя")
    public void waitForPageLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(profileSectionText));
    }

}


