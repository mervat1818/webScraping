package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GoogleHomePage {
    private static final By SEARCH_INPUT_FIELD = By.id("APjFqb");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo() {
        driver.get("https://www.google.com");
        wait.until(ExpectedConditions.titleContains("Google"));
    }

    public void searchFor(String keyword) throws InterruptedException {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT_FIELD));
        Thread.sleep(2000);
        searchBox.clear();
        Thread.sleep(2000);

        searchBox.sendKeys(keyword);
        Thread.sleep(2000);
        searchBox.submit();
    }

}