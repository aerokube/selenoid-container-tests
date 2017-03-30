package com.aerokube.selenoid.misc;

import ru.qatools.properties.DefaultValue;
import ru.qatools.properties.Property;

interface TestProperties {

    @Property("pages.base.url")
    @DefaultValue("http://vania-pooh.github.io/selenoid-container-tests/pages")
    String getBaseUrl();
    
    @Property("grid.auth.login")
    String getLogin();

    @Property("grid.auth.password")
    String getPassword();

    @Property("grid.host.name")
    @DefaultValue("localhost")
    String getHostName();

    @Property("grid.host.port")
    @DefaultValue("4444")
    Integer getHostPort();

    @Property("grid.browser.name")
    @DefaultValue("firefox")
    String getBrowserName();

    @Property("grid.browser.version")
    @DefaultValue("52.0")
    String getBrowserVersion();
}
