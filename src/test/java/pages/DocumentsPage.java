package pages;

import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

import static com.codeborne.selenide.Selenide.*;

public class DocumentsPage {
    private String documentsPageUrl;

    public DocumentsPage() {
        documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");
    }

    private SelenideElement getFilterInputLegalEntityField() {
        return $x("//*[@data-qa='documents-registry-table-filter-row-legal-entities-select']");
    }

    private SelenideElement getFilterInputEmployeeField() {
        return $x("//*[@data-qa='documents-registry-table-filter-row-employees-select']//input");
    }

    private SelenideElement getFirstEmployeeOnTheList() {
        return $x("//div[contains(@class,'employee-select__column')])[1]");
    }

    private SelenideElement getDropdownListLegalEntityField() {
        return $x("//ng-dropdown-panel[@id='ac46fe5281e3']");
    }

    private SelenideElement getDropdownListEmployeeField() {
        return $x("//ng-dropdown-panel[@id='a3b6566b59a3']");
    }

    public void openPage() {
        open(documentsPageUrl);
    }

    public void searchEmployee(String name) {
        getFilterInputEmployeeField().setValue(name);
        getFirstEmployeeOnTheList().click();
    }

}
