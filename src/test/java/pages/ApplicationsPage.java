package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ApplicationsPage {

    private SelenideElement registryOfApplicationsIcon;
    private SelenideElement noApplications;
    private SelenideElement applicationsRow;
    private ElementsCollection applicationsRows;


    public ApplicationsPage() {
        registryOfApplicationsIcon = $x("//a[@href='/hr/applications']");
        noApplications = $x("//div[contains(@class, 'body')]");
        applicationsRow = applicationsRows.first();
        applicationsRows = $$x("//div[contains(@class, 'row')]");
    }

    public void clickRegistryOfApplicationsIcon() {
        registryOfApplicationsIcon.click();
        sleep(5000);
    }

    public boolean rowsIsVisible() {
        return applicationsRow.is(visible);
    }

    public boolean verifyNoTextInRows(String employee, String legalEntity) {
        for (SelenideElement row : applicationsRows) {
            if (row.getText().contains(employee) || row.getText().contains(legalEntity)) {
                return false;
            }
        }
        return true;
    }

    public String noApplicationText() {
        return noApplications.getText();
    }

}
