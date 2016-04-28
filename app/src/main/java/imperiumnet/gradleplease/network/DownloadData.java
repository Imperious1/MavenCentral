package imperiumnet.gradleplease.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadData {

    public static String queryGradle(String url) throws IOException {
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

    public static InputStream getXml(String url) throws IOException {
        HttpURLConnection mConnection;
        if (isHttps(url))
            mConnection = (HttpURLConnection) new URL(url).openConnection();
        else
            mConnection = (HttpsURLConnection) new URL(url).openConnection();
        return mConnection.getInputStream();
    }

    public static boolean isHttps(String url) {
        return url.contains("https");
    }
}
