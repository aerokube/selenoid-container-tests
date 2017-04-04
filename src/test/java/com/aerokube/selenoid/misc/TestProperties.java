package com.aerokube.selenoid.misc;

import ru.qatools.properties.DefaultValue;
import ru.qatools.properties.Property;

interface TestProperties {

    @Property("pages.base.url")
    @DefaultValue("http://aerokube.github.io/selenoid-container-tests/pages")
    String getBaseUrl();
    
    @Property("grid.connection.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String getConnectionUrl();

    @Property("grid.browser.name")
    @DefaultValue("firefox")
    String getBrowserName();

    @Property("grid.browser.version")
    @DefaultValue("52.0")
    String getBrowserVersion();
}
