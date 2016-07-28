package com.frankenstein.tests;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.frankenstein.LoginPage;
import com.frankenstein.LaboratoryPage;

import static com.frankenstein.DriverBase.closeWebDriver;
import static com.frankenstein.DriverBase.destroyWebDriver;

public class LoginTests {

    private static LoginPage loginPage = new LoginPage();
    private static LaboratoryPage labPage = null;

    @Before
    public void setUpTest() throws Exception {

        loginPage.getPage();

    }

    @AfterClass
    public static void closeSession() {

        closeWebDriver();
        destroyWebDriver();

    }

    @Test
    public void enterCorrectPasscode() {

        //Check that web page is in correct state to test
        assertTrue("Application is not in correct state to test; keypad not visible", loginPage.keyPad.isDisplayed());

        labPage = loginPage.correctKeypadEntryAllowsAccessToPage();

        //Check that keypad is hidden and controls on app page are visible
        assertTrue("The app is not being displayed after correct code has been entered", labPage.controls.isDisplayed());
        assertFalse("The key pad is still visible after correct code has been entered", loginPage.keyPad.isDisplayed());

    }

    @Test
    public void enterIncorrectPasscode() {

        //Check that web page is in correct state to test
        assertEquals("Application is not in correct state to test; keys pressed indicator should be blank", "", loginPage.keycodeStatus.getText());

        loginPage.incorrectKeypadEntryDeniesAccessToPage();

        //Check that keypad is hidden and controls on app page are visible
        assertEquals("'Incorrect code' message should be displayed after failed passcode entry", "Incorrect\ncode", loginPage.keycodeStatus.getText());
        assertTrue("The key pad is not still visible after incorrect code has been entered", loginPage.keyPad.isDisplayed());

    }

}
