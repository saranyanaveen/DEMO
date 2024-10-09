package utility;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.opencsv.exceptions.CsvException;

public class Dataproviders {

    @DataProvider(name = "LoginData")
    public Object[][] loginDataprovider() throws IOException, CsvException {
       
        List<String[]> Data = CSVUtils.readCSV("./src/test/resources/credential.csv");
        
        // Convert List<String[]> to Object[][]
        Object[][] result = new Object[Data.size()][3];
        for (int i = 0; i < Data.size(); i++) {
            result[i] = Data.get(i);
        }
        
        return result;
    }
}
