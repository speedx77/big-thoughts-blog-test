package bigthoughts;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class StandAloneTest {
	
	public static void main(String[] args) {
		
		WebDriverManager.chromedriver().setup(); //Don't need to locally donwload webdrivers
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver , Duration.ofSeconds(5)); //explicit waits
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		Actions action = new Actions(driver);
		
		
		driver.get("http://3.22.223.230/");
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("body"))));
		
		//System.out.println(driver.getTitle());
		
		//driver.close();
		
		//-----------create new thought submission test-------------
		
		//PageObjects
		WebElement title = 	driver.findElement(By.cssSelector("[name='title']"));
		WebElement submission = driver.findElement(By.cssSelector("[name='submission']"));
		WebElement tag = driver.findElement(By.cssSelector("[value='Poetry']"));
		WebElement submit = driver.findElement(By.cssSelector("[value='Submit']"));
		
		//constants to test against
		String textTitle = "Older - Searows";
		String textSubmission = "I am building something else, it's not something I will tear down"
				+ "\n" + "\n" + "I will make it myself, and I'm too proud to break that promise"
				+ "\n" + "\n" + "I'd do anything but ask for your help.";
		String tagName = driver.findElement(By.cssSelector("[value='Poetry']")).getDomAttribute("value");

		//Web element actions
		title.sendKeys(textTitle);
		submission.sendKeys(textSubmission);
		action.moveToElement(tag).click().build().perform();
		action.moveToElement(submit).click().build().perform();
	
		
		//assertions
		WebElement actualTitle = driver.findElement(By.cssSelector(".fst-italic"));
		WebElement actualSubmission = driver.findElement(By.cssSelector("[style='white-space: pre-wrap;']"));
		WebElement actualTag = driver.findElement(By.cssSelector("h4"));
		
		/*
		System.out.println(actualTitle.getText());
		System.out.println(actualSubmission.getText());
		System.out.println(actualTag.getText());
		System.out.println(tagName);
		*/
		
		Assert.assertEquals(actualTitle.getText(), textTitle);
		Assert.assertEquals(actualSubmission.getText(), textSubmission);
		Assert.assertEquals(actualTag.getText(), tagName);
		
		
		//------------------------Delete Post--------------------------------
		
		//navigate to all posts page
		driver.get("http://3.22.223.230/all-posts");
		
		//list of thought names on all posts page
		List<WebElement> thoughtNames = driver.findElements(By.cssSelector("h4"));
		
		//find post that matches entered post from first test
		WebElement selectedThought = thoughtNames.stream().filter(thought ->
		thought.getText().equalsIgnoreCase(textTitle)).findFirst().orElse(null);
		
		action.moveToElement(selectedThought).click().build().perform();
		
		//delete post
		WebElement delete = driver.findElement(By.cssSelector("[method='DELETE']"));
		action.moveToElement(delete).click().build().perform();
		
		//assertions
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("body"))));
		
		thoughtNames = driver.findElements(By.cssSelector("h4"));
		
		for (int i = 0; i < thoughtNames.size(); i++) {
			Boolean thoughtNameMatches =
					thoughtNames.stream().anyMatch(thoughtName ->
					thoughtName.getText().equalsIgnoreCase(textTitle));
			Assert.assertFalse(thoughtNameMatches);
			
		}
		
		
		
	}
	
	

}
