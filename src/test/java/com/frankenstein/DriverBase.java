package com.frankenstein;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverBase {

    static Logger logger = Logger.getLogger(DriverBase.class);
    private static WebDriver browserDriver = null;
    public static String browserChoice = LaboratoryProperties.browserUnderTest();
    //public static String browserChoice = "firefox";
    public static String gridHost = LaboratoryProperties.gridHost();
    static long startTime = System.currentTimeMillis();

    /**
     * Instantiate instance of WebDriver object if one does not already exist
     * @return  WebDriver
     */
    public static WebDriver wd() {

        if (browserDriver == null) {

            if (browserChoice.equalsIgnoreCase("firefox")) {
                browserDriver = new FirefoxDriver();
            }

            //for Internet Explorer browser
            if (browserChoice.equalsIgnoreCase("iexplore")) {

                System.setProperty("webdriver.ie.driver", System.getenv("webdriver.ie.driver"));
                browserDriver = new InternetExplorerDriver();

            }

            if (browserChoice.equalsIgnoreCase("chrome")) {

                System.setProperty("webdriver.chrome.driver", System.getenv("webdriver.chrome.driver"));
                browserDriver = new ChromeDriver();

            }

            //starts host instance of Grid server and returns instance of RemoteWebDriver for Grid2 testing
            if (browserChoice.equalsIgnoreCase("grid")) {

                //Requirements for firefox - note BrowserName, Platform and Version must match corresponding node
                DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                capabilities.setPlatform(Platform.WINDOWS);
                capabilities.setBrowserName("firefox");
                capabilities.setVersion("47");

                //Internet Explorer
                //DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                //capabilities.setPlatform(Platform.WINDOWS);
                //capabilities.setBrowserName("internet explorer");

                try {
                    browserDriver = new RemoteWebDriver(new URL(gridHost), capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }

            if (browserDriver == null) {

                logger.info("browser.undertest setting in props file is" +
                        " not set to a valid value i.e. firefox, iexplore, chrome or grid");

            }

        }
        return browserDriver;
    }


    /**
     * Close browser window associated with singleton
     * instance of WebDriver
     */
    public static void closeWebDriver() {

        calculateTimeElapsed();
        wd().close();

    }

    /**
     * Destroy singleton instance of WebDriver
     */
    public static void destroyWebDriver() {

        wd().quit();
        browserDriver = null;

    }

    /**
     * Cause the method to sleep
     */
    public static void pause(int sleepMs) {

        try {
            TimeUnit.MILLISECONDS.sleep(sleepMs);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private static void calculateTimeElapsed() {

        int milliSecs = 0, secs = 0, mins = 0;
        long timeElapsed = System.currentTimeMillis() - startTime;

        milliSecs = (int)(timeElapsed % 1000);
        secs = (int)(timeElapsed/1000 % 60);
        mins = (int)(timeElapsed/60000);

        String timeElapsedMsg = String.format("Duration of last test execution: %d:%02d:%d\n", mins, secs, milliSecs );
        logger.info(timeElapsedMsg);
        //System.out.println(timeElapsedMsg);

    }


}

