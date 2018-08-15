package Gauge;

import com.thoughtworks.gauge.ContinueOnFailure;
import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.WebElementDataStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation {

    @ContinueOnFailure
    @Step("Access NewBee Website")
    public void OpenNewBee() {
        String app_url = System.getenv("APP_URL");
        Driver.webDriver.manage().deleteAllCookies();
        Driver.webDriver.manage().window().maximize();
        Driver.webDriver.get(app_url);
        assertThat(Driver.webDriver.findElement(By.className("welcome-page-title")).getText()).contains("NewBee Portal");
    }

    @Step("Enter UserID <UserId>")
    public void EnterUserID(String email) {
        WebElementDataStore.getWebElementByUniqueId("loginUserName").sendKeys(email);
    }

    @Step("Enter Password <Password>")
    public void EnterPassword(String password) {
        WebElementDataStore.getWebElementByUniqueId("loginPassword").sendKeys(password);
    }

    @Step("Click Login Button")
    public void ClickLoginButton() {
        WebElementDataStore.getWebElementByUniqueId("loginButton").click();
    }
    @ContinueOnFailure
    @Step("Login as SuperUser")
    public void LoginSuperUser() {
        String userName = System.getenv("USER_NAME");
        String password = System.getenv("PASSWORD");
        EnterUserID(userName);
        EnterPassword(password);
        ClickLoginButton();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(Driver.webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[2]/div[2]")).getText().contains("Hello, SuperUser SuperUser"));

    }
    @ContinueOnFailure
    @Step("Add HR Director as Reviewer <FirstName> <LastName> <Email> <AdminLevel> <Hive> <Job Position>")
    public void AddReviewer(String firstName, String lastName, String email, String adminLevel, String hive, String jobPosition) {

        // Click Manage Reviewer --> "Add Reviewer"

        Driver.webDriver.findElement(By.cssSelector("#main-app-area > div > div > div.body-admin > div > div.menu-staff > div.menu-staff-left > div:nth-child(3)")).click();
        assertThat(Driver.webDriver.findElement(By.xpath("//*[@id=\"add-a-new-reviewer\"]/div/div[1]")).getText().contains("Add a new reviewer"));

        // Assert Reviewer Details Page & Enter details

        Driver.webDriver.findElement(By.xpath("//*[@id=\"add-a-new-reviewer\"]/div/div[1]")).click();
        assertThat(Driver.webDriver.findElement(By.xpath("//*[@id=\"main-app-area\"]/div/div/div[1]/div/div[2]/div/div[1]/div[2]")).getText().contains("REVIEWER DETAILS"));

        Driver.webDriver.findElement(By.id("reviewer-first-name")).sendKeys(firstName);
        Driver.webDriver.findElement(By.id("reviewer-surname")).sendKeys(lastName);
        Driver.webDriver.findElement(By.id("reviewer-email")).sendKeys(email);

        Select AdminLevel = new Select(Driver.webDriver.findElement(By.xpath("//*[@id=\"main-app-area\"]/div/div/div[1]/div/div[2]/div/div[2]/div[2]/div[3]/select")));
        AdminLevel.selectByVisibleText(adminLevel);

        Select Hive = new Select(Driver.webDriver.findElement(By.xpath("//*[@id=\"main-app-area\"]/div/div/div[1]/div/div[2]/div/div[2]/div[3]/div[1]/select")));
        Hive.selectByVisibleText(hive);

        Select JobPosition = new Select(Driver.webDriver.findElement(By.xpath("//*[@id=\"main-app-area\"]/div/div/div[1]/div/div[2]/div/div[2]/div[3]/div[3]/select")));
        JobPosition.selectByVisibleText(jobPosition);

        String status = Driver.webDriver.findElement(By.className("new-reviewer-send-button")).getAttribute("disabled");
        System.out.println(status);
        assertThat(status).isNull();


        Driver.webDriver.findElement(By.xpath("//*[@id=\"main-app-area\"]/div/div/div[1]/div/div[2]/div/div[2]/div[4]/div")).click();


        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Verify Reviewer in "Manage Reviewers"

        String tableValue = Driver.webDriver.findElement(By.xpath("//*[@id=\"add-a-new-reviewer\"]/div/div[5]/div/div")).getText();

        List<String> tableListValues = Arrays.asList(tableValue.split("\n"));
        String checkPoint = firstName + " " + lastName;

        int occurrences = Collections.frequency(tableListValues,checkPoint);

        assertThat(occurrences).isEqualTo(1);

        int i = tableListValues.indexOf(checkPoint);

        assertThat(i).isGreaterThanOrEqualTo(0);
        assertThat(checkPoint).isEqualToIgnoringCase(tableListValues.get(i).trim());
        assertThat(jobPosition).isEqualToIgnoringCase(tableListValues.get(++i).trim());
        assertThat(hive).isEqualToIgnoringCase(tableListValues.get(++i).trim());
        assertThat(adminLevel).isEqualToIgnoringCase(tableListValues.get(++i).trim());

    }
}
