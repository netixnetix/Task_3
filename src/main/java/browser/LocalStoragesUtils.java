package browser;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LocalStoragesUtils {

    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String REFRESH_TOKEN_KEY = "refreshToken";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_PATTERN = BEARER_PREFIX + "[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+";


    private static String getLocalStorageItem(WebDriver driver, String key) {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return window.localStorage.getItem(arguments[0]);", key);
    }

    @Step("Проверить, что в LocalStorage нет accessToken и refreshToken")
    public static void verifyNoTokensInLocalStorage(WebDriver driver) {
        String accessToken = getLocalStorageItem(driver, ACCESS_TOKEN_KEY);
        String refreshToken = getLocalStorageItem(driver, REFRESH_TOKEN_KEY);
        if (accessToken != null && !accessToken.isEmpty())
            throw new AssertionError("В LocalStorage не должно быть accessToken, получено: " + accessToken);
        if (refreshToken != null && !refreshToken.isEmpty())
            throw new AssertionError("В LocalStorage не должно быть refreshToken, получено: " + refreshToken);
    }

    @Step("Проверить, что в LocalStorage есть accessToken и refreshToken в формате Bearer <JWT>")
    public static void verifyTokensInLocalStorage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(webDriver -> {
            String at = getLocalStorageItem(webDriver, ACCESS_TOKEN_KEY);
            String rt = getLocalStorageItem(webDriver, REFRESH_TOKEN_KEY);
            return at != null && !at.isEmpty() && rt != null && !rt.isEmpty();
        });
        String accessToken = getLocalStorageItem(driver, ACCESS_TOKEN_KEY);
        String refreshToken = getLocalStorageItem(driver, REFRESH_TOKEN_KEY);
        if (accessToken == null || accessToken.isEmpty())
            throw new AssertionError("В LocalStorage ожидался accessToken");
        if (refreshToken == null || refreshToken.isEmpty())
            throw new AssertionError("В LocalStorage ожидался refreshToken");
        if (!accessToken.startsWith(BEARER_PREFIX))
            throw new AssertionError("accessToken должен начинаться с 'Bearer ', получено: " + accessToken);
        if (!accessToken.matches(JWT_PATTERN))
            throw new AssertionError("accessToken должен быть в формате Bearer <JWT> (Bearer eyJ...), получено: " + accessToken);
    }
}