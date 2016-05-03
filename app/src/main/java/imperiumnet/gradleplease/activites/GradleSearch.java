package imperiumnet.gradleplease.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import imperiumnet.gradleplease.R;

public class GradleSearch extends AppCompatActivity {


    public static SharedPreferences mPreferences;

    public static int swapTheme() {
        switch (mPreferences.getString("colors", "R.style.TealTheme")) {
            case "color_deep_orange":
                return R.style.DeepOrangeTheme;
            case "color_teal":
                return R.style.TealTheme;
            case "color_red":
                return R.style.RedTheme;
            case "color_pink":
                return R.style.PinkTheme;
            case "color_purple":
                return R.style.PurpleTheme;
            case "color_deep_purple":
                return R.style.IndigoTheme;
            case "color_blue":
                return R.style.BlueTheme;
            case "color_light_blue":
                return R.style.LightBlueTheme;
            case "color_cyan":
                return R.style.CyanTheme;
            case "color_green":
                return R.style.GreenTheme;
            case "color_light_green":
                return R.style.LightGreenTheme;
            case "color_lime":
                return R.style.LimeTheme;
            case "color_yellow":
                return R.style.YellowTheme;
            case "color_amber":
                return R.style.AmberTheme;
            case "color_orange":
                return R.style.OrangeTheme;
            default:
                return R.style.TealTheme;
        }
    }

    public static int swapTheme(String value) {
        switch (value) {
            case "color_deep_orange":
                return R.style.DeepOrangeTheme;
            case "color_teal":
                return R.style.TealTheme;
            case "color_red":
                return R.style.RedTheme;
            case "color_pink":
                return R.style.PinkTheme;
            case "color_purple":
                return R.style.PurpleTheme;
            case "color_deep_purple":
                return R.style.IndigoTheme;
            case "color_blue":
                return R.style.BlueTheme;
            case "color_light_blue":
                return R.style.LightBlueTheme;
            case "color_cyan":
                return R.style.CyanTheme;
            case "color_green":
                return R.style.GreenTheme;
            case "color_light_green":
                return R.style.LightGreenTheme;
            case "color_lime":
                return R.style.LimeTheme;
            case "color_yellow":
                return R.style.YellowTheme;
            case "color_amber":
                return R.style.AmberTheme;
            case "color_orange":
                return R.style.OrangeTheme;
            default:
                return R.style.TealTheme;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gradle_search, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.setTheme(swapTheme());
        setContentView(R.layout.activity_gradle_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        assert mFab != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GradleSearch.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }
}