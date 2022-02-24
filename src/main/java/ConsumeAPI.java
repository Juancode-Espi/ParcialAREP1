import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ConsumeAPI {

    public static String getClima(String city) throws IOException {
        String res = "{}";
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=1dc647740ce39a8ad83463a91a3450c8");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                res += inputLine;
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        return res;
    }
}
