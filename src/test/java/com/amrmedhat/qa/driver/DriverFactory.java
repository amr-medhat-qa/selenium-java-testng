package com.amrmedhat.qa.driver;

import com.amrmedhat.qa.config.Config;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

/**
 * Creates WebDriver instances. Drivers are resolved automatically by Selenium Manager,
 * so no driver binaries or WebDriverManager are needed.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create(String browser, boolean headless) {
        WebDriver driver = switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new");
                options.addArguments("--disable-search-engine-choice-screen", "--no-sandbox", "--disable-dev-shm-usage");
                disablePasswordPopups(options);
                yield new ChromeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless=new");
                disablePasswordPopups(options);
                yield new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
        driver.manage().window().setSize(new Dimension(1366, 900));
        return driver;
    }

    /**
     * Chromium's password manager shows "save password" / "password found in a data breach" dialogs
     * after a login. They sit on top of the page and swallow clicks (seen in CI: Logout click ignored
     * on Chrome only), so they are switched off for test browsers.
     */
    private static void disablePasswordPopups(ChromiumOptions<?> options) {
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck");
    }

    public static WebDriver create() {
        return create(Config.browser(), Config.headless());
    }
}
