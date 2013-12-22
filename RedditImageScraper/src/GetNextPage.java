import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GetNextPage {

	public static void main(String[] args) {
		new GetNextPage().next();
	}

	private void next() {
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.reddit.com/r/emmawatson/");
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		WebElement element = driver
				.findElement(By
						.xpath("//span[@class='nextprev']/a[contains(text(), 'next')]"));
		element.click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		System.out.println(driver.getCurrentUrl());

	}
}
