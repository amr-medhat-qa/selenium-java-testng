package com.amrmedhat.qa.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By FLASH = By.id("flash");

    /** How long to wait for the site's answer before deciding the session was lost. */
    private static final Duration SETTLE_TIME = Duration.ofSeconds(5);

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        open("/login");
        visible(LOGIN_BUTTON);
        return this;
    }

    /** Successful login lands on the secure area. */
    public SecureAreaPage loginAs(String username, String password) {
        submit(username, password);
        if (!arrivesWithin(ExpectedConditions.urlContains("/secure"))) {
            retryAfterLostSession("login was accepted (POST 303) but /secure answered 'not logged in'");
            submit(username, password);
        }
        return new SecureAreaPage(driver);
    }

    /** Failed login stays on this page with an error message. */
    public LoginPage loginExpectingError(String username, String password) {
        submit(username, password);
        if (!arrivesWithin(ExpectedConditions.visibilityOfElementLocated(FLASH))) {
            retryAfterLostSession("the error message (stored in the session) never appeared");
            submit(username, password);
        }
        return this;
    }

    public String flashMessage() {
        // the flash text ends with a "×" close icon — keep only the message
        return text(FLASH).replace("×", "").trim();
    }

    private void submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    /**
     * the-internet.herokuapp.com occasionally loses the session right after a login (about 1 in 60
     * attempts; captured in the Chrome network log: POST /authenticate → 303, GET /secure → 302 → /login).
     * Flash messages live in that same session, so they disappear too. It is a server-side quirk, not a
     * test bug, so the login is repeated ONCE — and the retry is visible as a step in the report.
     * A real defect still fails: the second attempt is not forgiven.
     */
    private void retryAfterLostSession(String symptom) {
        Allure.step("Site lost the session (known the-internet quirk): " + symptom + " - retrying login once");
        visible(LOGIN_BUTTON);
    }

    private boolean arrivesWithin(ExpectedCondition<?> condition) {
        try {
            new WebDriverWait(driver, SETTLE_TIME).until(condition);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
