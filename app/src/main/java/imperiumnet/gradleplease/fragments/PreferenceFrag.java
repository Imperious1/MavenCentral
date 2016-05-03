package imperiumnet.gradleplease.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.activites.SearchActivity;
import imperiumnet.gradleplease.callbacks.Listeners;

public class PreferenceFrag extends PreferenceFragment {

    Listeners.ThemeSettingsChangeListener mSettingsListener;
    Listeners.ThemeSearchChangeListener mSearchListener;
    Listeners.DataSetChangedListener mDataListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingsListener = (Listeners.ThemeSettingsChangeListener) context;
        mSearchListener = (Listeners.ThemeSearchChangeListener) SearchActivity.getContext();
        mDataListener = (Listeners.DataSetChangedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Preference mListPreference = getPreferenceManager().findPreference("colors");
        Preference mEditPreference = getPreferenceManager().findPreference("custom");
        Preference mSwitchPreference = getPreferenceManager().findPreference("single");
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mSettingsListener.changeTheme((String) newValue);
                if (mSearchListener != null)
                    mSearchListener.changeTheme((String) newValue);
                return true;
            }
        });
        mEditPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDataListener.clearData(((String) newValue), false, false);
                return true;
            }
        });
        mSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDataListener.clearData(null, ((boolean) newValue), true);
                return true;
            }
        });
        return inflater.inflate(R.layout.frag_settings, container, false);
    }
}
