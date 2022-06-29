package com.aerokube.selenoid.misc;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebDriverRule implements TestRule {

    private static final TestProperties PROPERTIES = PropertyLoader.newInstance().populate(TestProperties.class);
    private static final String CHROME = "chrome";
    private static final String YANDEX = "yandex";
    private static final String OPERA = "opera";

    private WebDriver driver;

    private final Function<MutableCapabilities, MutableCapabilities> capabilitiesProcessor;

    WebDriverRule(Function<MutableCapabilities, MutableCapabilities> capabilitiesProcessor) {
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

    private MutableCapabilities getDesiredCapabilities() {
        switch (PROPERTIES.getBrowserName()) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("no-sandbox");
                chromeOptions.setCapability("screenResolution", "1280x1024x24");
                return chromeOptions;
            case YANDEX:
                ChromeOptions yandexOptions = new ChromeOptions();
                yandexOptions.setBinary("/usr/bin/yandex-browser");
                yandexOptions.addArguments("no-sandbox");
                yandexOptions.setCapability("screenResolution", "1280x1024x24");
                return yandexOptions;
            case OPERA:
                OperaOptions operaOptions = new OperaOptions();
                operaOptions.setCapability(CapabilityType.BROWSER_VERSION, PROPERTIES.getBrowserVersion());
                operaOptions.setBinary("/usr/bin/opera");
                operaOptions.addArguments("no-sandbox");
                operaOptions.setCapability("screenResolution", "1280x1024x24");
                return operaOptions;
            default:
                DesiredCapabilities caps = new DesiredCapabilities(PROPERTIES.getBrowserName(), PROPERTIES.getBrowserVersion(), Platform.LINUX);
                caps.setCapability("screenResolution", "1280x1024x24");
                return caps;
        }
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
