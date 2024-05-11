package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class EmployeesPage {

    private SelenideElement registryOfEmployeesIcon;
    private SelenideElement employeeNameFilter;
    private SelenideElement noEmployeesToDisplay;
    private ElementsCollection employeesRegistryRows;

    public EmployeesPage() {
        registryOfEmployeesIcon = $x("//a[@href='/hr/employees']");
        employeeNameFilter = $x("//*[@data-qa='registry-header-name-input']");
        noEmployeesToDisplay = $x("//div[contains(@class, 'empty')]");
        employeesRegistryRows = $$x("//employees-registry-row]");
    }

    public void clickRegistryOfEmployeesIcon() {
        registryOfEmployeesIcon.click();
    }

    public void searchEmployee(String employee) {
        clickRegistryOfEmployeesIcon();
        employeeNameFilter.setValue(employee).shouldBe(Condition.visible);
        sleep(5000);
    }

    public void noEmployeesToDisplay() {
        noEmployeesToDisplay.shouldBe(Condition.visible);
    }

    public boolean noEmployeesToDisplayIsVisible() {
        return noEmployeesToDisplay.is(Condition.visible);
    }

    public String noEmployeesToDisplayText() {
        return noEmployeesToDisplay.getText();
    }

    public boolean isTextEmployeesRegistryRows(String legalEntity) {
        ElementsCollection options = employeesRegistryRows;
        return options.stream().anyMatch(option -> option.getText().contains(legalEntity));
    }

}
