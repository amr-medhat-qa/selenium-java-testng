package com.amrmedhat.qa.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Native JavaScript dialogs: alert, confirm and prompt. */
public class AlertsPage extends BasePage {

    private static final By JS_ALERT = By.cssSelector("button[onclick='jsAlert()']");
    private static final By JS_CONFIRM = By.cssSelector("button[onclick='jsConfirm()']");
    private static final By JS_PROMPT = By.cssSelector("button[onclick='jsPrompt()']");
    private static final By RESULT = By.id("result");

    public AlertsPage(WebDriver driver) {
        super(driver);
    }

    public AlertsPage open() {
        open("/javascript_alerts");
        visible(JS_ALERT);
        return this;
    }

    public Alert openAlert() {
        click(JS_ALERT);
        return waitForAlert();
    }

    public Alert openConfirm() {
        click(JS_CONFIRM);
        return waitForAlert();
    }

    public Alert openPrompt() {
        click(JS_PROMPT);
        return waitForAlert();
    }

    public String result() {
        return text(RESULT);
    }

    private Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}
