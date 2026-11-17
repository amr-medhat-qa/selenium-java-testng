package com.amrmedhat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By FLASH = By.id("flash");

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
        return new SecureAreaPage(driver);
    }

    /** Failed login stays on this page with an error message. */
    public LoginPage loginExpectingError(String username, String password) {
        submit(username, password);
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
}
