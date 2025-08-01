package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTests extends BaseTest {


    @Test
    public void testInvalidLoginShowErrorMsg() {

        LoginPage login = new LoginPage(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        } catch (Exception e) {
            Assert.fail("Login form did not load. Page may be stuck on notification warning.");
        }

        login.enterEmail("wrong@mail.com");
        login.enterPassword("wrong123");
        login.clickLogin();

        String error = login.getErrorMessage();
        System.out.println("Error Message: " + error);
        Assert.assertTrue(error.toLowerCase().contains("notification"), "Error message did not contain 'notification'");
    }

    @Test
    public void testLoginButtonDisabledWhenFieldAreEmpty() {
        LoginPage login = new LoginPage(driver);

        login.clickLogin(); // Try to login with empty fields

        String error = login.getErrorMessage();
        Assert.assertFalse(error.isEmpty(), "Expected error message when trying to login with empty fields.");
    }



    @Test
    public void testLoginWithRandomCredentials() {
        LoginPage login = new LoginPage(driver);
        login.enterEmail("random@mail.com");
        login.enterPassword("wrongPass123");
        login.clickLogin();

        String error = login.getErrorMessage();
        System.out.println("Error Message (random credentials): " + error);

        Assert.assertTrue(
                !error.isEmpty() &&
                        (error.toLowerCase().contains("invalid") || error.toLowerCase().contains("error")),
                "Expected an error message for random credentials.");
    }

    @Test
    public void testPasswordMaskedButton() {

        LoginPage login = new LoginPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        login.enterPassword("testPassword");

        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        String typeBefore = passwordInput.getAttribute("type");
        System.out.println("Type before toggle: " + typeBefore);

        // Ensure password starts as masked
        Assert.assertEquals(typeBefore, "password", "Password should be masked initially.");

        // Toggle visibility
        login.togglePasswordVisibility();

        // Re-fetch element in case DOM updated after toggle
        WebElement passwordInputAfter = driver.findElement(By.name("password"));
        String typeAfter = passwordInputAfter.getAttribute("type");
        System.out.println("Type after toggle: " + typeAfter);

        Assert.assertEquals(typeAfter, "text", "Password should be visible after clicking eye icon.");
    }

    @Test
    public void testLoginPageElementsPresence() {
        Assert.assertTrue(driver.findElement(By.name("email")).isDisplayed(), "Email field not found.");
        Assert.assertTrue(driver.findElement(By.name("password")).isDisplayed(), "Password field not found.");
        Assert.assertTrue(driver.findElement(By.cssSelector("button[type='submit']")).isDisplayed(), "Login button not found.");
    }


}
