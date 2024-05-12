package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class DocumentsPage {

    private SelenideElement sideFilter;
    private SelenideElement employeeFilter;
    private SelenideElement notFoundEmployee;
    private ElementsCollection employeeList;
    private SelenideElement legalEntityFilter;
    private SelenideElement notFoundLegalEntity;

    public DocumentsPage() {
        sideFilter = $x("//button[@data-qa='side-drawer-button']");
        employeeFilter = $x("//*[@data-qa='documents-filters-form-employees-select']//input");
        notFoundEmployee = $x("//employee-select//div[@role='listbox']//div[contains(text(), 'Не найдено')]");
        employeeList = $$x("//employee-select//div[@role='listbox']//div[contains(@class, 'ng-option')]");
        legalEntityFilter = $x("//*[@data-qa='documents-registry-table-filter-row-legal-entities-select']//input");
        notFoundLegalEntity = $x("//div[@class='documents-registry-table-filter-row__legal-entity-cell']//div[@role='listbox']//div[contains(text(), 'Не найдено')]");
    }

    public void clickSideFilter() {
        sideFilter.click();
    }

    public void searchEmployee(String employee) {
        employeeFilter.setValue(employee).shouldBe(Condition.visible);
        sleep(3000);
    }

    public boolean notFoundEmployeeIsVisible() {
        return notFoundEmployee.is(Condition.visible);
    }

    public void notFoundEmployee() {
        notFoundEmployee.shouldBe(Condition.visible);
    }

    public String notFoundEmployeeText() {
        return notFoundEmployee.getText().replaceAll("\\s+", "").trim();
    }

    public boolean isTextInEmployeeList(String legalEntity) {
        ElementsCollection options = employeeList;
        return options.stream().anyMatch(option -> option.getText().contains(legalEntity));
    }

    public void searchLegalEntity(String legalEntityName) {
        legalEntityFilter.click();
        legalEntityFilter.setValue(legalEntityName);
        sleep(3000);
        legalEntityFilter.shouldBe(Condition.visible);
    }

    public void notFoundLegalEntity() {
        notFoundLegalEntity.shouldBe(Condition.visible);
    }

    public String notFoundLegalEntityText() {
        return notFoundLegalEntity.getText().replaceAll("\\s+", "").trim();
    }

}
