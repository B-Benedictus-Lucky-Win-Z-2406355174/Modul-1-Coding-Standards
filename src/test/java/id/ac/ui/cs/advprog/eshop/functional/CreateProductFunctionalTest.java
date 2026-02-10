package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws Exception {
        // Navigate to Create Product page
        driver.get(baseUrl + "/product/create");

        // Fill in the product form
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.clear();
        nameInput.sendKeys("Sampo Cap Bambang");

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.clear();
        quantityInput.sendKeys("100");

        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify we are redirected to the product list page
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Sampo Cap Bambang"));
        assertTrue(pageSource.contains("100"));
    }

    @Test
    void createProduct_appearsInProductList(ChromeDriver driver) throws Exception {
        // Navigate to Create Product page
        driver.get(baseUrl + "/product/create");

        // Fill in the product form
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.clear();
        nameInput.sendKeys("Sabun Mandi Cair");

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.clear();
        quantityInput.sendKeys("50");

        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify the product appears in the product list
        // After submission, should be redirected to /product/list
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product/list"));

        // Verify product data is shown in the table
        WebElement productTable = driver.findElement(By.tagName("table"));
        String tableText = productTable.getText();
        assertTrue(tableText.contains("Sabun Mandi Cair"));
        assertTrue(tableText.contains("50"));
    }

    @Test
    void createProduct_pageTitle_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/product/create");
        String pageTitle = driver.getTitle();
        assertEquals("Create New Product", pageTitle);
    }
}
