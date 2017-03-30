package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Features;

public class TestAlert extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.ALERT);
    }

    @Features("Capability to close alerts")
    @Test
    public void testAlertClose() throws Exception {
        try {
            WebDriver driver = getDriver();
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            fail("Closing alerts is not supported", e);
        }
    }


}
