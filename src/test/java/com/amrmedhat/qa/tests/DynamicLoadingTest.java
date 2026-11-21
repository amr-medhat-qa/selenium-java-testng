package com.amrmedhat.qa.tests;

import com.amrmedhat.qa.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import com.amrmedhat.qa.pages.DynamicLoadingPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Epic("the-internet.herokuapp.com")
@Feature("Dynamic loading")
public class DynamicLoadingTest extends BaseTest {

    @Test(groups = "smoke", description = "Hidden element becomes visible after loading")
    public void hiddenElementAppears() {
        DynamicLoadingPage page = new DynamicLoadingPage(driver()).open(1);

        assertTrue(page.isResultInDom(), "example 1: result exists in the DOM from the start");
        assertFalse(page.isResultDisplayed(), "example 1: …but it is hidden");

        assertEquals(page.start().waitForResult(), "Hello World!");
    }

    @Test(description = "Element rendered only after loading is found with an explicit wait")
    public void elementRenderedAfterLoading() {
        DynamicLoadingPage page = new DynamicLoadingPage(driver()).open(2);

        assertFalse(page.isResultInDom(), "example 2: result does not exist before loading");

        assertEquals(page.start().waitForResult(), "Hello World!");
    }
}
