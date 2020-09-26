package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Features;
import com.aerokube.selenoid.misc.Page;

public class TestScreenshot extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.FIRST);
        waitUntilElementIsPresent(By.cssSelector("#test-id"));
    }

    @Features("Taking screenshots")
    @Test
    public void testScreenshot() throws Exception {
        WebDriver driver = getDriver();
        try {
            takeScreenshot(driver);
        } catch (Exception e) {
            fail("Screenshots are not supported", e);
        }
    }

    @Attachment("screenshot")
    private byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) new Augmenter().augment(driver))
                .getScreenshotAs(OutputType.BYTES);
    }

}
