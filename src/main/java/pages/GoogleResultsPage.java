package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleResultsPage {

    private static final By allResults = By.cssSelector("div.g, div[class*='MjjYud'], div[data-snf]");
    private static final By linkInResults = By.cssSelector("a[href^='http']:not([role='button'])");

    private static final String[] irrelevantDomains = {
            "youtube.com",
            "wikipedia.org",
            "twitter.com",
            "facebook.com",
            "instagram.com",
            "pinterest.com",
            "npmjs.com",
            "testgrid.io"
    };
    private final WebDriver driver;
    private final WebDriverWait wait;

    public GoogleResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public List<String> getFirstFiveOrganicLinks() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));

        List<WebElement> allResults = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(GoogleResultsPage.allResults));
        Set<String> uniqueUrls = new HashSet<>();
        List<String> organicLinks = new ArrayList<>();
        int targetCount = 5;

        for (WebElement result : allResults) {
            if (organicLinks.size() >= targetCount) break;

            try {
                if (isAdOrSpecialResult(result)) continue;

                WebElement link = result.findElement(linkInResults);
                String href = link.getAttribute("href");

                if (href == null || href.isEmpty()) continue;

                String cleanUrl = cleanUrl(href);

                if (isValidResult(cleanUrl) && !uniqueUrls.contains(cleanUrl) && !isLinkBroken(cleanUrl)) {
                    uniqueUrls.add(cleanUrl);
                    organicLinks.add(cleanUrl);
                    System.out.println("Added link (" + organicLinks.size() + "): " + cleanUrl);
                }
            } catch (Exception e) {
                System.out.println(" Skipping one result element");
            }
        }

        if (organicLinks.size() < targetCount) {
            System.out.println("Only found " + organicLinks.size() + " organic results");
        }

        return organicLinks.subList(0, Math.min(organicLinks.size(), targetCount));
    }

    private String cleanUrl(String url) {
        // Normalize URL format
        url = url.split("\\?")[0].split("#")[0];
        url = url.replace("https://www.", "https://")
                .replace("http://www.", "http://");
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private boolean isAdOrSpecialResult(WebElement result) {
        try {
            String resultText = result.getText().toLowerCase();
            return result.findElements(By.xpath(".//span[contains(., 'Sponsored')]")).size() > 0
                    || result.findElements(By.cssSelector("[aria-label='Ad']")).size() > 0
                    || result.findElements(By.cssSelector("div[data-dtld]")).size() > 0
                    || result.findElements(By.cssSelector("div.ULSxyf")).size() > 0
                    || resultText.contains("404")
                    || resultText.contains("not found")
                    || resultText.contains("test environment");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidResult(String url) {

        for (String domain : irrelevantDomains) {
            if (url.contains(domain)) {
                System.out.println(" Excluded domain: " + url);
                return false;
            }
        }


        return !url.contains("google.")
                && url.contains("selenium")
                && !url.matches(".*\\.(pdf|docx|ppt)$")
                && url.length() < 100
                && !url.matches(".*(test|dummy|example).*");
    }

    private boolean isLinkBroken(String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println(" Broken link (404): " + url);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(" Could not verify link: " + url);
            return true;
        }
    }
}
