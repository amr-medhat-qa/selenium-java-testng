package com.amrmedhat.qa.base;

import com.amrmedhat.qa.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * One browser per test method, kept in a ThreadLocal so tests can run in parallel safely.
 * A screenshot is saved to target/screenshots when a test fails.
 */
public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    protected WebDriver driver() {
        return DRIVER.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
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
            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);
            String name = result.getTestClass().getRealClass().getSimpleName() + "_" + result.getMethod().getMethodName();
            Files.write(dir.resolve(name + ".png"), png);
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
