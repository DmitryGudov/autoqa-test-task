package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

public class DocumentsPage {

    private static final String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");

    private SelenideElement filtersInputLegalEntityField;
    private SelenideElement notFoundLegalEntity;


    public DocumentsPage() {
        filtersInputLegalEntityField = $x("//*[@data-qa='documents-registry-table-filter-row-legal-entities-select']//input");
        notFoundLegalEntity = $x("//div[contains(@class, 'ng-option-disabled')]");
    }

    public void openPage() {
        open(documentsPageUrl);
    }

    public void searchLegalEntity(String legalEntityName) {
        filtersInputLegalEntityField.setValue(legalEntityName).shouldBe(Condition.visible);
        sleep(1500);
    }

    public void notFoundLegalEntity() {
        notFoundLegalEntity.shouldBe(Condition.visible);
    }

    public String notFoundLegalEntityString() {
        return notFoundLegalEntity.getText().replaceAll("\\s+", "").trim();
    }

}
