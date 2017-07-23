# Selenoid Container Tests
This repository contains an automated Java test suite which is run against every [Selenoid](https://github.com/aerokube/selenoid) container to verify all major [Selenium](http://seleniumhq.org/) features work as expected. To run the suite type:
```
$ mvn clean test -Dgrid.connection.url=http://my-selenoid-host.example.com:4444/wd/hub -Dgrid.browser.name=firefox -Dgrid.browser.version=52.0
```
