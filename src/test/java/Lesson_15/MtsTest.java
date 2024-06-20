package Lesson_15;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.findElement(By.id("cookie-agree")).click();
    }

    @Test
    void checkBlockNameTest() {
        String expected = "Онлайн пополнение\nбез комиссии";
        String actual = driver.findElement(By.xpath("//div[@class='pay__wrapper']/h2")).getText();
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {
            "visa",
            "visa-verified",
            "mastercard",
            "mastercard-secure",
            "belkart"
    })
    void checkForPaymentSystemLogosTest(String request) {
        WebElement logo = driver.findElement(By.xpath("//div[@class='pay__wrapper']" +
                "/.//img[contains(@src, '" + request + ".svg')]"));
        assertTrue(logo.isDisplayed());
    }

    @Test
    void checkLinkWorksTest() {
        String expected = "https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/";
        driver.findElement(By.xpath("//a[text()='Подробнее о сервисе']")).click();
        assertEquals(expected, driver.getCurrentUrl());
    }

    @Test
    void checkButtonOperationTest() {
        driver.findElement(By.xpath("//button[@class='select__header']")).click();
        driver.findElement(By.xpath("//p[@class='select__option']")).click();
        WebElement inputPhoneElem = driver.findElement(By.id("connection-phone"));
        inputPhoneElem.click();
        inputPhoneElem.sendKeys("297777777");
        WebElement inputSumElem = driver.findElement(By.id("connection-sum"));
        inputSumElem.click();
        inputSumElem.sendKeys("10");
        driver.findElement(By.xpath("//div[@class='pay__forms']/.//button")).click();
        WebElement iframeElem = (new WebDriverWait(driver, Duration.ofSeconds(20)))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//iframe[@class='bepaid-iframe']")));
        assertTrue(iframeElem.isDisplayed());
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }
}
