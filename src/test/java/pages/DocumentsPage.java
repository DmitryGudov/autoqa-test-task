package pages;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

public class DocumentsPage {

    private static final String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");
    private SelenideElement filterInputEmployeeField;
    private SelenideElement notFoundEmployee;

    public DocumentsPage() {
        filterInputEmployeeField = $x("//*[@data-qa='documents-registry-table-filter-row-employees-select']//input");
        notFoundEmployee = $x("//div[contains(@class, 'ng-option-disabled')]");
    }

    public void openPage() {
        open(documentsPageUrl);
    }

    public void searchEmployee(String name) {
        openPage();
        filterInputEmployeeField.setValue(name);
        sleep(3000);
    }

    public SelenideElement getNotFoundEmployee() {
        return notFoundEmployee.shouldBe(visible);
    }

    public String notFoundEmployeeString() {
        return notFoundEmployee.getText().replaceAll("\\s+", "").trim();
    }

}
