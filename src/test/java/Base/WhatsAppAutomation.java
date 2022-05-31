package Base;


import net.bytebuddy.asm.Advice;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

public class WhatsAppAutomation extends BaseUtil{

    public void connectWhatsApp() throws InterruptedException {
        LoginPage lpOb = new LoginPage();
        String pgUrl = "https://web.whatsapp.com/";           // datafile.getData("paymentGatewayUrl");
        lpOb.navigateToUrl(pgUrl);
        Thread.sleep(6000);
        // WebDriverWait(driver, 600);
    }

    public String getData(){
        String target = "Deepti";
        String msg = "Message sent using Automation!!!";
        return target+","+msg;
    }



    By inp_xpath = By.xpath("//div[@class=\"input\"][@dir=\"auto\"][@data-tab=\"1\"]");

    public void sendDataToCustomer(String target, String msg) throws InterruptedException {

        By targetPerson = By.xpath("//span[contains(@title,' + target + ')]");

        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetPerson));
        getDriver().findElement(targetPerson).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(inp_xpath));
        getDriver().findElement(inp_xpath).sendKeys(msg);
        // getDriver().findElement(inp_xpath).send_keys(Keys.ENTER);

        Thread.sleep(2000);
    }
}



