package imperiumnet.gradleplease.network;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by overlord on 4/15/16.
 */
public class NetworkUtilz extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String s;
        try {
            s = queryGradle(params[0]);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface AsyncResponse {
        void processFinish(String output) throws JSONException, ParseException;
    }

    public AsyncResponse delegate;

    public NetworkUtilz(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            delegate.processFinish(result);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String queryGradle(String url) throws IOException {
        HttpURLConnection connection;
        if (isHttps(url))
            connection = (HttpURLConnection) new URL(url).openConnection();
        else connection = (HttpsURLConnection) new URL(url).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null)
            sb.append(s);
        br.close();
        connection.disconnect();
        return sb.toString();
    }

    public boolean isHttps(String url) {
        return url.contains("https");
    }
}

