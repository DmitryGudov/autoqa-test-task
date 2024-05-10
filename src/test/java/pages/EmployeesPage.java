package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class EmployeesPage {

    private SelenideElement registerOfEmployeesIcon;
    private SelenideElement employeeNameFilterInput;
    private SelenideElement noEmployeesToDisplay;

    public EmployeesPage() {
        registerOfEmployeesIcon = $x("//a[@href='/hr/employees']");
        employeeNameFilterInput = $x("//*[@data-qa='registry-header-name-input']");
        noEmployeesToDisplay = $x("//div[contains(@class, 'empty')]");
    }

    public void clickRegisterOfEmployeesIcon() {
        registerOfEmployeesIcon.click();
    }

    public void searchEmployee(String employee) {
        clickRegisterOfEmployeesIcon();
        employeeNameFilterInput.setValue(employee).shouldBe(Condition.visible);
    }

    public void noEmployeesToDisplay() {
        noEmployeesToDisplay.shouldBe(Condition.visible);
    }

    public String noEmployeesToDisplayString() {
        return noEmployeesToDisplay.getText();
    }

}
