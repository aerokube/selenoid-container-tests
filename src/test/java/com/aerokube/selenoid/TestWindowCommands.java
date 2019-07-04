package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.*;
import ru.yandex.qatools.allure.annotations.Features;

import java.util.List;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@Features("Miscellaneous window commands")
public class TestWindowCommands extends TestBase {
    
    @Before
    public void before() throws Exception {
        openPage(Page.FIRST);
        waitUntilElementIsPresent(By.id("test-id"));
    }
    
    @Features("Closing window")
    @Test
    public void testWindowCloseIsSupported() throws Exception {
        try {
            WebDriver driver = getDriver();
            WebElement link = driver.findElement(By.id("test-link")); //This link opens new window
            link.click();
            await().until(() -> getDriver().getWindowHandles().size() == 2);
            getDriver().close();
            await().until(() -> getDriver().getWindowHandles().size() == 1);
        } catch (Exception e) {
            fail("WebDriver.close() is not supported", e);
        }
    }
    
    @Features("Switching between windows")
    @Test
    public void testSwitchWindows() throws Exception {
        try {
            WebDriver driver = getDriver();
            WebElement link = driver.findElement(By.id("test-link")); //This link opens new window
            link.click();
            await().until(() -> getDriver().getWindowHandles().size() == 2);
            List<String> windowNames = getDriver().getWindowHandles().stream().map(wh -> {
                driver.switchTo().window(wh);
                return driver.getTitle();
            }).collect(Collectors.toList());
            assertThat(windowNames, containsInAnyOrder("first", "second"));
        } catch (Exception e) {
            fail("Switching between windows is not supported", e);
        }
    }
    
    @Features("Switching between frames")
    @Test
    public void testSwitchFrames() throws Exception {
        try {
            openPage(Page.FRAMES);
            WebDriver driver = getDriver();
            WebElement firstFrameElement = driver.findElement(By.id("first"));
            driver.switchTo().frame(firstFrameElement);
            WebElement firstFrame = driver.switchTo().activeElement();
            assertThat(firstFrame.findElements(By.cssSelector("span.test-class")), hasSize(1));

            driver.switchTo().defaultContent();
            WebElement secondFrameElement = driver.findElement(By.id("second"));
            driver.switchTo().frame(secondFrameElement);
            WebElement secondFrame = driver.switchTo().activeElement();
            assertThat(secondFrame.findElements(By.cssSelector("div#test-id")), hasSize(1));
            assertThat(secondFrame.findElements(By.cssSelector("span.test-class")), is(empty()));
        } catch (Exception e) {
            fail("Switching between frames is not supported", e);
        }
    }
    
    @Features("Navigating back and forward")
    @Test
    public void testBackAndForward() throws Exception {
        WebDriver driver = getDriver();
        assertThat(driver.getTitle(), equalTo("first"));
        openPage(Page.SECOND);
        assertThat(driver.getTitle(), equalTo("second"));
        driver.navigate().back();
        assertThat(driver.getTitle(), equalTo("first"));
        driver.navigate().forward();
        assertThat(driver.getTitle(), equalTo("second"));
    }
    
    @Features("Screen orientation support")
    @Ignore
    @Test
    public void testScreenOrientation() {
        try {
            WebDriver driver = getDriver();
            if (driver instanceof Rotatable) {
                ScreenOrientation orientation = ((Rotatable) driver).getOrientation();
                assertThat(orientation, is(notNullValue()));
            } else {
                fail("This driver does not support rotation");
            }
        } catch (Exception e) {
            fail("This driver does not support rotation", e);
        }
    }
    
}
