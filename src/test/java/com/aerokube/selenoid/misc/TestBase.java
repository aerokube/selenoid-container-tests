package com.aerokube.selenoid.misc;

import org.junit.Assert;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class TestBase {

    private static final Logger LOG = LoggerFactory.getLogger(TestBase.class);
    
    @Rule
    public WebDriverRule webDriverRule;
    
    @Rule
    public JettyRule jettyRule = new JettyRule();

    public TestBase() {
        this.webDriverRule = new WebDriverRule(getCapabilitiesProcessor());
    }

    public WebDriver getDriver() {
        return webDriverRule.getDriver();
    }
    
    public void openPage(Page page) throws Exception {
        String pageUrl = getPageUrl(page);
        LOG.info(String.format("Opening page at: %s", pageUrl));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executor.submit(() -> {
                getDriver().get(pageUrl);
            });
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.info(String.format("Failed to open page at: %s", pageUrl));
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }
    }

    private String getPageUrl(Page page) throws Exception {
        String file = String.format("/%s", page.getName());
        return new URL("http", JettyRule.getHostName(), jettyRule.getPort(), file).toString();
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
    
}
