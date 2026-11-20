package com.amrmedhat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Example 1: the result element is in the DOM but hidden until loading finishes.
 * Example 2: the result element does not exist at all until loading finishes.
 * Both are handled with explicit waits — the classic case where Thread.sleep makes tests slow and flaky.
 */
public class DynamicLoadingPage extends BasePage {

    private static final By START = By.cssSelector("#start button");
    private static final By LOADING = By.id("loading");
    private static final By FINISH_TEXT = By.cssSelector("#finish h4");

    public DynamicLoadingPage(WebDriver driver) {
        super(driver);
    }

    public DynamicLoadingPage open(int example) {
        open("/dynamic_loading/" + example);
        visible(START);
        return this;
    }

    public DynamicLoadingPage start() {
        click(START);
        return this;
    }

    /** Waits for the loading bar to disappear, then for the result to become visible. */
    public String waitForResult() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING));
        return text(FINISH_TEXT);
    }

    public boolean isResultInDom() {
        return !driver.findElements(FINISH_TEXT).isEmpty();
    }

    public boolean isResultDisplayed() {
        return driver.findElements(FINISH_TEXT).stream().anyMatch(e -> e.isDisplayed());
    }
}
