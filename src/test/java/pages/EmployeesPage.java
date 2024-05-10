package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class EmployeesPage {

    private SelenideElement registerOfEmployeesIcon;
    private SelenideElement employeeNameFilterInput;
    private SelenideElement noEmployeesToDisplay;
    private ElementsCollection employeesRegistryRows;

    public EmployeesPage() {
        registerOfEmployeesIcon = $x("//a[@href='/hr/employees']");
        employeeNameFilterInput = $x("//*[@data-qa='registry-header-name-input']");
        noEmployeesToDisplay = $x("//div[contains(@class, 'empty')]");
        employeesRegistryRows = $$x("//employees-registry-row]");
    }

    public void clickRegisterOfEmployeesIcon() {
        registerOfEmployeesIcon.click();
    }

    public void searchEmployee(String employee) {
        clickRegisterOfEmployeesIcon();
        employeeNameFilterInput.setValue(employee).shouldBe(Condition.visible);
        sleep(5000);
    }

    public void noEmployeesToDisplay() {
        noEmployeesToDisplay.shouldBe(Condition.visible);
    }

    public boolean noEmployeesToDisplayVisible() {
        return noEmployeesToDisplay.is(Condition.visible);
    }

    public String noEmployeesToDisplayString() {
        return noEmployeesToDisplay.getText();
    }

    public boolean isTextEmployeesRegistryRows(String text) {
        ElementsCollection options = employeesRegistryRows;
        return options.stream().anyMatch(option -> option.getText().contains(text));
    }

}
