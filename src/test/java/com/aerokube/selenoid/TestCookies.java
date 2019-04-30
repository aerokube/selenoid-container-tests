package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Features;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestCookies extends TestBase {
    

    @Before
    public void before() throws Exception {
        openPage(Page.FIRST);
        waitUntilElementIsPresent(By.id("test-id"));
    }

    @Features("Working with cookies")
    @Test
    public void testCookie() throws Exception {
        final String COOKIE_NAME = "test-cookie";
        final String COOKIE_VALUE = "test-cookie";
        WebDriver driver = getDriver();
        try {
            driver.manage().deleteAllCookies();
            Cookie cookie = new Cookie(COOKIE_NAME, COOKIE_VALUE);
            driver.manage().addCookie(cookie);
            Cookie fetchedCookie = driver.manage().getCookieNamed(COOKIE_NAME);
            assertThat(fetchedCookie.getValue(), equalTo(COOKIE_VALUE));
            driver.manage().deleteCookie(fetchedCookie);
            assertThat(driver.manage().getCookies(), is(empty()));
        } catch (Exception e) {
            fail("Cookies are not supported", e);
        }
    }
    
}
