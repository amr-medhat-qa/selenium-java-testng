package com.amrmedhat.qa.tests;

import com.amrmedhat.qa.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import com.amrmedhat.qa.pages.AlertsPage;
import org.openqa.selenium.Alert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("the-internet.herokuapp.com")
@Feature("JavaScript alerts")
public class AlertsTest extends BaseTest {

    @Test(groups = "smoke", description = "Simple alert shows its message and is accepted")
    public void acceptSimpleAlert() {
        AlertsPage page = new AlertsPage(driver()).open();

        Alert alert = page.openAlert();
        assertEquals(alert.getText(), "I am a JS Alert");
        alert.accept();

        assertEquals(page.result(), "You successfully clicked an alert");
    }

    @Test(description = "Confirm dialog: OK is reported")
    public void acceptConfirm() {
        AlertsPage page = new AlertsPage(driver()).open();

        Alert confirm = page.openConfirm();
        assertEquals(confirm.getText(), "I am a JS Confirm");
        confirm.accept();

        assertEquals(page.result(), "You clicked: Ok");
    }

    @Test(description = "Confirm dialog: Cancel is reported")
    public void dismissConfirm() {
        AlertsPage page = new AlertsPage(driver()).open();

        page.openConfirm().dismiss();

        assertEquals(page.result(), "You clicked: Cancel");
    }

    @Test(description = "Prompt dialog: typed text is returned to the page")
    public void typeIntoPrompt() {
        AlertsPage page = new AlertsPage(driver()).open();
        String input = "Selenium " + System.currentTimeMillis();

        Alert prompt = page.openPrompt();
        assertEquals(prompt.getText(), "I am a JS prompt");
        prompt.sendKeys(input);
        prompt.accept();

        assertEquals(page.result(), "You entered: " + input);
    }

    @Test(description = "Prompt dialog: dismissing returns null")
    public void dismissPrompt() {
        AlertsPage page = new AlertsPage(driver()).open();

        page.openPrompt().dismiss();

        assertEquals(page.result(), "You entered: null");
    }
}
