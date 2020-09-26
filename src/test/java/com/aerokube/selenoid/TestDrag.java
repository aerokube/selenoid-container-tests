package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.allure.annotations.Features;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestDrag extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.DRAG);
        waitUntilElementIsPresent(By.cssSelector("#custom-handle"));
    }

    @Features("Drag functionality")
    @Test
    public void testDrag() throws Exception {
        try {
            WebDriver driver = getDriver();
            WebElement button = driver.findElement(By.cssSelector("#custom-handle"));
            assertThat(button.getText(), equalTo("10"));

            Actions drag = new Actions(driver);
            drag
                    .clickAndHold(button)
                    .moveByOffset(100, 0)
                    .build()
            .perform();

            assertThat(button.getText(), equalTo("20"));
        } catch (Exception e) {
            fail("Drag functionality is not supported", e);
        }
    }
}
