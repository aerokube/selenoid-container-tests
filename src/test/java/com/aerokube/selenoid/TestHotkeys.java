package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Features;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestHotkeys extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.HOTKEYS);
    }

    @Features("Pressing keys on the keyboard")
    @Test
    public void testHotkeys() {
        WebDriver driver = getDriver();
        String someKeys = Keys.chord(Keys.CONTROL, "c");
        WebElement divTag = driver.findElement(By.id("test-id"));
        assertThat(divTag.getText(), equalTo("initial"));
        driver.findElement(By.tagName("html")).sendKeys(someKeys);
        assertThat(divTag.getText(), equalTo("new"));
    }
    
}
