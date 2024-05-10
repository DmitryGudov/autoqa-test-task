package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class HandBooksPage {

    private SelenideElement handBooksIcon;
    private SelenideElement legalEntityRegistryPage;
    private ElementsCollection legalEntityRegistryRows;

    public HandBooksPage() {
        handBooksIcon = $x("//a[@href='/hr/legal-entities']");
        legalEntityRegistryPage = $x("//div[@class='legal-entities-catalogs-header']");
        legalEntityRegistryRows = $$x("//*[@data-qa='legal-entities-catalog-table-body-open-legal-entity-editing-dialog-button']");
    }

    public void clickHandBooksIcon() {
        handBooksIcon.click();
        legalEntityRegistryPage.shouldBe(visible);
    }

    public boolean isTextLegalEntityRegistryRows(String text) {
        ElementsCollection options = legalEntityRegistryRows;
        return options.stream().anyMatch(option -> option.getText().contains(text));
    }

}
