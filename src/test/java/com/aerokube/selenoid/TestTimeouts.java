package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Features;

import java.util.concurrent.TimeUnit;

@Features("Setting timeout values")
public class TestTimeouts extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.FIRST);
        waitUntilElementIsPresent(By.cssSelector("#test-id"));
    }

    @Features("Setting page load timeout")
    @Test
    public void testPageLoadTimeout() throws Exception {
        try {
            WebDriver driver = getDriver();
            driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.MILLISECONDS);
            openPage(Page.SECOND);
        } catch (Exception e) {
            fail("Setting page load timeout is not supported", e);
        }
    }

    @Features("Setting implicit timeout")
    @Test
    public void testImplicitTimeout() throws Exception {
        try {
            WebDriver driver = getDriver();
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            fail("Implicitly waiting is not supported", e);
        }
    }

    @Features("Setting script execution timeout")
    @Test
    public void testScriptTimeout() throws Exception {
        try {
            WebDriver driver = getDriver();
            driver.manage().timeouts().setScriptTimeout(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            fail("Setting script timeout is not supported", e);
        }
    }
}
