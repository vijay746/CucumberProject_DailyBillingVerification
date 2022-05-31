package helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * File to create mapping of respective Config file to be used in project
 */
public class fileMapping {
    Properties properties;
    public fileMapping(String FilePath) {
        try {
            FileInputStream Locator = new FileInputStream(FilePath);
            properties = new Properties();
            properties.load(Locator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getData(String ElementName)  {
        return properties.getProperty(ElementName);
    }
}