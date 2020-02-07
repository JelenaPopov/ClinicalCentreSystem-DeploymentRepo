package ftn.tim16.ClinicalCentreSystem.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AssignExaminationRoomE2ETests {
    private static final String baseUrl = "http://localhost:4200/";
    private WebDriver driver;

    private LoginPage loginPage;

    private RoomPage roomPage;


    @Before
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        roomPage = PageFactory.initElements(driver, RoomPage.class);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testAssignRoomSuccess() throws InterruptedException {
        driver.navigate().to(baseUrl + "user/login");
        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");

        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();

        loginPage.ensureIsNotVisibleLoginBtn();

        roomPage.ensureIsDisplayedTableForRequests();

        roomPage.ensureIsDisplayedTableForRequests1stElement();

        List<WebElement> rowsBefore = roomPage.getTableForExaminationRequests().findElements(By.tagName("tr"));
        driver.findElement(By.cssSelector(".mat-row:nth-child(3) .mat-button-wrapper")).click();

        roomPage.ensureIsNotVisibleTableForRequests();

        roomPage.ensureIsDisplayedTableForRooms();

        roomPage.ensureIsDisplayedTableForRooms1stElement();

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) .mat-button-wrapper")).click();

        roomPage.ensureIsNotVisibleTableForRooms();

        roomPage.ensureIsDisplayedTableForRequests();

        roomPage.ensureIsDisplayedTableForRequests1stElement();

        List<WebElement> rows = roomPage.getTableForExaminationRequests().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }

    @Test
    public void testAssignRoomWithTimeChangeSuccess() throws InterruptedException {
        driver.navigate().to(baseUrl + "user/login");
        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");

        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();

        loginPage.ensureIsNotVisibleLoginBtn();

        roomPage.ensureIsDisplayedTableForRequests();

        roomPage.ensureIsDisplayedTableForRequests1stElement();

        List<WebElement> rowsBefore = roomPage.getTableForExaminationRequests().findElements(By.tagName("tr"));
        driver.findElement(By.cssSelector(".mat-row:nth-child(2) .mat-button-wrapper")).click();

        roomPage.ensureIsNotVisibleTableForRequests();

        roomPage.ensureIsDisplayedTableForRooms();

        roomPage.ensureIsDisplayedTableForRooms1stElement();

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) .mat-button-wrapper")).click();

        roomPage.ensureIsNotVisibleTableForRooms();

        roomPage.ensureIsDisplayedTableForRequests();

        roomPage.ensureIsDisplayedTableForRequests1stElement();

        List<WebElement> rows = roomPage.getTableForExaminationRequests().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }

    @Test
    public void testAssignRoomWithTimeAndDoctorChangeSuccess() throws InterruptedException {
        driver.navigate().to(baseUrl + "user/login");
        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");

        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();

        loginPage.ensureIsNotVisibleLoginBtn();

        roomPage.ensureIsDisplayedTableForRequests();

        roomPage.ensureIsDisplayedTableForRequests1stElement();

        List<WebElement> rowsBefore = roomPage.getTableForExaminationRequests().findElements(By.tagName("tr"));
        driver.findElement(By.cssSelector(".mat-row:nth-child(1) .mat-button-wrapper")).click();

        roomPage.ensureIsNotVisibleTableForRequests();

        roomPage.ensureIsDisplayedTableForRooms();

        roomPage.ensureIsDisplayedTableForRooms1stElement();

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) .mat-button-wrapper")).click();

        roomPage.ensureIsDisplayedSelectDoctor();
        roomPage.getSelectDoctor().click();

        roomPage.getChooseDoctorOption().click();
        roomPage.ensureIsDisplayedChooseDoctorConfirmBtn();

        roomPage.getChooseDoctorConfirmBtn().click();

        roomPage.ensureIsNotVisibleTableForRooms();

        roomPage.ensureIsDisplayedTableForRequests();

        List<WebElement> rows = roomPage.getTableForExaminationRequests().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }
}
