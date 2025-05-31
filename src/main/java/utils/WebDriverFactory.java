package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class WebDriverFactory {
    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--headless");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setPageLoadTimeout(Duration.ofSeconds(30));
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions",
                "--disable-popup-blocking",
                "--disable-notifications");

        options.addArguments("--window-size=1920,1080");
        return new ChromeDriver(options);
    }

//    public static WebDriver createFirefoxDriver() {
//        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.addArguments("--incognito");
//        firefoxOptions.addArguments("-width=1920");
//        firefoxOptions.addArguments("-height=1080");
//        firefoxOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//
//        return new FirefoxDriver(firefoxOptions);
//    }

}