package pages;

import static com.codeborne.selenide.Selenide.*;

import managers.ConfigManager;

public class DocumentsPage {

    private static final String documentsPageUrl = ConfigManager.getProperty("documentsPageUrl");

    public void openPage() {
        open(documentsPageUrl);
    }

}
