package com.aerokube.selenoid.misc;

import org.junit.Assert;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Function;

public abstract class TestBase {

    private static final Logger LOG = LoggerFactory.getLogger(TestBase.class);
    
    @Rule
    public WebDriverRule webDriverRule;
    
    public TestBase() {
        this.webDriverRule = new WebDriverRule(getCapabilitiesProcessor());
    }

    public WebDriver getDriver() {
        return webDriverRule.getDriver();
    }
    
    public void openPage(Page page) throws Exception {
        String pageUrl = webDriverRule.getPageUrl(page);
        LOG.info(String.format("Opening page at: %s", pageUrl));
        getDriver().get(pageUrl);
    }

    public void fail(String message, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionMessage = sw.toString();
        String msg = String.format("%s: %s", message, exceptionMessage);
        fail(msg);
    }
    
    public void fail(String msg) {
        Assert.fail(msg);
    }
    
    protected Function<DesiredCapabilities, DesiredCapabilities> getCapabilitiesProcessor() {
        return Function.identity();
    }

    protected String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("Failed to determine host name");
            return "localhost";
        }
    }

}
