import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarGurusTest {



    @Test
    public void testCarsGuru() throws InterruptedException {

     ChromeOptions options = new ChromeOptions();
     options.addArguments("--remote-allow-origins=*");

     WebDriver driver = new ChromeDriver(options);
    ;
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        //1. Navigate to cargurus.com
        driver.get("https://www.cargurus.com/");
        // returns the Title of the page
        String actualTitle = driver.getTitle();
        String expectedTitle = "Buy & Sell Cars: Reviews, Prices, and Financing - CarGurus";
        Assert.assertEquals(actualTitle, expectedTitle, "Wrong page.");

        //2. Click on Buy Used
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[@id=\"heroSearch\"]/label[2]")).click();

        //3. Verify that the default selected option in Makes dropdown is All Makes (Use Assert
        //methods for all verifications)
        Thread.sleep(5000);
        String allMakes = String.valueOf(driver.findElement(By.xpath("//*[@id=\"carPickerUsed_makerSelect\"]/option[1]")).getText());
        Assert.assertEquals(allMakes, "All Makes", "Expected: \"All Makes\"" + ", Actual: " + allMakes + ".");

        //4. In Makes dropdown, choose Lamborghini
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[@id=\"carPickerUsed_makerSelect\"]/optgroup[2]/option[52]")).click();

        //5. Verify that the default selected option in Models dropdown is All Models
        Thread.sleep(5000);
        String allModels = String.valueOf(driver.findElement(By.xpath("//*[@id=\"carPickerUsed_modelSelect\"]/option[1]")).getText());
        Assert.assertEquals(allModels, "All Models", "Expected: \"All Models\"" + ", Actual: " + allModels + ".");

        //6. Verify that Models dropdown options are [All Models, Aventador, Huracan, Urus,
        //400GT, Centenario, Countach, Diablo, Espada, Gallardo, Murcielago]
        Thread.sleep(5000);
        List<String> expectedListOfModels = Arrays.asList("All Models", "Aventador", "Gallardo", "Huracan", "Urus",
                "400GT", "Centenario", "Countach", "Diablo", "Espada", "Murcielago");

        WebElement dropDownModel = driver.findElement(By.id("carPickerUsed_modelSelect"));

        Select s = new Select(dropDownModel);
        List<WebElement> op = s.getOptions();
        List<String> all = new ArrayList<>();
        for (WebElement e : op) {
            all.add(e.getText());
        }
      Assert.assertEquals(all, expectedListOfModels, "The lists are not the same.");

        //7. In Models dropdown, choose Gallardo
        new Select(driver.findElement(By.id("carPickerUsed_modelSelect"))).selectByVisibleText("Gallardo");

       // 8. Enter 22031 for zip and hit search

        driver.findElement(By.id("dealFinderZipUsedId_dealFinderForm")).sendKeys("22031", Keys.ENTER);


        Thread.sleep(1000);
        List <WebElement> elements = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));
        if (elements.size()==0){
            throw new RuntimeException("List is empty");
        }else{
            System.out.println("There are " + elements.size() + " search results in this page.");
        }

        Assert.assertEquals(elements.size(), 15);

       // 10. Verify that all 15 result's title text contains "Lamborghini Gallardo"

        Thread.sleep(1000);
        List <WebElement> titleElements = driver.findElements(By.xpath("//h4[@class='vO42pn']/ancestor::a[not(contains(@href, 'FEATURED'))]"));
        for (WebElement titleElement: titleElements){
            Assert.assertTrue(titleElement.getText().contains("Lamborghini Gallardo"), "The title contains Lamborghini Gallardo");
        }
//        11. From the dropdown on the left corner choose “Lowest price first” option and verify that
//        all 15 results are sorted from lowest to highest. You should exclude the first result since
//        it will not be a part of sorting logic.
//                To verify correct sorting, collect all 15 prices into a list, create a copy of it and sort in
//        ascending order and check the equality of the sorted copy with the original

        new Select(driver.findElement(By.id("sort-listing"))).selectByVisibleText("Lowest price first");
        Thread.sleep(2000);
        List <WebElement> lpfElements =driver.findElements(By.xpath("//span[@class='JzvPHo']"));
        List <Double> prices =new ArrayList<>();
        for (int i=1; i<lpfElements.size(); i++){
            String price = lpfElements.get(i).getText();
            price = price.split(" ")[0];
            prices.add(Double.parseDouble(price.replace("$","").replace(",", "")));

        }
        Thread.sleep(1000);
        for (int i=1; i< prices.size(); i++){
            Assert.assertTrue(prices.get(i-1)<= prices.get(i), "Checking result are sorted in ascending order.");
        }
//        12. From the dropdown menu, choose “Highest mileage first” option and verify that all 15
//        results are sorted from highest to lowest. You should exclude the first result since it will
//        not be a part of sorting logic.

        new Select(driver.findElement(By.id("sort-listing"))).selectByVisibleText("Highest mileage first");
        Thread.sleep(2000);
        List <WebElement> hmfElements =driver.findElements(By.xpath("//p[@class='JKzfU4 umcYBP']"));
        List <Double> mileages =new ArrayList<>();
        for (int i=1; i<hmfElements.size(); i++){
            String mileage = hmfElements.get(i).getText();
            mileages.add(Double.parseDouble(mileage.replace(" mi","").replace(",", "")));

        }
        Thread.sleep(1000);
        for (int i=1; i< mileages.size(); i++){
            Assert.assertTrue(mileages.get(i-1)>= mileages.get(i), "Checking result are sorted in descending order.");
        }
        Thread.sleep(2000);
        WebElement awdCheckbox = driver.findElement(By.xpath("//input[@value='Coupe AWD']"));
        Thread.sleep(2000);
        if (!(awdCheckbox.isSelected())){
            driver.findElement(By.xpath("//input[@value='Coupe AWD']/following-sibling::p")).click();
        }


        //13. On the left menu, click on Coupe AWD checkbox and verify that all results on the page
        //contains “Coupe AWD

        Thread.sleep(5000);
        List <WebElement> awdElements = driver.findElements(By.xpath("//h4[@class='vO42pn']"));
        for (int i=0; i<awdElements.size(); i++){
            Assert.assertTrue(awdElements.get(i).getText().contains("Coupe AWD"), "The title contains Coupe AWD");
        }
        Thread.sleep(2000);
        awdElements.get(awdElements.size()-1).click();

       //14. Click on the last result (get the last result dynamically, i.e., your code should click on the
        //last result regardless of how many results are there)

        driver.findElement(By.xpath("//button[@class='r1inOn']")).click();
        Thread.sleep(2000);
        List <WebElement> images = driver.findElements(By.xpath("//img[@class='C6f2e2 bmTmAy']"));

        //15. Once you are in the result details page go back to the results page and verify that the
        //clicked result has “Viewed” text on it.

        String viewed = images.get(images.size()-1).findElement(By.xpath("//parent ::div/following-sibling::div/p")).getText(); //find the "Viewed" of the last search result
        Assert.assertEquals(viewed, "Viewed", "Not viewd");

        driver.quit();


    }
}



