package com.amrmedhat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SecureAreaPage extends BasePage {

    private static final By HEADING = By.cssSelector("div.example h2");
    private static final By FLASH = By.id("flash");
    private static final By LOGOUT = By.cssSelector("a[href='/logout']");

    public SecureAreaPage(WebDriver driver) {
        super(driver);
        // the page object is only "ready" once navigation has finished — avoids racing the redirect
        wait.until(ExpectedConditions.urlContains("/secure"));
    }

    public String heading() {
        return text(HEADING);
    }

    public String flashMessage() {
        return text(FLASH).replace("×", "").trim();
    }

    public LoginPage logout() {
        click(LOGOUT);
        wait.until(ExpectedConditions.urlContains("/login"));
        return new LoginPage(driver);
    }
}
