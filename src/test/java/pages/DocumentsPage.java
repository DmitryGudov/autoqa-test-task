package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class DocumentsPage {

    private SelenideElement sideFilter;
    private SelenideElement employeeFilterInput;
    private SelenideElement notFoundEmployee;
    private SelenideElement legalEntityFilterInput;
    private SelenideElement notFoundLegalEntity;
    private ElementsCollection employeeList;

    public DocumentsPage() {
        sideFilter = $x("//button[@data-qa='side-drawer-button']");
        employeeFilterInput = $x("//*[@data-qa='documents-filters-form-employees-select']//input");
        notFoundEmployee = $x("//employee-select//div[@role='listbox']//div[contains(text(), 'Не найдено')]");
        legalEntityFilterInput = $x("//*[@data-qa='documents-registry-table-filter-row-legal-entities-select']//input");
        notFoundLegalEntity = $x("//div[@class='documents-registry-table-filter-row__legal-entity-cell']//div[@role='listbox']//div[contains(text(), 'Не найдено')]");
        employeeList = $$x("//employee-select//div[@role='listbox']//div[contains(@class, 'ng-option')]");
    }

    public void clickSideFilter() {
        sideFilter.click();
    }

    public void searchEmployee(String employee) {
        employeeFilterInput.setValue(employee).shouldBe(Condition.visible);
        sleep(3000);
    }

    public boolean notFoundEmployeeVisible() {
        return notFoundEmployee.is(Condition.visible);
    }

    public void notFoundEmployee() {
        notFoundEmployee.shouldBe(Condition.visible);
    }

    public String notFoundEmployeeString() {
        return notFoundEmployee.getText().replaceAll("\\s+", "").trim();
    }

    public boolean isTextInEmployeeList(String text) {
        ElementsCollection options = employeeList;
        return options.stream().anyMatch(option -> option.getText().contains(text));
    }

    public void searchLegalEntity(String legalEntityName) {
        legalEntityFilterInput.setValue(legalEntityName).shouldBe(Condition.visible);
        sleep(1500);
    }

    public void notFoundLegalEntity() {
        notFoundLegalEntity.shouldBe(Condition.visible);
    }

    public String notFoundLegalEntityString() {
        return notFoundLegalEntity.getText().replaceAll("\\s+", "").trim();
    }

}
