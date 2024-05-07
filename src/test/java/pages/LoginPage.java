package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

public class LoginPage {

    private String loginPageUrl;

    public LoginPage() {
        loginPageUrl = ConfigManager.getProperty("loginPageUrl");
    }

    private SelenideElement getLoginInput() {
        return $x("//input[@data-qa='credential-form-login-input']");
    }

    private SelenideElement getPasswordInput() {
        return $x("//input[@data-qa='credential-form-password-input']");
    }

    private SelenideElement getLoginButton() {
        return $x("//button[@data-qa='credential-form-submit-button']");
    }

    public void openPage() {
        open(loginPageUrl);
    }

    public void enterEmail(String email) {
        getLoginInput().clear();
        getLoginInput().setValue(email);
    }

    public void enterPassword(String password) {
        getPasswordInput().clear();
        getPasswordInput().setValue(password);
    }

    public void clickLoginButton() {
        getLoginButton().click();
    }

    public void login(String email, String password) {
        openPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }
}
