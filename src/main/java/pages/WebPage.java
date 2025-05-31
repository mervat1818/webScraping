package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WebPage {
    private static final By BODY_TEXT = By.cssSelector("body");
    private static final By HEADINGS = By.cssSelector("h1, h2, h3, h4, h5, h6, [role='heading']");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public WebPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getFirstHeading() {
        try {
            List<WebElement> headings = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(HEADINGS));
            for (WebElement heading : headings) {
                String text = heading.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not find heading: " + e.getMessage());
        }
        return "No heading found";
    }

    public int getWordCount() {
        try {
            WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(BODY_TEXT));
            String text = body.getText().replaceAll("[^\\p{L}\\p{Nd}\\s]", "").trim();
            return text.isEmpty() ? 0 : text.split("\\s+").length;
        } catch (Exception e) {
            System.err.println("Could not calculate word count: " + e.getMessage());
            return 0;
        }
    }
}