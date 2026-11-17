package com.amrmedhat.qa.tests;

import com.amrmedhat.qa.base.BaseTest;
import com.amrmedhat.qa.pages.LoginPage;
import com.amrmedhat.qa.pages.SecureAreaPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.amrmedhat.qa.data.Credentials.VALID_PASSWORD;
import static com.amrmedhat.qa.data.Credentials.VALID_USERNAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    @Test(groups = "smoke", description = "Valid credentials open the secure area")
    public void validLoginOpensSecureArea() {
        SecureAreaPage secureArea = new LoginPage(driver()).open().loginAs(VALID_USERNAME, VALID_PASSWORD);

        assertEquals(secureArea.currentPath(), "/secure");
        assertEquals(secureArea.heading(), "Secure Area");
        assertTrue(secureArea.flashMessage().contains("You logged into a secure area!"));
    }

    @Test(groups = "smoke", description = "Logout returns to the login page with a confirmation")
    public void logoutReturnsToLoginPage() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginAs(VALID_USERNAME, VALID_PASSWORD)
                .logout();

        assertEquals(loginPage.currentPath(), "/login");
        assertTrue(loginPage.flashMessage().contains("You logged out of the secure area!"));
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][]{
                {"unknown user", "wrong-user", VALID_PASSWORD, "Your username is invalid!"},
                {"wrong password", VALID_USERNAME, "wrong-password", "Your password is invalid!"},
                {"empty username", "", VALID_PASSWORD, "Your username is invalid!"},
                {"empty password", VALID_USERNAME, "", "Your password is invalid!"},
                {"case-sensitive username", VALID_USERNAME.toUpperCase(), VALID_PASSWORD, "Your username is invalid!"},
        };
    }

    @Test(dataProvider = "invalidLogins", description = "Invalid credentials show a specific error and stay on login")
    public void invalidLoginShowsError(String scenario, String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(driver()).open().loginExpectingError(username, password);

        assertTrue(loginPage.flashMessage().contains(expectedError),
                scenario + ": expected '" + expectedError + "' but was '" + loginPage.flashMessage() + "'");
        assertEquals(loginPage.currentPath(), "/login", scenario + ": must stay on the login page");
    }

    @Test(description = "Secure area is protected when not logged in")
    public void secureAreaRequiresLogin() {
        driver().get(com.amrmedhat.qa.config.Config.baseUrl() + "/secure");
        LoginPage loginPage = new LoginPage(driver());

        assertEquals(loginPage.currentPath(), "/login");
        assertTrue(loginPage.flashMessage().contains("You must login to view the secure area!"));
    }
}
