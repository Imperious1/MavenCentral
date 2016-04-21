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

public class NetworkUtilsJson extends AsyncTask<String, Void, String> {

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

    public interface TaskFinishedListener {
        void processFinish(String output) throws JSONException, ParseException;
    }

    public TaskFinishedListener mTaskFinishedListener;

    public NetworkUtilsJson(TaskFinishedListener mTaskFinishListener) {
        this.mTaskFinishedListener = mTaskFinishListener;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            mTaskFinishedListener.processFinish(result);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String queryGradle(String url) throws IOException {
        HttpURLConnection mConnection;
        if (isHttps(url))
            mConnection = (HttpURLConnection) new URL(url).openConnection();
        else
            mConnection = (HttpsURLConnection) new URL(url).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null)
            sb.append(s);
        br.close();
        mConnection.disconnect();
        return sb.toString();
    }

    public boolean isHttps(String url) {
        return url.contains("https");
    }
}

