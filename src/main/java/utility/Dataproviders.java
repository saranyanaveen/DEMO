package utility;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.opencsv.exceptions.CsvException;

public class Dataproviders {

    @DataProvider(name = "LoginData")
    public Object[][] loginDataprovider() throws IOException, CsvException {
       
        List<String[]> csvData = CSVUtils.readCSV("./src/test/resources/DemoData.csv");
        
        // Convert List<String[]> to Object[][]
        Object[][] result = new Object[csvData.size()][3];
        for (int i = 0; i < csvData.size(); i++) {
            result[i] = csvData.get(i);
        }
        
        return result;
    }
}
