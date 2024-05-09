package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.fail;

public class ApplicationsPage {
    private static final String applicationsPageUrl = ConfigManager.getProperty("applicationsPageUrl");

    private SelenideElement filtersInputEmployeeField;
    private SelenideElement notFoundEmployee;

    public ApplicationsPage() {
        filtersInputEmployeeField = $x("//*[@data-qa='applications-registry-table-filter-row-employees-select']//input");
        notFoundEmployee = $x("//div[contains(@class, 'ng-option-disabled')]");

    }

    public void openPage() {
        open(applicationsPageUrl);
    }

    public void searchEmployee(String employee) {
        openPage();
        int retryLimit = 60;
        int retryInterval = 1000;
        int retries = 0;

        while (retries < retryLimit && !filtersInputEmployeeField.is(Condition.visible)) {
            sleep(retryInterval);
            retries++;
        }

        if (filtersInputEmployeeField.is(Condition.visible)) {
            filtersInputEmployeeField.setValue(employee);
        } else {
            fail("Поле для ввода сотрудника не появилось в течение ожидаемого времени.");
        }
    }

    public void notFoundEmployee() {
        notFoundEmployee.shouldBe(Condition.visible);
    }

    public String notFoundEmployeeString() {
        return notFoundEmployee.getText().replaceAll("\\s+", "").trim();
    }
}
