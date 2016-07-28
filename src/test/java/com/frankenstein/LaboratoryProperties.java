package com.frankenstein;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Properties;

public class LaboratoryProperties {

    final static Logger logger = Logger.getLogger("LaboratoryProperties");

    public static int wait30secs = 30;
    public static int wait20secs = 20;
    public static int wait10secs = 10;
    public static int wait1000ms = 1000;
    public static int wait2000ms = 2000;
    public static int wait4000ms = 4000;

    public LaboratoryProperties() {
        new File(System.getProperty("java.io.tmpdir")).mkdirs();
    }

    enum PropertyStatus {
        notLoaded,
        loaded
    }

    private static PropertyStatus status = PropertyStatus.notLoaded;
    private static Properties properties = null;

    synchronized public static String getProperty(final String what,
                                                  final String def) {
        if (status == PropertyStatus.notLoaded) {
            status = PropertyStatus.loaded;

            final String fileName = System.getenv("LAB_PROPS");
            if (fileName != null) {
                properties = new Properties();
                try {
                    properties.load(new FileInputStream(fileName));
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        }

        String retVal = def;

        if (properties != null) {
            retVal = properties.getProperty(what, def);
        }

        return retVal;
    }

    public static String getPropertyFromFile(String string) {
        return System.getProperty(string, "X_NOT_DEFINED_X" + string);
    }

    public static String getLaboratoryPasscode() {
        return getProperty("laboratory.passcode", getPropertyFromFile("laboratory.passcode"));
    }

    public static String getIncorrectLaboratoryPasscode() {
        return getProperty("laboratory.passcode.incorrect", getPropertyFromFile("laboratory.passcode.incorrect"));
    }

    public static String ieDriverPath() {
        return getProperty("webdriver.ie.driver", getPropertyFromFile("webdriver.ie.driver"));
    }

    public static String chromeDriverPath() {
        return getProperty("webdriver.chrome.driver", getPropertyFromFile("webdriver.chrome.driver"));
    }

    public static String gridHost() {
        return getProperty("fcml.gridhost", getPropertyFromFile("fcml.gridhost"));
    }

    public static String browserUnderTest() {
        return getProperty("browser.undertest", getPropertyFromFile("browser.undertest"));
    }

    public static String getRpcaUrl()  {
        return getProperty("rpca.url", getPropertyFromFile("rpca.url"));
    }

}

