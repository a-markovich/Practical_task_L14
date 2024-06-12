import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MtsTest {
    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get("https://www.mts.by");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("cookie-agree"))).click();
    }

    @Test
    void CheckBlockNameTest() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .textToBe(By.xpath("//div[@class='pay__wrapper']/h2"),
                                "Онлайн пополнение\nбез комиссии"));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {
            "visa",
            "visa-verified",
            "mastercard",
            "mastercard-secure",
            "belkart"
    })
    void CheckForPaymentSystemLogosTest(String request) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//div[@class='pay__wrapper']" +
                                "/.//img[contains(@src, '" + request + ".svg')]")));
    }

    @Test
    void CheckLinkWorksTest() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//a[text()='Подробнее о сервисе']"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.
                urlToBe("https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/"));
    }

    @Test
    void checkButtonOperationTest() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//button[@class='select__header']"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//p[@class='select__option']" +
                                "[text()='Услуги связи']"))).click();
        WebElement inputPhoneElem = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("connection-phone")));
        inputPhoneElem.click();
        inputPhoneElem.sendKeys("297777777");
        WebElement inputSumElem = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("connection-sum")));
        inputSumElem.click();
        inputSumElem.sendKeys("10");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//div[@class='pay__forms']" +
                                "/.//button"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions
                        .frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[@class='bepaid-iframe']")));
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }
}
