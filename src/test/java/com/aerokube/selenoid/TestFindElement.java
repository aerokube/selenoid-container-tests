package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Features;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@Features("Finding element")
public class TestFindElement extends TestBase {

    @Before
    public void before() throws Exception {
        openPage(Page.FIRST);
        waitUntilElementIsPresent(By.id("test-id"));
    }

    @Features("Finding element by CSS selector")
    @Test
    public void testFindByCSSSelector() throws Exception {
        WebDriver driver = getDriver();
        List<WebElement> elementsByCSSSelector = driver.findElements(By.cssSelector("div#test-id"));
        assertThat(elementsByCSSSelector, hasSize(1));
        assertThat(elementsByCSSSelector.get(0).getTagName(), equalToIgnoringCase("div"));
        assertThat(elementsByCSSSelector.get(0).getText(), equalTo("foo"));
    }

    @Features("Finding element by ID")
    @Test
    public void testFindById() throws Exception {
        WebDriver driver = getDriver();
        List<WebElement> elementsById = driver.findElements(By.id("test-id"));
        assertThat(elementsById, hasSize(1));
        assertThat(elementsById.get(0).getTagName(), equalToIgnoringCase("div"));
        assertThat(elementsById.get(0).getText(), equalTo("foo"));
    }

    @Features("Finding element by class name")
    @Test
    public void testFindByClass() throws Exception {
        WebDriver driver = getDriver();
        List<WebElement> elementsByClass = driver.findElements(By.className("test-class"));
        assertThat(elementsByClass, hasSize(1));
        assertThat(elementsByClass.get(0).getTagName(), equalToIgnoringCase("span"));
        assertThat(elementsByClass.get(0).getText(), equalTo("bar"));
    }
    
    @Features("Finding element by XPath expression")
    @Test
    public void testFindByXPath() throws Exception {
        WebDriver driver = getDriver();
        List<WebElement> elementsByXPath = driver.findElements(By.xpath("/html/body/span[contains(@class, 'test-class')]"));
        assertThat(elementsByXPath, hasSize(1));
        assertThat(elementsByXPath.get(0).getTagName(), equalToIgnoringCase("span"));
        assertThat(elementsByXPath.get(0).getText(), equalTo("bar"));
    }
    
}
