package ftn.tim16.ClinicalCentreSystem.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"email\"]")
    private WebElement email;

    @FindBy(xpath = "//*[@id=\"password\"]")
    private WebElement password;

    @FindBy(xpath = "//*[@id=\"login-btn\"]")
    private WebElement loginBtn;

    @FindBy(xpath = "//*[@id=\"btn-logout-clinicalCentreAdmin\"]")
    private WebElement logoutBtnClinicalCentreAdminBtn;

    @FindBy(xpath = "//*[@id=\"btn-logOut-clinicAdmin\"]")
    private WebElement logoutBtnClinicAdminBtn;

    public LoginPage() {
    }

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureIsDisplayedEmail() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(email));
    }

    public void ensureIsNotVisibleLoginBtn() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("login-btn")));
    }

    public void ensureIsNotVisibleLogoutBtnClinicalCentreAdmin() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn-logOut-clinicalCentreAdmin")));
    }

    public void ensureIsNotVisibleLogoutBtnClinicAdmin() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn-logOut-clinicAdmin")));
    }

    public WebElement getEmail() {
        return email;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getLoginBtn() {
        return loginBtn;
    }

    public WebElement getLogoutBtnClinicalCentreAdminBtn() {
        return logoutBtnClinicalCentreAdminBtn;
    }

    public WebElement getLogoutBtnClinicAdminBtn() {
        return logoutBtnClinicAdminBtn;
    }
}
