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

public class ListRequestForTimeOffOrHolidayE2ETest {
    private static final String baseUrl = "http://localhost:4200/";
    private WebDriver driver;

    private ListRequestForTimeOffOrHolidayPage listRequestForTimeOffOrHolidayPage;

    private LoginPage loginPage;

    @Before
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        listRequestForTimeOffOrHolidayPage = PageFactory.initElements(driver, ListRequestForTimeOffOrHolidayPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void testRequestForHolidayOrTimeOffError() throws InterruptedException {
        driver.navigate().to(baseUrl + "user/login");
        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("1st.Admin@maildrop.cc");
        loginPage.getPassword().sendKeys("1st.Admin");

        loginPage.getLoginBtn().click();

        loginPage.ensureIsNotVisibleLoginBtn();

        driver.navigate().to(baseUrl + "clinic-admin/requests-for-holiday-or-time-off");

        Assertions.assertEquals(baseUrl + "error/non-authorized", driver.getCurrentUrl());

        loginPage.getLogoutBtnClinicalCentreAdminBtn().click();

        loginPage.ensureIsNotVisibleLogoutBtnClinicalCentreAdmin();
    }

    @Test
    public void testApproveHolidayOrTimeOffDoctorSuccess() throws InterruptedException {

        driver.navigate().to(baseUrl + "user/login");
        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");

        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();

        driver.navigate().to(baseUrl + "clinic-admin/requests-for-holiday-or-time-off");

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rowsBefore = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) #requests-for-holiday-approve > .mat-button-wrapper")).click();

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedConfirmButton();

        listRequestForTimeOffOrHolidayPage.getConfirmButton().click();

        listRequestForTimeOffOrHolidayPage.ensureIsNotVisibleConfirmApproveBtn();

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();
        //Thread.sleep(1000);
        List<WebElement> rows = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }

    @Test
    public void testRejectHolidayOrTimeOffDoctorSuccess() throws InterruptedException {
        driver.navigate().to(baseUrl + "user/login");

        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");
        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();

        driver.navigate().to(baseUrl + "clinic-admin/requests-for-holiday-or-time-off");

        Thread.sleep(1000);
        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rowsBefore = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) #requests-for-holiday-reject > .mat-button-wrapper")).click();

        listRequestForTimeOffOrHolidayPage.getReasonForReject().sendKeys("We have a lot of work.");

        listRequestForTimeOffOrHolidayPage.getRejectButton().click();

        listRequestForTimeOffOrHolidayPage.ensureIsNotVisibleReason();
        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rows = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }


    @Test
    public void testApproveHolidayOrTimeOffNurseSuccess() throws InterruptedException {

        driver.navigate().to(baseUrl + "user/login");

        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");
        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();

        driver.navigate().to(baseUrl + "clinic-admin/requests-for-holiday-or-time-off");

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        listRequestForTimeOffOrHolidayPage.getSelectList().click();
        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedSelect();
        listRequestForTimeOffOrHolidayPage.getSelectNurse().click();

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rowsBefore = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) #requests-for-holiday-approve > .mat-button-wrapper")).click();
        //  driver.findElement(By.cssSelector(".mat-primary > .mat-button-wrapper")).click();
        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedConfirmButton();
        listRequestForTimeOffOrHolidayPage.getConfirmButton().click();

        listRequestForTimeOffOrHolidayPage.ensureIsNotVisibleConfirmApproveBtn();
        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rows = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }


    @Test
    public void testRejectHolidayOrTimeOffNurseSuccess() throws InterruptedException {
        driver.navigate().to(baseUrl + "user/login");
        loginPage.ensureIsDisplayedEmail();

        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");
        loginPage.getPassword().sendKeys("ClinicAdmin1");

        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();
        driver.navigate().to(baseUrl + "clinic-admin/requests-for-holiday-or-time-off");

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        listRequestForTimeOffOrHolidayPage.getSelectList().click();
        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedSelect();
        listRequestForTimeOffOrHolidayPage.getSelectNurse().click();

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rowsBefore = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) #requests-for-holiday-reject > .mat-button-wrapper")).click();

        listRequestForTimeOffOrHolidayPage.getReasonForReject().sendKeys("We have a lot of work.");

        listRequestForTimeOffOrHolidayPage.getRejectButton().click();
        listRequestForTimeOffOrHolidayPage.ensureIsNotVisibleReason();

        listRequestForTimeOffOrHolidayPage.ensureIsDisplayedTable();

        List<WebElement> rows = listRequestForTimeOffOrHolidayPage.getTable().findElements(By.tagName("tr"));

        Assertions.assertEquals(rowsBefore.size() - 1, rows.size());
        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }
}
