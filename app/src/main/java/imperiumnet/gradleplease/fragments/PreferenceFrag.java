package imperiumnet.gradleplease.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.callbacks.Listeners;

public class PreferenceFrag extends PreferenceFragment {

    Listeners.ThemeChangeListener listener;
    Listeners.DataSetChangedListener dataListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listeners.ThemeChangeListener) context;
        dataListener = (Listeners.DataSetChangedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Preference mListPreference = getPreferenceManager().findPreference("colors");
        Preference mEditPreference = getPreferenceManager().findPreference("custom");
        Preference mSwitchPreference = getPreferenceManager().findPreference("single");
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listener.changeTheme((String) newValue);
                return true;
            }
        });
        mEditPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                dataListener.clearData(((String) newValue), false, false);
                return true;
            }
        });
        mSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                dataListener.clearData(null, ((boolean) newValue), true);
                return true;
            }
        });
        return inflater.inflate(R.layout.frag_settings, container, false);
    }
}
