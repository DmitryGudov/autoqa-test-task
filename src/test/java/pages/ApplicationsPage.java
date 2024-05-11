package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.fail;

public class ApplicationsPage {

    private SelenideElement registryOfApplicationsIcon;
    private SelenideElement employeeFilter;
    private SelenideElement notFoundEmployee;

    public ApplicationsPage() {
        registryOfApplicationsIcon = $x("//a[@href='/hr/applications']");
        employeeFilter = $x("//*[@data-qa='applications-registry-table-filter-row-employees-select']//input");
        notFoundEmployee = $x("//div[contains(@class, 'ng-option-disabled')]");
    }

    public void clickRegistryOfApplicationsIcon() {
        registryOfApplicationsIcon.click();
    }

    public void searchEmployee(String employee) {
        clickRegistryOfApplicationsIcon();

        int retryLimit = 120;
        int retryInterval = 500;
        int retries = 0;

        while (retries < retryLimit && !employeeFilter.is(Condition.visible)) {
            sleep(retryInterval);
            retries++;
        }

        if (employeeFilter.is(Condition.visible)) {
            employeeFilter.setValue(employee);
        } else {
            fail("Поле для ввода сотрудника не появилось в течение ожидаемого времени!");
        }
    }

    public void notFoundEmployee() {
        notFoundEmployee.shouldBe(Condition.visible);
    }

    public String notFoundEmployeeText() {
        return notFoundEmployee.getText().replaceAll("\\s+", "").trim();
    }

}
