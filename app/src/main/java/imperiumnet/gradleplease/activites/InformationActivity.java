package imperiumnet.gradleplease.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import imperiumnet.gradleplease.R;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
