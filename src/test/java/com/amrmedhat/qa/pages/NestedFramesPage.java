package com.amrmedhat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page made of nested frames:
 * frame-top → (frame-left, frame-middle, frame-right) and frame-bottom.
 * Every read switches into the right frame and always returns to the main document afterwards.
 */
public class NestedFramesPage extends BasePage {

    private static final By BODY = By.tagName("body");

    public NestedFramesPage(WebDriver driver) {
        super(driver);
    }

    public NestedFramesPage open() {
        open("/nested_frames");
        return this;
    }

    /** Reads the text of a frame inside "frame-top", e.g. "frame-left" or "frame-middle". */
    public String topFrameText(String innerFrameName) {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(innerFrameName));
            return text(BODY);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    public String bottomFrameText() {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-bottom"));
            return text(BODY);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    public int topLevelFrameCount() {
        return driver.findElements(By.tagName("frame")).size();
    }
}
