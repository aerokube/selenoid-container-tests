package com.aerokube.selenoid;

import com.aerokube.selenoid.misc.Page;
import com.aerokube.selenoid.misc.TestBase;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.yandex.qatools.allure.annotations.Features;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TestProxy extends TestBase {
    
    private static final BrowserMobProxy proxy = new BrowserMobProxyServer();
    
    @BeforeClass
    public static void initProxy(){
        if (!proxy.isStarted()) {
            proxy.start();
            proxy.setHarCaptureTypes(CaptureType.REQUEST_HEADERS);
        }
    }
    
    @Before
    public void startHar(){
        if (proxy.isStarted()) {
            proxy.newHar();
        }
    }
    
    @Features("Using proxies")
    @Test
    public void testProxy() throws Exception {
        openPage(Page.FIRST);
        List<HarEntry> proxyLogEntries = proxy.getHar().getLog().getEntries();
        assertThat(proxyLogEntries, is(not(empty()))); //Probably need to to add more assertions :)
    }

    @After
    public void clearHar() {
        if (proxy.isStarted()) {
            proxy.endHar();
        }
    }
    
    @AfterClass
    public static void shutdownProxy() {
        if (proxy.isStarted()) {
            proxy.stop();
        }
    }

    @Override
    protected Function<DesiredCapabilities, DesiredCapabilities> getCapabilitiesProcessor() {
        return dc -> new DesiredCapabilities(dc, getProxyCapabilities(dc));
    }

    private DesiredCapabilities getProxyCapabilities(DesiredCapabilities caps) {
        DesiredCapabilities ret = new DesiredCapabilities();
        String proxyString = getProxyString();
        Proxy proxy = new Proxy();
        proxy
                .setProxyType(Proxy.ProxyType.MANUAL)
                .setHttpProxy(proxyString)
                .setFtpProxy(proxyString)
                .setSslProxy(proxyString);

        if (caps.getBrowserName().contains("chrome")) {
            @SuppressWarnings("unchecked")
            List<String> switches = caps.getCapability("chrome.switches") != null ? 
                    (List<String>) caps.getCapability("chrome.switches") :
                    new ArrayList<>();
            switches.add("--proxy-server=" + proxyString);
            caps.setCapability("chrome.switches", switches);
        }
        
        ret.setCapability(CapabilityType.PROXY, proxy);
        return ret;
    }

    private String getProxyString() {
        return String.format("%s:%d", getHostName(), proxy.getPort());
    }
    
}
