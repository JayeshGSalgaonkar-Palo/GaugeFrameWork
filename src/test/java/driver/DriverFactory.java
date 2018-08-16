package driver;

import com.thoughtworks.gauge.datastore.DataStoreFactory;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static utils.TxtUtils.populateElementDefinitions;

class DriverFactory {

    // Get a new WebDriver Instance.
    // There are various implementations for this depending on browser. The required browser can be set as an environment variable.
    // Refer http://getgauge.io/documentation/user/current/managing_environments/README.html
    static WebDriver getDriver() {

        String browser = System.getenv("BROWSER");
        browser = (browser == null) ? "CHROME": browser;

        try {
            DataStoreFactory.getSuiteDataStore().put("PageObjects", populateElementDefinitions());
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (browser) {
            case "IE":
                InternetExplorerDriverManager.getInstance().setup();
                return new InternetExplorerDriver();
            case "FIREFOX":
                FirefoxDriverManager.getInstance().setup();
                return new FirefoxDriver();
            case "CHROME":
            default:
	            ChromeDriverManager.getInstance().setup();
	
	            ChromeOptions options = new ChromeOptions();
	            if ("Y".equalsIgnoreCase(System.getenv("HEADLESS"))) {
	                options.addArguments("--headless");
	                options.addArguments("--disable-gpu");
	            }
	
	            return new ChromeDriver(options);
        }
    }
}
