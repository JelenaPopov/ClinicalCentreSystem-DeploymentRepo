package ftn.tim16.ClinicalCentreSystem.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ListRequestForTimeOffOrHolidayPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"requests-for-holiday-approve\"]")
    private WebElement approve;

    @FindBy(xpath = "//*[@id=\"requests-for-holiday-reject\"]")
    private WebElement reject;

    @FindBy(xpath = "//*[@id=\"requests-to-register-table\"]")
    private WebElement table;

    @FindBy(xpath = "//*[@id=\"reason-for-reject-holiday\"]")
    private WebElement reasonForReject;

    @FindBy(xpath = "//*[@id=\"reject-request-for-holiday\"]")
    private WebElement rejectButton;

    @FindBy(xpath = "//*[@id=\"selectNurseRequestForHoliday\"]")
    private WebElement selectList;

    @FindBy(xpath = "//*[@id=\"chosenNurseRequestForHoliday\"]")
    private WebElement selectNurse;

    @FindBy(xpath = "//*[@id=\"confirmDialogApproveRequest\"]")
    private WebElement confirmButton;

    public ListRequestForTimeOffOrHolidayPage() {
    }

    public ListRequestForTimeOffOrHolidayPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureIsDisplayedApprove() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(approve));
    }

    public void ensureIsDisplayedTable() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(table));
    }

    public void ensureIsDisplayedSelect() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(selectList));
    }

    public void ensureIsDisplayedConfirmButton() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(confirmButton));
    }

    public void ensureIsNotVisibleConfirmApproveBtn() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("confirmDialogApproveRequest")));
    }

    public void ensureIsNotVisibleReason() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("reason-for-reject-holiday")));
    }

    public WebElement getTable() {
        return table;
    }

    public WebElement getApprove() {
        return approve;
    }

    public WebElement getReject() {
        return reject;
    }

    public WebElement getReasonForReject() {
        return reasonForReject;
    }

    public WebElement getRejectButton() {
        return rejectButton;
    }

    public WebElement getSelectList() {
        return selectList;
    }

    public WebElement getSelectNurse() {
        return selectNurse;
    }

    public WebElement getConfirmButton() {
        return confirmButton;
    }


}
