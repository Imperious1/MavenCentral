package imperiumnet.gradleplease.callbacks;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;

public class Listeners {
    public interface TaskFinishedListener {
        void processFinish(String output) throws JSONException, ParseException, IOException, SAXException;
    }

    public interface DialogClickListeners {
        void hideFrag(boolean isResult);
    }

    public interface DataSetChangedListener {
        void clearData(String data, boolean value, boolean isSwitch);
    }

    public interface SwipeListener {
        void handleSwipe(int position);
    }

    public interface OnHistoryClickListener {
        void onHistoryClick(String query);
    }
}