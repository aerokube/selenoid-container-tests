package com.aerokube.selenoid.misc;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebDriverRule implements TestRule {
    
    private static final TestProperties PROPERTIES = PropertyLoader.newInstance().populate(TestProperties.class);
    private static final String OPERA = "opera";
    
    private WebDriver driver;
    
    private final Function<DesiredCapabilities, DesiredCapabilities> capabilitiesProcessor;

    WebDriverRule(Function<DesiredCapabilities, DesiredCapabilities> capabilitiesProcessor) {
        this.capabilitiesProcessor = capabilitiesProcessor;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                driver = new RemoteWebDriver(getConnectionUrl(), capabilitiesProcessor.apply(getDesiredCapabilities()));
                driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                try {
                    base.evaluate();
                } catch (Throwable e) {
                    takeScreenshot(driver);
                    throw e;
                } finally {
                    if (driver != null) {
                        driver.quit();
                        driver = null;
                    }
                }
            }
        };
    }
    
    private URL getConnectionUrl() throws MalformedURLException {
        return new URL(PROPERTIES.getConnectionUrl());
    }

    private DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities(PROPERTIES.getBrowserName(), PROPERTIES.getBrowserVersion(), Platform.LINUX);
        caps.setCapability("screenResolution", "1280x1024x24");
        if (OPERA.equals(PROPERTIES.getBrowserName())) {
            caps.setCapability("operaOptions", new HashMap<String, Object>(){
                {
                    put("binary", "/usr/bin/opera");
                }
            });
        }
        return caps;
    }

    public WebDriver getDriver() {
        return driver;
    }

    String getPageUrl(Page page) {
        return String.format("%s/%s", PROPERTIES.getBaseUrl(), page.getName());
    }

    @Attachment("failure-screenshot")
    private byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) new Augmenter().augment(driver)).getScreenshotAs(OutputType.BYTES);
    }
    
}
