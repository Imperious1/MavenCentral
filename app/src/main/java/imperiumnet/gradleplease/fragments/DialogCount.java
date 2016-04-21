package imperiumnet.gradleplease.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import imperiumnet.gradleplease.R;

/**
 * Created by overlord on 4/20/16.
 */
public class DialogCount extends DialogFragment {

    public interface Communicator {
        void hideFrag();
        void setResult(String number);
    }
    AppCompatImageButton close;
    Button btnSubmit;
    EditText userInput;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_dialog, container);
        close = (AppCompatImageButton) view.findViewById(R.id.close_result);
        btnSubmit = (Button) view.findViewById(R.id.apply_result);
        userInput = (EditText) view.findViewById(R.id.edit_result);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.hideFrag();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.setResult(userInput.getText().toString());
            }
        });
        return view;
    }
}
