package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@CucumberOptions(
         // features = {"src/test/java/features/Login.feature"} ,  // for running using TestRunner :: java/features/Login.feature
        features = {"java/features/Login.feature"} ,
        plugin = {"pretty"},
        glue = "java/steps")

public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeClass
    public void setUp(){
    }

    @AfterClass
    public void TearDownTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // getDriver().quit();
    }
}
