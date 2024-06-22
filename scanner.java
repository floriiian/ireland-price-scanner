import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
public class IrelandScanner {

    public static void main(String[] args) throws FileNotFoundException {

        // Creating a HashMap which will hold Arrays filled with Strings
        HashMap<String, ArrayList<String>> countyPrices = new HashMap<>();

        // New Print-Writer which will write the wanted data
        PrintWriter output = new PrintWriter(new FileOutputStream("prices.txt"));

        // All Counties of Ireland inside an array
        String[] counties = {
                "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin", "Galway", "Kerry",
                "Kildare", "Kilkenny", "Laois", "Leitrim", "Limerick", "Longford", "Louth",
                "Mayo", "Meath", "Monaghan", "Offaly", "Roscommon", "Sligo", "Tipperary",
                "Waterford", "Westmeath", "Wexford", "Wicklow"
        };

        // Can be seen as a for loop, as county through counties.
        for (String county : counties) {
            // Adds a new array for every county inside the array
            countyPrices.put(county, new ArrayList<>());
        }

        // New File "Scanner" which reads specified content
        Scanner file = new Scanner(new File("files/PPR-ALL.csv"));
        try {
            // Loops through the file
            while (file.hasNextLine()) {
                String line = file.nextLine();
                /* Splits each line at "," which should NORMALLY make sure
                *  that data is seperated but this .csv is messed up.
                */
                String[] splitData = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                // Removes all artifacts that remain after slicing, so it only keeps alphabetic letters.
                String currentCounty = splitData[2].replaceAll("[^a-zA-Z]", "");
                String currentPrice = splitData[4].replaceAll("�", "").replaceAll(",","").split("\\.")[0].replaceAll("\"", "");

                // Creates a new Array which gets the current county as a key from county prices
                ArrayList<String> priceList = countyPrices.get(currentCounty);
                // If the key exists, add the current price.
                if (priceList != null) {
                    priceList.add(currentPrice);
                }
            }

            // Loops through countyPrices
            for (Map.Entry<String, ArrayList<String>> entry : countyPrices.entrySet()) {
                String county = entry.getKey();
                ArrayList<String> prices = entry.getValue();

                int propertyCount = 0;
                long pricesTogether = 0;

                // Loops through prices and adds them together, essentially trying to create an average
                for (String price : prices) {
                    propertyCount ++;
                    try {
                        pricesTogether += (int) Long.parseLong(price);
                    }catch(NumberFormatException e) {
                        System.out.println("Invalid: " + price);
                    }
                }
                // Writes it into the prices.txt which is generated on the fly
                output.write(county + "'s total price: " + pricesTogether + " | Total Count: " + propertyCount);
                // If property count is null it would result in an error while dividing, so I add this condition
                if(propertyCount != 0){
                    output.write(" | Average Price: " + (pricesTogether / propertyCount) + "€");
                }
                else {
                    output.write(" | Average Price: NaN");
                }
                output.println();
            }

        }catch(FileSystemNotFoundException e){
            System.out.println("File not found");
            // Closes output and file to prevent memory leaks
        }finally {
            output.close();
            file.close();
        }
    }
}
