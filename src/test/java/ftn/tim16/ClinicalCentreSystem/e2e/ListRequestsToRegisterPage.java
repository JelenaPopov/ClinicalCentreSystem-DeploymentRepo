package ftn.tim16.ClinicalCentreSystem.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ListRequestsToRegisterPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"list-request-to-register-table\"]")
    private WebElement requestsToRegisterTable;

    @FindBy(xpath = "//*[@id=\"approve-request-to-register-confirm-btn\"]")
    private WebElement confirmApproveBtn;

    @FindBy(xpath = "//*[@id=\"reject-request-to-register-reason\"]")
    private WebElement reason;

    @FindBy(xpath = "//*[@id=\"reject-request-to-register-confirm-btn\"]")
    private WebElement confirmRejectBtn;

    public ListRequestsToRegisterPage() {
    }

    public ListRequestsToRegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureIsDisplayedRequestsToRegisterTable() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOf(requestsToRegisterTable));
    }

    public void ensureIsDisplayedConfirmApproveBtn() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(confirmApproveBtn));
    }

    public void ensureIsNotVisibleConfirmApproveBtn() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("approve-request-to-register-confirm-btn")));
    }

    public void ensureIsDisplayedReason() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(reason));
    }

    public void ensureIsNotVisibleReason() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("reject-request-to-register-reason")));
    }

    public WebElement getRequestsToRegisterTable() {
        return requestsToRegisterTable;
    }

    public WebElement getConfirmApproveBtn() {
        return confirmApproveBtn;
    }

    public WebElement getReason() {
        return reason;
    }

    public WebElement getConfirmRejectBtn() {
        return confirmRejectBtn;
    }
}
