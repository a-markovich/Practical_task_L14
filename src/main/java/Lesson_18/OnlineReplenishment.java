package Lesson_18;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OnlineReplenishment {
    private WebDriver driver;

    public OnlineReplenishment(WebDriver driver) {
        this.driver = driver;
    }

    public boolean checkPlaceholder(String idInputElem, String fieldInscription) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (boolean) js.executeScript(
                "return document.getElementById('" + idInputElem + "').placeholder === '" + fieldInscription + "'"
        );
    }

    public void openPaymentWindow(String phone, String sum) {
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

    public String getText(String nameElem) {
        return driver.findElement(By.xpath(
                "//div[input[@formcontrolname='" + nameElem + "']]/label")).getText();
    }

    public WebElement getWebElement(String imgName) {
        return (new WebDriverWait(driver, Duration.ofSeconds(5))).until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//img[contains(@src, '" + imgName + ".svg')]")));
    }
}
