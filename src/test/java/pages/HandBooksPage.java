package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class HandBooksPage {

    private SelenideElement handBooksIcon;
    private ElementsCollection legalEntityRegistryRows;


    public void HandBooksPage() {
        handBooksIcon = $x("//a[@href='/hr/legal-entities']");
        legalEntityRegistryRows = $$x("//legal-entities-catalog-table-body__row]");
    }

    public void clickHandBooksIcon() {
        handBooksIcon.click();
    }

    public boolean isTextLegalEntityRegistryRows(String text) {
        ElementsCollection options = legalEntityRegistryRows;
        return options.stream().anyMatch(option -> option.getText().contains(text));
    }

}
