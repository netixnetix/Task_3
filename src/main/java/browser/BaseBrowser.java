package browser;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class BaseBrowser {
    protected WebDriver driver;
    protected String baseURL;

    public BaseBrowser(WebDriver driver, String baseURL) {
        this.driver = driver;
        this.baseURL = baseURL;
        driver.manage().window().maximize();
    }

    @Step("Получить экземпляр WebDriver")
    public WebDriver getDriver() {
        return driver;
    }

    @Step("Закрыть браузер")
    public void quit() {
        driver.quit();
    }

    @Step("Перейти на главную страницу")
    public void navigateToMain() {
        driver.get(baseURL);
    }

    @Step("Перейти по пути: {path}")
    public void navigateTo(String path) {
        driver.get(baseURL + path);
    }

    @Step("Получить текущий URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Получить базовый URL")
    public String getBaseUrl() {
        return baseURL;
    }
}