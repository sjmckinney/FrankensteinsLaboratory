package com.frankenstein;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class LaboratoryReference{


    private static WebDriver browserDriver = new FirefoxDriver();
    @BeforeClass
    public static void setUp() throws Exception {

        //home
        //browserDriver.getPage("http://localhost/~stan/Frankenstein/laboratory.html");
        //work
        browserDriver.get("http://localhost/Frankenstein/laboratory.html");
    }

    /*
    Users must enter the a four digit code to access the app.
    The key presses are echoed as asterisks on screen for key-presses 1, 2, and 3.
    On the fourth key-press the code is validated;
     - if correct the app screen appears
     - if incorrect then the message "Incorrect code" appears in place of the asterisks
    The user can attempt to enter a new code immediately after failure - no lockout or max attempts
    If the page is refreshed the page is reset - any "Incorrect code" messages will disappear
    */


    @Test
    /*
    Test that an incorrectly entered key code;
     - triggers the correct error message
     - does not allow access to the app
    Test that the first three key-presses cause key-press indicators to appear on screen
    */
    public void incorrectKeypadEntryDeniesAccessToPageTest() throws InterruptedException {

        String[] keypadCode = {"btn1", "btn2", "btn3", "btn2"};
        String codeEnteredHtml = "";
        String codeExpected = "";
        String asterisk = "***";
        String codeEnteredText = "";

        //Pause to allow users to see page
        Thread.sleep(2000);

        //Check that web page is in correct state to test
        assertTrue("Application is not in correct state to test; keypad not visible", browserDriver.findElement(By.id("keypad")).isDisplayed());
        assertFalse("Application is not in correct state to test; app is visible", browserDriver.findElement(By.id("controls")).isDisplayed());

        //For key-presses 1 to 3 test that a string of asterisks equal to the number of key-presses is displayed.
        //On key-press 4 (which complete entry of incorrect code) test that
        for(int i=0; i < keypadCode.length; i++) {
            browserDriver.findElement(By.id(keypadCode[i])).click();
            //i has values 0, 1 and 2 for first three key-presses
            if(i<3){
                //Dynamically adjust length and or content of test string
                //dependent on value of loop counter i
                codeExpected = asterisk.substring(0, i+1);
            } else {
                codeExpected = "Incorrectcode";
            }

            //Get text from element displaying kep-press indicator and remove mark-up
            codeEnteredHtml = browserDriver.findElement(By.id("keysPressed")).getText();
            codeEnteredText = codeEnteredHtml.replaceAll("\\p{Cntrl}", "");

            //Assert displayed text is correct
            assertEquals("Value displayed " + codeEnteredText + " does not match expected text" + codeExpected +
                    " for key-press" + (i + 1), codeExpected, codeEnteredText);
        }

        Thread.sleep(2000);

        //Test that keypad is still visible and that app element is not i.e.
        //user cannot access app having entered incorrect key code
        assertTrue("Key-pad is no longer displayed", browserDriver.findElement(By.id("keypad")).isDisplayed());
        assertFalse("App should not be displayed", browserDriver.findElement(By.id("controls")).isDisplayed());

    }

    @Test
    /*
    Test that a correctly entered key code does allow access to the app
    */
    public void correctKeypadEntryAllowsAccessToPageTest() throws InterruptedException {

        List<WebElement> keypadBtns = new ArrayList<WebElement>();
        String[] keypadCode = {"1", "2", "3", "1"};

        //Pause to allow users to see page
        Thread.sleep(2000);

        //Check that web page is in correct state to test
        //Note the the isDisplayed function returns a boolean value depending on whether the element is displayed on screen.
        assertTrue("Application is not in correct state to test; keypad not visible", browserDriver.findElement(By.id("keypad")).isDisplayed());
        assertFalse("Application is not in correct state to test; app is visible", browserDriver.findElement(By.id("controls")).isDisplayed());

        /*
        Taking a different approach to finding and manipulating elements.
        Here we generate a List of web elements, keyPadBtns (having the class "keypadBtn").
        Then, using the values in the keypadCode array,loop through the keypadBtns
        List until we match the text value against the array value and then perform
        the click action on that element.
        This approach would be used when elements have a common identifying
        attribute rather than individual identifiers.
         */

        //findElements returns one or more web elements matching the selector
        keypadBtns = browserDriver.findElements(By.className("keypadBtn"));

        for(int i = 0; i < keypadCode.length; i++) {

            for(WebElement button : keypadBtns) {
                if(button.getText().equals(keypadCode[i])) {
                    button.click();
                    break;
                }
            }
        }

        //Pause to allow users to see page
        Thread.sleep(2000);

        assertFalse("The key pad is still visible after correct code has been entered", browserDriver.findElement(By.id("keypad")).isDisplayed());
        assertTrue("The app is not being displayed after correct code has been entered", browserDriver.findElement(By.id("controls")).isDisplayed());

    }

    @Test
    public void sliderSettingsAreOptimalDefaultSettingsTest() throws InterruptedException {

        String expectedOptimalPositionIndicatorRed = "rgba(255, 0, 0, 1)";
        String actualOptimalPositionIndicatorColor = "";
        List<WebElement> sliderValueField = new ArrayList<WebElement>();

        actualOptimalPositionIndicatorColor = browserDriver.findElement(By.id("settingsCorrect")).getCssValue("background-color");

        assertEquals("Optimal Position indicator colour is not correct - should be red",
                expectedOptimalPositionIndicatorRed, actualOptimalPositionIndicatorColor);

        sliderValueField = browserDriver.findElements(By.className("inputCls"));

    }

    @Test
    public void sliderSettingsAreOptimalTest() throws InterruptedException {

        //sliderMaxValue needs to be double as manipulating mathematically
        double sliderMaxValue = 10;
        //String expectedOptimalPositionIndicatorRed = "rgba(255, 0, 0, 1)";
        String expectedOptimalPositionIndicatorGreen = "rgba(0, 128, 0, 1)";
        String actualOptimalPositionIndicatorColor = "";
        int sliderValuesIdx = 0;
        String sliderLength = "";
        double sliderLengthDbl = 0;
        double[] sliderValues = {2, 4, 7};
        List<WebElement> sliderHandles = new ArrayList<WebElement>();

        //actualOptimalPositionIndicatorColor = browserDriver.findElement(By.id("settingsCorrect")).getCssValue("background-color");

        //assertEquals("Optimal Position indicator colour is not correct - should be red",
        //expectedOptimalPositionIndicatorRed, actualOptimalPositionIndicatorColor);

        sliderLength = browserDriver.findElement(By.id("slider1")).getCssValue("width");
        //sliderLength is returned as String like 365px so remove all letters and cast to number
        sliderLengthDbl = Double.parseDouble(sliderLength.replaceAll("[^\\d]", ""));

        //Get a list of all the slider handle elements
        sliderHandles = browserDriver.findElements(By.className("ui-slider-handle"));

        //Pause to allow users to see page
        Thread.sleep(2000);

        /*
        For each slider handle in the list calculate the distance it needs to be dragged.
        Then use the Actions class to perform the drag and drop movement.
        NOTE: this code could fail as it assumes that the for loop accesses the web elements
        in the order they appear on the page and therefore match the order of the values in
        sliderValues array.
        */
        for(WebElement handle : sliderHandles) {
            double dragProportion = sliderValues[sliderValuesIdx]/sliderMaxValue;
            double calcDragLength = dragProportion * sliderLengthDbl;
            //convert to integer as expected type for dragAndDropBy function below
            int dragLength = (int)calcDragLength;
            (new Actions(browserDriver)).dragAndDropBy(handle, dragLength, 0).perform();
            //Check displayed value is correct
            sliderValuesIdx++;
        }

        //Once the sliders have been set to the correct value the Optimal Position element will change colour
        actualOptimalPositionIndicatorColor = browserDriver.findElement(By.id("settingsCorrect")).getCssValue("background-color");

        assertEquals("Optimal Position indicator colour is not correct - should be green",
                expectedOptimalPositionIndicatorGreen, actualOptimalPositionIndicatorColor);

        //Actions dragAndDrop = new Actions(browserDriver);
        //dragAndDrop.clickAndHold(sliderHandle);
        //dragAndDrop.moveByOffset(100, 0);
        //dragAndDrop.release();
        //dragAndDrop.perform();

        //Pause to allow users to see page
        Thread.sleep(2000);
    }

    @Test
    public void radioButtonSettingsAreOptimalTest() throws InterruptedException {

        String riskLevel = "";
        List<WebElement> maxRadioLabels = new ArrayList<WebElement>();

        Thread.sleep(2000);

        riskLevel = browserDriver.findElement(By.id("riskLevelPara")).getText();

        assertEquals("Risk level: Boring", riskLevel);

        maxRadioLabels = browserDriver.findElements(By.className("choice"));

        for(WebElement radioLabel : maxRadioLabels) {

            if(radioLabel.getText().equals("100%")) {
                String radioLabelValue = radioLabel.getAttribute("for");
                browserDriver.findElement(By.id(radioLabelValue)).click();
            }
        }

        riskLevel = browserDriver.findElement(By.id("riskLevelPara")).getText();

        assertEquals("Risk level: Insane", riskLevel);

        Thread.sleep(2000);
    }

    @Test
    public void extractAndRetractProgressbarTest() throws InterruptedException {

        long maxProgressbarVal = 99;
        long minProgressbarVal = 1;
        JavascriptExecutor js =  (JavascriptExecutor) browserDriver;

        assertTrue(((Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") == minProgressbarVal));

        browserDriver.findElement(By.id("extend")).click();

        WebDriverWait wait = new WebDriverWait(browserDriver, 12);

        wait.until(new ExpectedCondition<Boolean>() {
            //@Override
            public Boolean apply(WebDriver d) {
                long maxProgressbarVal = 99;
                JavascriptExecutor js =  (JavascriptExecutor) d;
                System.out.println("ExpectedCondition: " + ((Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") > maxProgressbarVal));
                return (Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") > maxProgressbarVal;
            }
        });

        assertTrue(((Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") > maxProgressbarVal));

        Thread.sleep(2000);

        browserDriver.findElement(By.id("retract")).click();

        wait.until(new ExpectedCondition<Boolean>() {
            //@Override
            public Boolean apply(WebDriver d) {
                long minProgressbarVal = 1;
                JavascriptExecutor js =  (JavascriptExecutor) d;
                System.out.println("ExpectedCondition: " + ((Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") < minProgressbarVal));
                return (Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") < minProgressbarVal;
            }
        });

        assertTrue(((Long)js.executeScript("return $(\"#progressbar\").progressbar('value');") < minProgressbarVal));

    }


    @AfterClass
    public static void deleteMeasuresTearDown() {

        browserDriver.quit();

    }

}
