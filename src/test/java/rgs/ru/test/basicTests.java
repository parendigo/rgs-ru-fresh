package rgs.ru.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;


public class basicTests {

    WebDriver  driver;
    @Before
    public void BeforeTest () {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.navigate().to("https://www.rgs.ru/");
    }

    @Test
    public void insuranceRequestTest () {

        try {

            // Кликаем на меню
            clickField("//body//div[@id=\"main-navbar-collapse\"]//a[contains(text(), 'Меню')]", driver);

            // Кликаем на элемен тменю "Компаниям"
            clickField("//body//div[@id='main-navbar-collapse']//a[contains(text(),'Компаниям')]", driver);

            // Скролим до здоровья и кликаем
            WebElement health = driver.findElement(By.xpath("//div[contains(@class,'col-rgs-content-center-col')]//a[contains(text(),'здоровь')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", health);
            health.click();

            // Так как открылось новое окно, переводим туда драйвер
            ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
            driver.switchTo().window(tabs2.get(1));

            // Кликаем на страхование
            clickField("//*[@id=\"main-content\"]/div[3]/div/div[1]/div/div/a[1]", driver);

            // Проверяем наличие заголовка "Добровольное медицинское страхование"

            try {
                WebElement insuranceTitle = driver.findElement(By.xpath("//div[contains(@class,'col-rgs-content-center')]" +
                        "//h1[contains(text(),'Добровольное медицинское страхование')]"));
                Assert.assertEquals("No such title: Добровольное медицинское страхование",
                        "Добровольное медицинское страхование", insuranceTitle.getText());
            } catch (NoSuchElementException e) {
                System.out.println("No such element: Добровольное медицинское страхование");
            }

            // Жмем кнопку "Отправить заявку"
            clickField("//div[contains(@class,'rgs-context-bar')]//a[contains(text(),'тправить заявку')]", driver);

            /* Заполняем все поля */

            // Ф.И.О
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[1]/input", "Фамилия", driver);
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[2]/input", "Имя", driver);
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[3]/input", "Отчество", driver);

            // Регион
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[4]/select", "Москва", driver);

            // Номер
            clickAndFillField("//*[@id=\"applicationForm\"]/div[2]/div[5]/input", "9999999999", driver);

            // Почта
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[6]/input", "qwertyqwerty", driver);

            // Дата
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[7]/input", "12.08.2021", driver);

            // Комментарий
            fillField("//*[@id=\"applicationForm\"]/div[2]/div[8]/textarea", "Comment", driver);

            // Я согласен на обработку данных
            clickField("//*[@id=\"applicationForm\"]/div[2]/div[9]", driver);

            // Кнопка Отправить
            clickField("//*[@id=\"button-m\"]", driver);

            // Првоеряем у поля почты ошибку
            try {
                WebElement emailError = driver.findElement(By.xpath("//*[@id=\"applicationForm\"]/div[2]/div[6]/div/label/span"));
                Assert.assertEquals("No email error", emailError.getText(), "Введите адрес электронной почты");
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
        } catch (InvalidSelectorException e) {
            System.out.println("Bad xpath");
        }
    }

    @After
    public void afterTest () {
        driver.quit();
    }

    public void clickField (String xpath, WebDriver driver) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            element.click();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fillField (String xpath, String str, WebDriver driver) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            element.sendKeys(str);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clickAndFillField (String xpath, String str, WebDriver driver) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            element.click();
            element.sendKeys(str);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
