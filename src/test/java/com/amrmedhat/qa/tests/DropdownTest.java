package com.amrmedhat.qa.tests;

import com.amrmedhat.qa.base.BaseTest;
import com.amrmedhat.qa.pages.DropdownPage;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class DropdownTest extends BaseTest {

    @Test(description = "Default state shows the placeholder and offers two options")
    public void defaultState() {
        DropdownPage page = new DropdownPage(driver()).open();

        assertEquals(page.selectedOption(), "Please select an option");
        assertEquals(page.selectableOptions(), List.of("Option 1", "Option 2"));
        assertFalse(page.isMultiple(), "dropdown is single-select");
    }

    @Test(groups = "smoke", description = "Select by visible text")
    public void selectByVisibleText() {
        DropdownPage page = new DropdownPage(driver()).open().selectByText("Option 2");
        assertEquals(page.selectedOption(), "Option 2");
    }

    @Test(description = "Select by value attribute and change the selection")
    public void selectByValueAndChange() {
        DropdownPage page = new DropdownPage(driver()).open();

        page.selectByValue("1");
        assertEquals(page.selectedOption(), "Option 1");

        page.selectByValue("2");
        assertEquals(page.selectedOption(), "Option 2");
    }
}
