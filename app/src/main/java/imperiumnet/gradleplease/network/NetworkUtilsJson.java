package imperiumnet.gradleplease.network;

import android.os.AsyncTask;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.utils.Constant;

public class NetworkUtilsJson extends AsyncTask<String, Void, String> {

    public Listeners.TaskFinishedListener mTaskFinishedListener;

    public NetworkUtilsJson(Listeners.TaskFinishedListener mTaskFinishListener) {
        this.mTaskFinishedListener = mTaskFinishListener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (params[0].equals(Constant.DENY)) {
                return DownloadData.queryGradle(params[1]);
            } else {
                InputStream is = DownloadData.getXml(params[1]);
                return parseXml(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            mTaskFinishedListener.processFinish(result);
        } catch (JSONException | ParseException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public String parseXml(InputStream inputStream) {
        DocumentBuilderFactory dcb = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dc = dcb.newDocumentBuilder();
            Document document = dc.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("description");
            Node node = null;
            if (nodeList.item(0) != null) {
                node = nodeList.item(0);
            }
            if (node != null) {
                return node.getTextContent();
            } else
                return null;

        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

