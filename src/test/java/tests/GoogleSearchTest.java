package tests;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.GoogleHomePage;
import pages.GoogleResultsPage;
import pages.WebPage;
import utils.FileUtils;
import utils.WebDriverFactory;

import java.util.List;

public class GoogleSearchTest {
    private WebDriver driver;
    private JSONArray searchResults;

    @BeforeMethod
    public void setup() {
        driver = WebDriverFactory.createChromeDriver();
        searchResults = new JSONArray();
    }

    @Test
    public void verifySearchResultsAndExtractData() throws InterruptedException {
        GoogleHomePage homePage = new GoogleHomePage(driver);
        homePage.navigateTo();
        homePage.searchFor("Selenium WebDriver");

        GoogleResultsPage resultsPage = new GoogleResultsPage(driver);
        List<String> organicLinks = resultsPage.getFirstFiveOrganicLinks();

        for (String link : organicLinks) {
            try {
                driver.get(link);
                WebPage targetPage = new WebPage(driver);

                JSONObject pageInfo = new JSONObject();
                pageInfo.put("title", targetPage.getTitle())
                        .put("url", targetPage.getCurrentUrl())
                        .put("heading", targetPage.getFirstHeading())
                        .put("wordCount", targetPage.getWordCount());

                searchResults.put(pageInfo);
            } catch (Exception e) {
                System.err.println("Failed to process page: " + link + " - " + e.getMessage());
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            if (searchResults.length() > 0) {
                FileUtils.saveAsJson(searchResults, "search_results.json");
            } else {
                System.err.println("No results to save");
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
