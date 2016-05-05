package imperiumnet.gradleplease.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.activites.SettingsActivity;
import imperiumnet.gradleplease.callbacks.Listeners;

public class PreferenceFrag extends PreferenceFragment {

    Listeners.DataSetChangedListener mOnDataChangedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDataChangedListener = (Listeners.DataSetChangedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Preference mListPreference = getPreferenceManager().findPreference("colors");
        Preference mEditPreference = getPreferenceManager().findPreference("custom");
        Preference mSwitchPreference = getPreferenceManager().findPreference("single");
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                startActivity(new Intent(container.getContext()
                        .getPackageManager()
                        .getLaunchIntentForPackage(container.getContext().getPackageName())
                        .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)));
                startActivity(new Intent(container.getContext(), SettingsActivity.class));
                return true;
            }
        });
        mEditPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mOnDataChangedListener.clearData(((String) newValue), false, false);
                return true;
            }
        });
        mSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mOnDataChangedListener.clearData(null, ((boolean) newValue), true);
                return true;
            }
        });
        return inflater.inflate(R.layout.frag_settings, container, false);
    }
}