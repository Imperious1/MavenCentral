package imperiumnet.gradleplease.callbacks;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;

public class listeners {
    public interface TaskFinishedListener {
        void processFinish(String output) throws JSONException, ParseException, IOException, SAXException;
    }

    public interface DialogClickListeners {
        void hideFrag(boolean isResult);

        void setResult(String number);
    }
}
