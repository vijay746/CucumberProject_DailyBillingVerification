package Base;

import helper.fileMapping;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseUtil {
    public static String  workingDir = System.getProperty("user.dir");
    public static WebDriver driver;
    public static fileMapping datafile;
    public static WebDriver getDriver(){
        if (driver == null){
            try {
                datafile = new fileMapping(workingDir + "\\config\\Configuration.properties");
                String chromeDriverLoc = workingDir + datafile.getData("chromeDriverPath");
                System.setProperty("webdriver.chrome.driver", chromeDriverLoc);
                driver = new ChromeDriver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return driver;
    }
}
