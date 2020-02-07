package ftn.tim16.ClinicalCentreSystem.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListRequestsToRegisterE2ETest {
    public static final String BASE_URL = "http://localhost:4200";

    private WebDriver driver;

    private LoginPage loginPage;
    private ListRequestsToRegisterPage listRequestsToRegisterPage;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.navigate().to(BASE_URL + "/user/login");

        driver.manage().window().maximize();

        loginPage = PageFactory.initElements(driver, LoginPage.class);
        listRequestsToRegisterPage = PageFactory.initElements(driver, ListRequestsToRegisterPage.class);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testApproveRequestToRegisterSuccess() {
        loginPage.ensureIsDisplayedEmail();
        loginPage.getEmail().sendKeys("1st.Admin@maildrop.cc");
        loginPage.getPassword().sendKeys("1st.Admin");
        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();

        listRequestsToRegisterPage.ensureIsDisplayedRequestsToRegisterTable();
        List<WebElement> tableBefore = listRequestsToRegisterPage.getRequestsToRegisterTable().findElements(By.tagName("tr"));

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) #list-request-to-register-approve-btn")).click();

        listRequestsToRegisterPage.ensureIsDisplayedConfirmApproveBtn();
        listRequestsToRegisterPage.getConfirmApproveBtn().click();
        listRequestsToRegisterPage.ensureIsNotVisibleConfirmApproveBtn();

        listRequestsToRegisterPage.ensureIsDisplayedRequestsToRegisterTable();
        List<WebElement> tableAfter = listRequestsToRegisterPage.getRequestsToRegisterTable().findElements(By.tagName("tr"));
        assertEquals(tableBefore.size() - 1, tableAfter.size());

        loginPage.getLogoutBtnClinicalCentreAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicalCentreAdmin();
    }

    @Test
    public void testRejectRequestToRegisterSuccess() {
        loginPage.ensureIsDisplayedEmail();
        loginPage.getEmail().sendKeys("1st.Admin@maildrop.cc");
        loginPage.getPassword().sendKeys("1st.Admin");
        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();

        listRequestsToRegisterPage.ensureIsDisplayedRequestsToRegisterTable();
        List<WebElement> tableBefore = listRequestsToRegisterPage.getRequestsToRegisterTable().findElements(By.tagName("tr"));

        driver.findElement(By.cssSelector(".mat-row:nth-child(1) #list-request-to-register-reject-btn")).click();

        listRequestsToRegisterPage.ensureIsDisplayedReason();
        listRequestsToRegisterPage.getReason().sendKeys("Some reason");

        listRequestsToRegisterPage.getConfirmRejectBtn().click();
        listRequestsToRegisterPage.ensureIsNotVisibleReason();

        listRequestsToRegisterPage.ensureIsDisplayedRequestsToRegisterTable();
        List<WebElement> tableAfter = listRequestsToRegisterPage.getRequestsToRegisterTable().findElements(By.tagName("tr"));
        assertEquals(tableBefore.size() - 1, tableAfter.size());

        loginPage.getLogoutBtnClinicalCentreAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicalCentreAdmin();
    }

    @Test
    public void testRequestsToRegisterPageUnauthorized() {
        loginPage.ensureIsDisplayedEmail();
        loginPage.getEmail().sendKeys("ClinicAdmin1@maildrop.cc");
        loginPage.getPassword().sendKeys("ClinicAdmin1");
        loginPage.getLoginBtn().click();
        loginPage.ensureIsNotVisibleLoginBtn();

        driver.navigate().to(BASE_URL + "/clinical-centre-admin/requests-to-register");

        assertEquals(BASE_URL + "/error/non-authorized", driver.getCurrentUrl());

        loginPage.getLogoutBtnClinicAdminBtn().click();
        loginPage.ensureIsNotVisibleLogoutBtnClinicAdmin();
    }

}
