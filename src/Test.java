import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
            JSONParser parser = new JSONParser();
            try (FileReader reader = new FileReader("src/Blocks.json")){

                JSONParser jsonParser = new JSONParser();

                File file = new File("src/Blocks.json");

                Object object = jsonParser.parse(new FileReader(file));

                JSONObject jsonObject = (JSONObject) object;



        }
    }
}
