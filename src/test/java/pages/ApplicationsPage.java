package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.fail;

public class ApplicationsPage {

    private SelenideElement registerOfApplicationsIcon;
    private SelenideElement employeeFilterInput;
    private SelenideElement notFoundEmployee;

    public ApplicationsPage() {
        registerOfApplicationsIcon = $x("//a[@href='/hr/applications']");
        employeeFilterInput = $x("//*[@data-qa='applications-registry-table-filter-row-employees-select']//input");
        notFoundEmployee = $x("//div[contains(@class, 'ng-option-disabled')]");
    }

    public void clickRegisterOfApplicationsIcon() {
        registerOfApplicationsIcon.click();
    }

    public void searchEmployee(String employee) {
        clickRegisterOfApplicationsIcon();

        int retryLimit = 60;
        int retryInterval = 500;
        int retries = 0;

        while (retries < retryLimit && !employeeFilterInput.is(Condition.visible)) {
            sleep(retryInterval);
            retries++;
        }

        if (employeeFilterInput.is(Condition.visible)) {
            employeeFilterInput.setValue(employee);
        } else {
            fail("Поле для ввода сотрудника не появилось в течение ожидаемого времени!");
        }
    }

    public void notFoundEmployee() {
        notFoundEmployee.shouldBe(Condition.visible);
    }

    public String notFoundEmployeeString() {
        return notFoundEmployee.getText().replaceAll("\\s+", "").trim();
    }

}
