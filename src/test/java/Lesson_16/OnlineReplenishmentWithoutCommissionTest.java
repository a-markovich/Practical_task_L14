package Lesson_16;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlineReplenishmentWithoutCommissionTest {
    WebDriver driver;
    String phone = "297777777";
    String sum = "10.50";

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get("https://www.mts.by");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("cookie-agree"))).click();
    }

    boolean checkPlaceholder(String idInputElem, String fieldInscription) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (boolean) js.executeScript(
                "return document.getElementById('" + idInputElem + "').placeholder === '" + fieldInscription + "'"
        );
    }

    @DisplayName("Проверить надписи в незаполненных полях каждого варианта оплаты услуг")
    @Test
    void checkEntriesInEmptyFieldsTest() {
        assertAll(
                () -> assertTrue(checkPlaceholder("connection-phone", "Номер телефона")),
                () -> assertTrue(checkPlaceholder("connection-sum", "Сумма")),
                () -> assertTrue(checkPlaceholder("connection-email", "E-mail для отправки чека")),
                () -> assertTrue(checkPlaceholder("internet-phone", "Номер абонента")),
                () -> assertTrue(checkPlaceholder("internet-sum", "Сумма")),
                () -> assertTrue(checkPlaceholder("internet-email", "E-mail для отправки чека")),
                () -> assertTrue(checkPlaceholder("score-instalment", "Номер счета на 44")),
                () -> assertTrue(checkPlaceholder("instalment-sum", "Сумма")),
                () -> assertTrue(checkPlaceholder("instalment-email", "E-mail для отправки чека")),
                () -> assertTrue(checkPlaceholder("score-arrears", "Номер счета на 2073")),
                () -> assertTrue(checkPlaceholder("arrears-sum", "Сумма")),
                () -> assertTrue(checkPlaceholder("arrears-email", "E-mail для отправки чека"))
        );
    }

    void openPaymentWindow(String phone, String sum) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement selectHeader = driver.findElement(By.xpath("//button[@class='select__header']"));
        WebElement selectOption = driver
                .findElement(By.xpath("//p[@class='select__option'][text()='Услуги связи']"));
        WebElement inputPhoneElem = driver.findElement(By.id("connection-phone"));
        WebElement inputSumElem = driver.findElement(By.id("connection-sum"));
        WebElement btn = driver.findElement(By.xpath("//form[@id='pay-connection']/button"));

        selectHeader.click();
        selectOption.click();
        inputPhoneElem.click();
        inputPhoneElem.sendKeys(phone, Keys.ENTER);
        inputSumElem.sendKeys(sum);
        btn.click();
        wait.until(ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[@class='bepaid-iframe']")));
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@class='payment-page__container']")));
    }

    @DisplayName("Проверить корректность отображения суммы")
    @Test
    void checkSumTest() {
        openPaymentWindow(phone, sum);
        WebElement actualSum = driver.findElement(By.xpath("//span[contains(text(),'BYN')]"));
        WebElement actualSumBtn = driver.findElement(By.xpath("//button[@class='colored disabled']"));
        assertAll(
                () -> assertTrue(actualSum.getText().contains(sum)),
                () -> assertTrue(actualSumBtn.getText().contains(sum))
        );
    }

    @DisplayName("Проверить корректность отображения номера телефона")
    @Test
    void checkPhoneTest() {
        openPaymentWindow(phone, sum);
        WebElement actualPhone = driver.findElement(By.xpath("//span[@class='pay-description__text']"));
        assertTrue(actualPhone.getText().contains(phone));
    }

    String getText(String nameElem) {
        return driver.findElement(By.xpath(
                "//div[input[@formcontrolname='" + nameElem + "']]/label")).getText();
    }

    @DisplayName("Проверить надписи в незаполненных полях для ввода реквизитов карты")
    @Test
    void checkLabelsTest() {
        openPaymentWindow(phone, sum);
        assertAll(
                () -> assertEquals("Номер карты", getText("creditCard")),
                () -> assertEquals("Срок действия", getText("expirationDate")),
                () -> assertEquals("CVC", getText("cvc")),
                () -> assertEquals("Имя держателя (как на карте)", getText("holder"))
        );
    }

    WebElement getWebElement(String imgName) {
        return (new WebDriverWait(driver, Duration.ofSeconds(5))).until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//img[contains(@src, '" + imgName + ".svg')]")));
    }

    @DisplayName("Проверить наличие иконок платёжных систем")
    @Test
    void checkIconsTest() {
        openPaymentWindow(phone, sum);
        assertAll(
                () -> assertTrue(getWebElement("mastercard-system").isDisplayed()),
                () -> assertTrue(getWebElement("visa-system").isDisplayed()),
                () -> assertTrue(getWebElement("belkart-system").isDisplayed()),
                () -> assertTrue(getWebElement("mir-system-ru").isDisplayed()),
                () -> assertTrue(getWebElement("maestro-system").isDisplayed())
        );
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }
}
