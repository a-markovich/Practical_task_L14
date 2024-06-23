package Lesson_18;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlineReplenishmentTest {
    WebDriver driver;
    OnlineReplenishment onlineReplenishment;
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
        onlineReplenishment = new OnlineReplenishment(driver);
    }

    @DisplayName("Проверить надписи в незаполненных полях")
    @Description("Проверить placeholder полей каждого варианта оплаты услуг: " +
            "услуги связи, домашний интернет, рассрочка, задолженность")
    @Owner("Анастасия")
    @Test
    void checkEntriesInEmptyFieldsTest() {
        assertAll(
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("connection-phone", "Номер телефона")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("connection-sum", "Сумма")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("connection-email", "E-mail для отправки чека")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("internet-phone", "Номер абонента")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("internet-sum", "Сумма")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("internet-email", "E-mail для отправки чека")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("score-instalment", "Номер счета на 44")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("instalment-sum", "Сумма")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("instalment-email", "E-mail для отправки чека")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("score-arrears", "Номер счета на 2073")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("arrears-sum", "Сумма")),
                () -> assertTrue(onlineReplenishment
                        .checkPlaceholder("arrears-email", "E-mail для отправки чека"))
        );
    }

    @DisplayName("Проверить корректность отображения суммы")
    @Description("Для варианта «Услуги связи» заполнить поля, " +
            "нажать кнопку «Продолжить» и в появившемся окне проверить корректность отображения суммы " +
            "(в том числе на кнопке)")
    @Owner("Анастасия")
    @Test
    void checkSumTest() {
        onlineReplenishment.openPaymentWindow(phone, sum);
        WebElement actualSum = driver.findElement(By.xpath("//span[contains(text(),'BYN')]"));
        WebElement actualSumBtn = driver.findElement(By.xpath("//button[@class='colored disabled']"));
        assertAll(
                () -> assertTrue(actualSum.getText().contains(sum)),
                () -> assertTrue(actualSumBtn.getText().contains(sum))
        );
    }

    @DisplayName("Проверить корректность отображения номера телефона")
    @Description("Для варианта «Услуги связи» заполнить поля, " +
            "нажать кнопку «Продолжить» и в появившемся окне проверить корректность отображения номера телефона")
    @Owner("Анастасия")
    @Test
    void checkPhoneTest() {
        onlineReplenishment.openPaymentWindow(phone, sum);
        WebElement actualPhone = driver.findElement(By.xpath("//span[@class='pay-description__text']"));
        assertTrue(actualPhone.getText().contains(phone));
    }

    @DisplayName("Проверить надписи в незаполненных полях для ввода реквизитов карты")
    @Description("Для варианта «Услуги связи» заполнить поля, " +
            "нажать кнопку «Продолжить» и в появившемся окне проверить надписи в незаполненных полях " +
            "для ввода реквизитов карты")
    @Owner("Анастасия")
    @Test
    void checkLabelsTest() {
        onlineReplenishment.openPaymentWindow(phone, sum);
        assertAll(
                () -> assertEquals("Номер карты", onlineReplenishment.getText("creditCard")),
                () -> assertEquals("Срок действия", onlineReplenishment.getText("expirationDate")),
                () -> assertEquals("CVC", onlineReplenishment.getText("cvc")),
                () -> assertEquals("Имя держателя (как на карте)", onlineReplenishment.getText("holder"))
        );
    }

    @DisplayName("Проверить наличие иконок платёжных систем")
    @Description("Для варианта «Услуги связи» заполнить поля, " +
            "нажать кнопку «Продолжить» и в появившемся окне проверить наличие иконок платёжных систем")
    @Owner("Анастасия")
    @Test
    void checkIconsTest() {
        onlineReplenishment.openPaymentWindow(phone, sum);
        assertAll(
                () -> assertTrue(onlineReplenishment.getWebElement("mastercard-system").isDisplayed()),
                () -> assertTrue(onlineReplenishment.getWebElement("visa-system").isDisplayed()),
                () -> assertTrue(onlineReplenishment.getWebElement("belkart-system").isDisplayed()),
                () -> assertTrue(onlineReplenishment.getWebElement("mir-system-ru").isDisplayed()),
                () -> assertTrue(onlineReplenishment.getWebElement("maestro-system").isDisplayed())
        );
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }
}
