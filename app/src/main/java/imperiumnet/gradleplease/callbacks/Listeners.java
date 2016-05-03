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

    public interface ThemeSettingsChangeListener {
        void changeTheme(String data);
    }

    public interface ThemeSearchChangeListener {
        void changeTheme(String data);
    }

    public interface DataSetChangedListener {
        void clearData(String data, boolean value, boolean isSwitch);
    }
}
