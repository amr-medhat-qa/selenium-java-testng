package com.amrmedhat.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class DropdownPage extends BasePage {

    private static final By DROPDOWN = By.id("dropdown");

    public DropdownPage(WebDriver driver) {
        super(driver);
    }

    public DropdownPage open() {
        open("/dropdown");
        visible(DROPDOWN);
        return this;
    }

    private Select select() {
        return new Select(visible(DROPDOWN));
    }

    public DropdownPage selectByText(String text) {
        select().selectByVisibleText(text);
        return this;
    }

    public DropdownPage selectByValue(String value) {
        select().selectByValue(value);
        return this;
    }

    public String selectedOption() {
        return select().getFirstSelectedOption().getText().trim();
    }

    /** Options a user can actually pick (the disabled placeholder is excluded). */
    public List<String> selectableOptions() {
        return select().getOptions().stream()
                .filter(WebElement::isEnabled)
                .map(o -> o.getText().trim())
                .toList();
    }

    public boolean isMultiple() {
        return select().isMultiple();
    }
}
