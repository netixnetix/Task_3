package browser;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeBrowser extends BaseBrowser {
    public ChromeBrowser(ChromeOptions options, String baseURL) {
        super(new ChromeDriver(options), baseURL);
    }
}