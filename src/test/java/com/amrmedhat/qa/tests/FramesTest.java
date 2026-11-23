package com.amrmedhat.qa.tests;

import com.amrmedhat.qa.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import com.amrmedhat.qa.pages.NestedFramesPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("the-internet.herokuapp.com")
@Feature("Frames")
public class FramesTest extends BaseTest {

    @DataProvider(name = "topFrames")
    public Object[][] topFrames() {
        return new Object[][]{
                {"frame-left", "LEFT"},
                {"frame-middle", "MIDDLE"},
                {"frame-right", "RIGHT"},
        };
    }

    @Test(dataProvider = "topFrames", description = "Each nested frame inside frame-top shows its own content")
    public void readNestedFrame(String frameName, String expectedText) {
        NestedFramesPage page = new NestedFramesPage(driver()).open();
        assertEquals(page.topFrameText(frameName), expectedText);
    }

    @Test(description = "Bottom frame is readable and focus returns to the main document")
    public void readBottomFrameAndReturnToMainDocument() {
        NestedFramesPage page = new NestedFramesPage(driver()).open();

        assertEquals(page.bottomFrameText(), "BOTTOM");
        // back in the main document: the two top-level frames are visible again
        assertEquals(page.topLevelFrameCount(), 2);
    }

    @Test(description = "Frames can be read one after another without leaking frame context")
    public void switchBetweenFrames() {
        NestedFramesPage page = new NestedFramesPage(driver()).open();

        assertEquals(page.topFrameText("frame-left"), "LEFT");
        assertEquals(page.bottomFrameText(), "BOTTOM");
        assertEquals(page.topFrameText("frame-right"), "RIGHT");
    }
}
