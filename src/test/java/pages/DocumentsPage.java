package pages;

import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

import static com.codeborne.selenide.Selenide.$x;

public class DocumentsPage {
    private String documentsPageUrl;

    public DocumentsPage() {
        documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");
    }

    private SelenideElement getFilterInputLegalEntityField() {
        return $x("//input[@data-qa='documents-registry-table-filter-row-legal-entities-select']");
    }

    private SelenideElement getDropdownListLegalEntityField() {
        return $x("//ng-dropdown-panel[@id='ac46fe5281e3']");
    }

}
