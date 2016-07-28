package com.frankenstein;

/**
 * Created by stephenmckinney on 27/07/2016.
 */

import static com.frankenstein.DriverBase.logger;
import static com.frankenstein.DriverBase.pause;
import static com.frankenstein.DriverBase.wd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class LoginPage {

    private WebDriver driver = wd();

    @FindBy(how = How.CLASS_NAME, using = "keypadBtn")
    private List<WebElement> keyPadBtns;
    @FindBy(how = How.ID, using = "keysPressed")
    public WebElement keycodeStatus;
    @FindBy(how = How.ID, using = "keypad")
    public WebElement keyPad;

    public LoginPage() {

        PageFactory.initElements(driver, this);

    }

    public void getPage() {

        int nos_keypadBtns = 9;
        String keypadBtnClass = "keypadBtn";

        WebDriverWait wait = new WebDriverWait(driver, LaboratoryProperties.wait10secs);
        driver.get(LaboratoryProperties.getRpcaUrl());
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className(keypadBtnClass), nos_keypadBtns));

    }

    public LaboratoryPage correctKeypadEntryAllowsAccessToPage() {

        String keypadCode = LaboratoryProperties.getLaboratoryPasscode();

        //Pause to allow users to see page
        pause(LaboratoryProperties.wait2000ms);

        enterKeycode(keypadCode);

        //Pause to allow users to see page
        pause(LaboratoryProperties.wait2000ms);

        return new LaboratoryPage();

    }

    public void incorrectKeypadEntryDeniesAccessToPage() {

        String keypadCode = LaboratoryProperties.getIncorrectLaboratoryPasscode();
        //Pause to allow users to see page
        pause(LaboratoryProperties.wait2000ms);

        enterKeycode(keypadCode);

        //Pause to allow users to see page
        pause(LaboratoryProperties.wait2000ms);

    }

    private void enterKeycode(String keycode) {

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
        String[] keypadCodeKeys = keycode.split("(?<=.)");

        try {
            for(int i = 0; i < keypadCodeKeys.length; i++) {

                for(WebElement button : keyPadBtns) {
                    if(button.getText().equals(keypadCodeKeys[i])) {
                        button.click();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error has occurred: " + e.getMessage());
        }


    }


}
