package browser;
import org.ini4j.Ini;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BrowserFactory {

    private static final String CONFIG_FILE = "src/config.ini";
    private static final String YANDEX_BROWSER_PATH = "C:\\Program Files\\Yandex\\YandexBrowser\\Application\\browser.exe";
    private static final String YANDEX_DRIVER_PATH = "src/main/java/browser/yandexdriver.exe";

    public static BaseBrowser createBrowser() {
        try {
            Ini ini = new Ini(new File(CONFIG_FILE));
            List<String> browserOptions = new ArrayList<>();
            String browserType = System.getProperty("browser", ini.get("browser", "type"));
            String baseURL = ini.get("environment", "url");
            browserOptions.add(ini.get("browser", "sandbox"));
            browserOptions.add(ini.get("browser", "headless"));
            browserOptions.add(ini.get("browser", "sharedMem"));

            switch (browserType.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments(browserOptions);
                    return new ChromeBrowser(chromeOptions, baseURL);

                case "yandex":
                    ChromeOptions yandexOptions = new ChromeOptions();
                    yandexOptions.addArguments(browserOptions);
                    yandexOptions.setBinary(YANDEX_BROWSER_PATH);
                    ChromeDriverService yandexService = new ChromeDriverService.Builder()
                            .usingDriverExecutable(new File(YANDEX_DRIVER_PATH))
                            .build();
                    ChromeDriver yandexDriver = new ChromeDriver(yandexService, yandexOptions);
                    return new YandexBrowser(yandexDriver, baseURL);

                default:
                    throw new IllegalArgumentException(
                            "Тип браузера не поддерживается: " + browserType
                    );
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать конфигурационный файл: " + CONFIG_FILE, e);
        }
    }
}