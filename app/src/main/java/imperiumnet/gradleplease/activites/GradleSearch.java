package imperiumnet.gradleplease.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import imperiumnet.gradleplease.R;

public class GradleSearch extends AppCompatActivity {


    FloatingActionButton fab;
    SharedPreferences preferences;
    private static final String themeKey = "theme_one_0101011";
    SharedPreferences.Editor editor;

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
            return true;
        } else if (id == R.id.color_one) {
            if (!preferences.getString(themeKey, "default").equals("theme1")) {
                swapTheme("theme1");
            }
            return true;
        } else if (id == R.id.color_two) {
            if (!preferences.getString(themeKey, "default").equals("themeDefault")) {
                swapTheme("themeDefault");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void swapTheme(String theme) {
        if (editor == null)
            editor = preferences.edit();
        editor.remove(themeKey);
        editor.putString(themeKey, theme);
        editor.apply();
        initialize();
    }

    public void initialize() {
        preferences = getSharedPreferences("Theme Settings", Context.MODE_PRIVATE);
        if (preferences.getString(themeKey, "default").equals("theme1"))
            super.setTheme(R.style.shadow);
        else if (preferences.getString(themeKey, "default").equals("themeDefault"))
            super.setTheme(R.style.shadowDefault);
        setContentView(R.layout.activity_gradle_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GradleSearch.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }

}
