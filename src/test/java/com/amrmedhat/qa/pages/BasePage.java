package com.amrmedhat.qa.pages;

import com.amrmedhat.qa.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Shared helpers for all page objects. Every interaction waits explicitly for the element —
 * no Thread.sleep and no implicit waits.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.explicitWait());
    }

    protected void open(String path) {
        driver.get(Config.baseUrl() + path);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Waits until the browser reports the document as fully loaded. */
    protected void waitForPageLoad() {
        wait.until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    /**
     * Clicks a link/button that must navigate to a URL containing {@code urlFragment}.
     * A click that lands while the page is still settling can be swallowed by the browser,
     * so the click is retried once if navigation did not start — instead of a blind sleep.
     */
    protected void clickAndWaitForUrl(By locator, String urlFragment) {
        waitForPageLoad();
        for (int attempt = 1; attempt <= 2; attempt++) {
            click(locator);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains(urlFragment));
                return;
            } catch (TimeoutException e) {
                if (attempt == 2) throw e;
            }
        }
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    public String currentPath() {
        return driver.getCurrentUrl().replace(Config.baseUrl(), "");
    }
}
