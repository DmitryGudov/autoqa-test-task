package pages;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;
import managers.ConfigManager;

public class LoginPage {

    private static final String loginPageUrl = ConfigManager.getProperty("loginPageUrl");

    private SelenideElement loginInput;
    private SelenideElement passwordInput;
    private SelenideElement loginButton;
    private SelenideElement hrLinkLogo;

    public LoginPage() {
        loginInput = $x("//input[@data-qa='credential-form-login-input']");
        passwordInput = $x("//input[@data-qa='credential-form-password-input']");
        loginButton = $x("//button[@data-qa='credential-form-submit-button']");
        hrLinkLogo = $x("//a[@class='ng-star-inserted']");
    }

    public void openPage() {
        open(loginPageUrl);
    }

    public void enterEmail(String email) {
        loginInput.setValue(email);
    }

    public void enterPassword(String password) {
        passwordInput.setValue(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public void login(String email, String password) {
        openPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        hrLinkLogo.shouldBe(visible);
    }

}
