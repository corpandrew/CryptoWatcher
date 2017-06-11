import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by corpa on 5/11/17.
 */
public class RESTfulUtils implements Job{

    public static Map<String, Ticker> getTickers() {
        String responseString = "";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://poloniex.com/public?command=returnTicker");
        HttpResponse response = null;
        try {
            response = client.execute(request);

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                responseString += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        return gson.fromJson(responseString, new TypeToken<Map<String, Ticker>>(){}.getType());

    }

    public static void playSound() throws MalformedURLException {

        URL soundFileUrl = new File("notification.wav").toURI().toURL();
        AudioClip clip = Applet.newAudioClip(soundFileUrl);
        clip.play();

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AppMain.marketsMap = getTickers();
    }
}
