package utils;

import com.thoughtworks.gauge.datastore.DataStoreFactory;
import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;

public class WebElementDataStore {

    public static WebElement getWebElementByPageAndUniqueId(String page, String uniqueId){
        HashMap<String, HashMap<String, ArrayList<String>>> elementDefinitions =
                (HashMap<String, HashMap<String, ArrayList<String>>>) DataStoreFactory
                        .getSuiteDataStore()
                        .get("PageObjects");
        String findBy = elementDefinitions.get(page).get(uniqueId).get(0);
        String value = elementDefinitions.get(page).get(uniqueId).get(1);

        switch (findBy.trim().toLowerCase()) {
            case "id":
                return Driver.webDriver.findElement(By.id(value));
            case "class":
                return Driver.webDriver.findElement(By.className(value));
            case "xpath":
                return Driver.webDriver.findElement(By.xpath(value));
            case "cssselector":
                return Driver.webDriver.findElement(By.cssSelector(value));
            case "name":
                return Driver.webDriver.findElement(By.name(value));
            case "tagname":
                return Driver.webDriver.findElement(By.tagName(value));
            case "partiallinktext":
                return Driver.webDriver.findElement(By.partialLinkText(value));
            case "linktext":
                return Driver.webDriver.findElement(By.linkText(value));
            default:
                return Driver.webDriver.findElement(By.xpath(value));
        }
    }
}
