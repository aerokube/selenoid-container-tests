package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Features;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestEvaluateJavascript extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.FIRST);
        waitUntilElementIsPresent(By.cssSelector("#test-id"));
        WebDriver driver = getDriver();
        assertThat("Javascript execution is not supported", driver, is(instanceOf(JavascriptExecutor.class)));
    }

    @Features("Synchronous Javascript evaluation")
    @Test
    public void testEvaluateJavascript() throws Exception {
        try {
            WebDriver driver = getDriver();

            WebElement element = driver.findElement(By.cssSelector("div#test-id"));
            assertThat(element.getText(), equalTo("foo"));

            JavascriptExecutor javaScriptExecutor = (JavascriptExecutor) driver;
            javaScriptExecutor.executeScript(getScript());
            assertThat(element.getText(), equalTo("bar"));

        } catch (Exception e) {
            fail("Synchronous javascript execution is not supported", e);
        }
    }

    @Features("Asynchronous Javascript evaluation")
    @Test
    public void testEvaluateJavascriptAsync() throws Exception {
        try {
            WebDriver driver = getDriver();

            WebElement element = driver.findElement(By.cssSelector("div#test-id"));
            assertThat(element.getText(), equalTo("foo"));

            JavascriptExecutor javaScriptExecutor = (JavascriptExecutor) driver;
            driver.manage().timeouts().setScriptTimeout(1000, TimeUnit.MILLISECONDS);
            String result = String.valueOf(javaScriptExecutor.executeAsyncScript(getAsyncScript()));
            assertThat(element.getText(), equalTo("baz"));
            assertThat(result, equalTo("works"));
        } catch (Exception e) {
            fail("Asynchronous javascript execution is not supported");
        }
    }

    @Features("Window scrolling")
    @Test
    public void testScroll() throws Exception {
        try {
            WebDriver driver = getDriver();
            JavascriptExecutor javaScriptExecutor = (JavascriptExecutor) driver;
            javaScriptExecutor.executeScript("scroll(0, 500);");
        } catch (Exception e) {
            fail("Window scrolling is not supported", e);
        }
    }

    private String getScript() {
        return "var div = document.getElementById('test-id'); div.textContent = 'bar';";
    }

    private String getAsyncScript() {
        return "var callback = arguments[arguments.length - 1];" +
                " var div = document.getElementById('test-id');" +
                " div.textContent = 'baz'; callback('works');";
    }

}
