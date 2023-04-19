package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SecondLabIndivid {

    private WebDriver chromeDriver;

    private static final String baseUrl = "https://www.olx.ua/uk/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions(){
        chromeDriver.get(baseUrl);
    }

    @Test
    public void isSearchFieldExistAndEmpty(){
        WebElement headerSearch = chromeDriver.findElement(By.id("headerSearch"));
        Assert.assertNotNull(headerSearch);
        Assert.assertEquals(headerSearch.getText(), "");
    }

    @Test
    public void testSearchField() {
        WebElement search = chromeDriver.findElement(By.className("queryfield"));
        Assert.assertNotNull(search);
        String inputValue = "Motorcycle";
        // нужно чтобы вводить текст в поле поиска
        search.click();
        search.sendKeys(inputValue);
        Assert.assertEquals(search.getAttribute("value"), inputValue);
        search.sendKeys(Keys.ENTER);
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testAdsExist() {
        chromeDriver.get(baseUrl + "dnepr/q-Motorcycle/");
        WebElement adsCountWrapper = chromeDriver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/form/div[4]/div[2]/h3/div"));
        Assert.assertNotNull(adsCountWrapper);
        int adsCountInt = Integer.parseInt(adsCountWrapper.getText().replaceAll("\\D+",""));
        Assert.assertTrue(adsCountInt > 0);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown(){
        chromeDriver.quit();
    }

}
