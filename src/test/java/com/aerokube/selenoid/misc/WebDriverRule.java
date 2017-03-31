package com.aerokube.selenoid.misc;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.qatools.properties.PropertyLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebDriverRule extends ExternalResource {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverRule.class);
    
    private static final TestProperties PROPERTIES = PropertyLoader.newInstance().populate(TestProperties.class);
    
    private WebDriver driver;
    
    private final Function<DesiredCapabilities, DesiredCapabilities> capabilitiesProcessor;

    WebDriverRule(Function<DesiredCapabilities, DesiredCapabilities> capabilitiesProcessor) {
        this.capabilitiesProcessor = capabilitiesProcessor;
    }

    @Override
    protected void before() throws Throwable {
        driver = new RemoteWebDriver(getConnectionUrl(), capabilitiesProcessor.apply(getDesiredCapabilities()));
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }
    
    private URL getConnectionUrl() throws MalformedURLException {
        return new URL(
                areLoginAndPasswordPresent() ?
                        String.format(
                                "http://%s:%s@%s:%s/wd/hub",
                                PROPERTIES.getLogin(),
                                PROPERTIES.getPassword(),
                                PROPERTIES.getHostName(),
                                PROPERTIES.getHostPort()
                        ) :
                        String.format(
                                "http://%s:%s/wd/hub",
                                PROPERTIES.getHostName(),
                                PROPERTIES.getHostPort()
                        )
        );
    }
    
    private boolean areLoginAndPasswordPresent() {
        String login = PROPERTIES.getLogin();
        String password = PROPERTIES.getPassword();
        return login != null && !login.isEmpty() && password != null && !password.isEmpty();
    }

    private DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities(PROPERTIES.getBrowserName(), PROPERTIES.getBrowserVersion(), Platform.LINUX);
        caps.setCapability("screenResolution", "1280x1024");
        return caps;
    }
    
    @Override
    protected void after() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        });
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.info("Deleting session timed out");
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    String getPageUrl(Page page) {
        return String.format("%s/%s", PROPERTIES.getBaseUrl(), page.getName());
    }
}
