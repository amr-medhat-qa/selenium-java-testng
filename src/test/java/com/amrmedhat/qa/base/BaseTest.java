package com.amrmedhat.qa.base;

import com.amrmedhat.qa.config.Config;
import com.amrmedhat.qa.driver.DriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * One browser per test method, kept in a ThreadLocal so tests can run in parallel safely.
 * On failure a screenshot is saved to target/screenshots and attached to the Allure report.
 */
public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    protected WebDriver driver() {
        return DRIVER.get();
    }

    /** Shows browser, URL and Java version in the "Environment" widget of the Allure report. */
    @BeforeSuite(alwaysRun = true)
    public void writeAllureEnvironment() throws IOException {
        Path dir = Path.of(System.getProperty("allure.results.directory", "target/allure-results"));
        Files.createDirectories(dir);

        Properties env = new Properties();
        env.setProperty("Browser", Config.browser() + (Config.headless() ? " (headless)" : ""));
        env.setProperty("Base.URL", Config.baseUrl());
        env.setProperty("Java", System.getProperty("java.version"));
        env.setProperty("OS", System.getProperty("os.name"));
        try (var out = Files.newOutputStream(dir.resolve("environment.properties"))) {
            env.store(out, null);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        // Without this, Allure sees "the same test" on Chrome and Firefox and merges them into one entry.
        // A parameter makes each browser a separate result (46 instead of 23) that can be filtered.
        Allure.parameter("browser", Config.browser());
        DRIVER.set(DriverFactory.create());
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser(ITestResult result) {
        WebDriver driver = DRIVER.get();
        if (driver == null) return;
        try {
            if (result.getStatus() == ITestResult.FAILURE) saveScreenshot(driver, result);
        } finally {
            driver.quit();
            DRIVER.remove();
        }
    }

    private void saveScreenshot(WebDriver driver, ITestResult result) {
        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.attachment("Screenshot on failure", "image/png", new ByteArrayInputStream(png),
                    AttachmentOptions.withFileExtension(".png"));

            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);
            String name = result.getTestClass().getRealClass().getSimpleName() + "_" + result.getMethod().getMethodName();
            Files.write(dir.resolve(name + ".png"), png);
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
