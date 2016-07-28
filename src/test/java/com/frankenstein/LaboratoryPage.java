package com.frankenstein;

import static com.frankenstein.DriverBase.pause;
import static com.frankenstein.DriverBase.wd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class LaboratoryPage {

    WebDriver driver = wd();

    @FindBy(how = How.ID, using = "controls")
    public WebElement controls;

    public LaboratoryPage() {

        PageFactory.initElements(driver, this);

    }



}
