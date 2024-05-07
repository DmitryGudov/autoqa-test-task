package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoginPage {

    private Properties properties;
    private String loginPageUrl;

    public LoginPage() {
        properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/java/resources/config.properties");
            properties.load(input);
            input.close();
            loginPageUrl = properties.getProperty("loginPageUrl");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
